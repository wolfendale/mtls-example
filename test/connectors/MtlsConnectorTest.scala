package connectors

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock._
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach}
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.ws.WSClient

class MtlsConnectorTest extends AnyFreeSpec with Matchers with ScalaFutures with IntegrationPatience with BeforeAndAfterEach with BeforeAndAfterAll with Base64ConfigDecoder {

  private val server: WireMockServer =
    new WireMockServer(
      wireMockConfig()
        .dynamicHttpsPort()
        .keystorePath("test/resources/tls/server/server-keystore.p12")
        .trustStorePath("test/resources/tls/server/server-truststore.p12")
        .httpDisabled(true)
        .needClientAuth(true)
    )

  override def beforeAll(): Unit = {
    server.start()
    super.beforeAll()
  }

  override def beforeEach(): Unit = {
    server.resetAll()
    super.beforeEach()
  }

  override def afterAll(): Unit = {
    super.afterAll()
    server.stop()
  }

  private lazy val app = GuiceApplicationBuilder()
    .configure(
      "play.ws.ssl.keyManager.stores.0.type" -> "pkcs12",
      "play.ws.ssl.keyManager.stores.0.password" -> "password",
      "play.ws.ssl.keyManager.stores.0.path" -> "test/resources/tls/client/client-keystore.p12"
    )
    .build()

  private lazy val wsClient = app.injector.instanceOf[WSClient]

  "must be configured to use mutual authentication" in {

    val url = s"https://localhost:${server.httpsPort()}/test"

    server.stubFor(
      get(urlPathEqualTo("/test"))
        .willReturn(aResponse().withStatus(200))
    )

    val response = wsClient.url(url)
      .get().futureValue

    response.status mustEqual 200
  }
}