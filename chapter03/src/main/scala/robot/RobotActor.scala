package robot

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

import RobotCommand._

object RobotActor {
  def apply(): Behavior[MoveCommand] =
    Behaviors.receive{(context, message) =>
        message match {
            case MoveForward() =>
                context.log.info(s"moved forward")
                Behaviors.same
            case MoveBackward() =>
                context.log.info(s"moved backward")
                Behaviors.same
            case MoveLeft() =>
                context.log.info(s"moved left")
                Behaviors.same
            case MoveRight() =>
                context.log.info(s"moved right")
                Behaviors.same
        }
    }
}
