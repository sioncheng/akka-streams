package actors

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.SupervisorStrategy
import akka.actor.typed.PostStop
import akka.actor.typed.PreRestart

object StoppableActor {
  sealed trait Message
  case class ProcessMessage(data: String) extends Message
  case class EndMessage() extends Message

  case class EndException(message: String) extends RuntimeException(message)

  def apply(): Behavior[Message] =
    Behaviors.supervise[Message](behavior())
        .onFailure[EndException](SupervisorStrategy.restart)

  def behavior(): Behavior[Message] =
    Behaviors.receive[Message] {(context, message) =>
        message match {
            case ProcessMessage(data) => 
                context.log.info(s"process $data")
                Behaviors.same
            case EndMessage() => 
                context.log.info(s"stop")
                //Behaviors.stopped
                throw new EndException("ending")
        }
    }.receiveSignal {
        case(context, PreRestart) =>
            context.log.info(s"#*#* stoppable actor is restarting")
            Behaviors.same
    }
}
