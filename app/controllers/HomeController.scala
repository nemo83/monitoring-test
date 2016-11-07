package controllers

import javax.inject._

import org.lyranthe.prometheus.client._
import play.api.mvc._

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class HomeController @Inject() extends Controller {

  implicit val defaultRegistry = DefaultRegistry()

  jmx.unsafeRegister()

  private val counter = Counter(metric"my_counter", "My Counter").labels().unsafeRegister

  /**
    * Create an Action to render an HTML page with a welcome message.
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */
  def index = Action {
    counter.inc()
    Ok(views.html.index("Your new application is ready."))
  }

  def metrics = Action {
    Ok(defaultRegistry.outputText)
  }

}
