import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors

object Boot extends App {
  val actorSystem = ActorSystem(Behaviors.empty, "ActorSystem")
  val ec = actorSystem.executionContext
  
  println(s"Boot chapter08")
}
