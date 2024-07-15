package actors

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import actors.StoppableActor.ProcessMessage
import actors.StoppableActor.EndMessage

object CallerActor {
  sealed trait CallerMessage
  case class CallerProcessMessage(data: String) extends CallerMessage
  case class CallerEndMessage() extends CallerMessage

  def apply(): Behavior[CallerMessage] = 
    Behaviors.setup[CallerMessage] {context =>
        val stoppableActor = context.spawn(StoppableActor(), "StoppableActor")

        Behaviors.receive { (context, message) =>
            message match {
                case CallerProcessMessage(data) => 
                    context.log.info(s"caller process $data")
                    stoppableActor ! ProcessMessage(data)
                    Behaviors.same
                case CallerEndMessage() => 
                    context.log.info(s"caller stop")
                    stoppableActor ! EndMessage()
                    Behaviors.same
            }
        }
    }
}
