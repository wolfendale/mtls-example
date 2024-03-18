package connectors

import play.api.ApplicationLoader.Context
import play.api.inject.guice.{GuiceApplicationBuilder, GuiceApplicationLoader}

class Base64AwareApplicationLoader extends GuiceApplicationLoader with Base64ConfigDecoder {

  override def builder(context: Context): GuiceApplicationBuilder =
    super.builder(context.copy(initialConfiguration = decodeConfig(context.initialConfiguration)))
}