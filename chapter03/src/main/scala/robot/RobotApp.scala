package robot

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors

import RobotCommand._
import java.util.UUID

object RobotApp {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem(Behaviors.empty, "robotApp")
    val collectActor = 
        system.systemActorOf(CollectRobotActor(), "CollecRobotActor")
    val robotActor = 
        system.systemActorOf(RobotActor(), "RobotActor")
    
    robotActor ! MoveForward()

    collectActor ! Collect(
        Collection(UUID.randomUUID(), scala.util.Random.nextDouble()))

    collectActor ! Collect(
        Collection(UUID.randomUUID(), scala.util.Random.nextDouble()))

    collectActor ! Collect(
        Collection(UUID.randomUUID(), scala.util.Random.nextDouble()))

    collectActor ! Collect(
        Collection(UUID.randomUUID(), scala.util.Random.nextDouble()))


    // collectActor ! Collect(
    //     Collection(UUID.randomUUID(), scala.util.Random.nextDouble()))

    //collectActor ! StartTransmission()

    scala.io.StdIn.readLine("press enter to exit")
  }
}
