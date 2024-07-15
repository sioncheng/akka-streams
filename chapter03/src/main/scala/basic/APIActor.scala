package basic

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import basic.DBActor
import akka.actor.typed.scaladsl.ActorContext

object APIActor {
  case class MessageApi(name: String, document: String)

  def apply(): Behavior[MessageApi] = Behaviors.setup { ctx =>
    //val refActorDB = ctx.spawn(DBActor(), "dbActor")
    val refActorDB = ctx.spawn(Behaviors.setup{
      context: ActorContext[DBActor.BankMessage] => new OODBActor(context)
    }, "ooDBActor")
    Behaviors.receive { (context, message) => 
      context.log.debug(s"$message received and send to db")
      refActorDB ! DBActor.BankMessage(message.name, message.document)
      Behaviors.same
    }
  }
}
