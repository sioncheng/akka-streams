package robot

import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

object TransmitRobotActor {
  def apply(): Behavior[RobotCommand.CollectCommand] =
    Behaviors.receive { (context, message) =>
        context.log.info(s"sample ${message} collected successfully " +
          s"transmitted to base operations")
        Behaviors.same
    }
}
