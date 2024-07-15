package basic

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.scaladsl.ActorContext
import akka.actor.typed.scaladsl.AbstractBehavior
import akka.actor.typed.Behavior

class OODBActor(context: ActorContext[DBActor.BankMessage]) extends AbstractBehavior(context) {

    override def onMessage(msg: DBActor.BankMessage): Behavior[DBActor.BankMessage] = msg match {
        case DBActor.BankMessage(name, document) => 
            context.log.debug(s"onMessage $name $document") 
            this
    }
    
}
