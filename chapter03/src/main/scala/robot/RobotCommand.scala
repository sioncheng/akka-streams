package robot

import java.util.UUID

object RobotCommand {
  sealed trait MoveCommand

  case class MoveForward() extends MoveCommand
  case class MoveBackward() extends MoveCommand
  case class MoveLeft() extends MoveCommand
  case class MoveRight() extends MoveCommand

  sealed trait CollectCommand
  sealed case class Collection(uuid: UUID, peso: Double)
  case class Collect(coleta: Collection) extends CollectCommand
  case class StartTransmission() extends CollectCommand
  case class Transmit(coleta: CollectCommand) extends CollectCommand
}
