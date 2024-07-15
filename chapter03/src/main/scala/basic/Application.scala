package basic

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors

object Application {
  def main(args: Array[String]) : Unit = {
    val system : ActorSystem[_] = ActorSystem(Behaviors.empty, "akka")
    // val actorDB = system.systemActorOf(DBActor(), "dbActor")
    // actorDB ! DBActor.BankMessage("a", "b")
    val actorAPI = system.systemActorOf(APIActor(), "apiActor")
    actorAPI ! APIActor.MessageApi("test", "123456")
  }
}
