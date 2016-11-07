import java.time.Clock

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}
import com.google.inject.{AbstractModule, Inject, Singleton}
import play.api.Logger
import play.api.libs.ws.WSClient
import services.{ApplicationTimer, AtomicCounter, Counter}

import scala.concurrent.Await
import scala.util.Random

/**
  * This class is a Guice module that tells Guice how to bind several
  * different types. This Guice module is created when the Play
  * application starts.
  *
  * Play will automatically use any class called `Module` that is in
  * the root package. You can create modules in other locations by
  * adding `play.modules.enabled` settings to the `application.conf`
  * configuration file.
  */
class Module extends AbstractModule {

  override def configure() = {
    // Use the system clock as the default implementation of Clock
    bind(classOf[Clock]).toInstance(Clock.systemDefaultZone)
    // Ask Guice to create an instance of ApplicationTimer when the
    // application starts.
    bind(classOf[ApplicationTimer]).asEagerSingleton()
    // Set AtomicCounter as the implementation for Counter.
    bind(classOf[Counter]).to(classOf[AtomicCounter])

    bind(classOf[Bla]).asEagerSingleton()
  }

}

@Singleton
class Bla @Inject()(wsClient: WSClient,
                    implicit val actorSystem: ActorSystem) {

  Logger.info("Starting!!!")

  implicit val actorMaterializer = ActorMaterializer()

  import scala.concurrent.duration._

  Source
    .tick(1 second, 100 milliseconds, ())
    .to(Sink.foreach(_ => {
      Await.result(wsClient.url("http://localhost:9000").get(), Duration.Inf)
      Thread.sleep(new Random().nextInt(250))
    }))
    .async
    .run()

}
