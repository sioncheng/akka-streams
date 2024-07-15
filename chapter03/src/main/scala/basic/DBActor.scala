package basic

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

object DBActor {
  case class BankMessage(name: String, document: String)

  def apply(): Behavior[BankMessage] =
    Behaviors.receive {(ctx, msg) => {
      ctx.log.debug(s"message ${msg}")
      Behaviors.same
    }}
}
