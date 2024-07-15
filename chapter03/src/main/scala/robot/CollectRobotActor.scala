package robot

import akka.actor.typed.Behavior

import RobotCommand._
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.PostStop
import akka.actor.typed.ActorRef

import scala.concurrent.duration._

object CollectRobotActor {
    def apply(): Behavior[CollectCommand] =
        Behaviors.withStash(4) {buffer =>
            Behaviors.setup { context =>
                val trasmitActorRef = context.spawn(TransmitRobotActor(), "TransmitRobotActor")
                
                Behaviors.receiveMessage[CollectCommand] { (message) =>
                    message match {
                        case Collect(coleta) =>
                            context.scheduleOnce(1.minutes, context.self, StartTransmission())
                            if (buffer.isFull) {
                                context.log.info(s"Collection limit reached! Aborting!")
                                buffer.unstashAll(transmitter(trasmitActorRef))
                                Behaviors.stopped
                            } else {
                                buffer.stash(message)
                                context.log.info(s"sample $message collected")
                                Behaviors.same
                            }
                        case StartTransmission() =>
                            buffer.unstashAll(transmitter(trasmitActorRef))
                            Behaviors.same
                        case _ =>
                            context.log.info(s"message $message invalid")
                            Behaviors.same
                    }
                }.receiveSignal {
                    case (context, PostStop) =>
                        context.log.info(s"Collection model stopped. Restart it if neccessary.")
                        Behaviors.stopped
                }
            }
        }

    def transmitter(trasmitActorRef: ActorRef[CollectCommand]) : Behavior[CollectCommand] = 
        Behaviors.setup { context =>
            Behaviors.receive {(context, message) =>
                message match {
                    case Collect(coleta) =>
                        context.log.info(s"transmitter trans $message")
                        trasmitActorRef ! Transmit(message)
                    case _ =>
                        context.log.info(s"message $message invalid")
                }
                Behaviors.same
            }
        }
}
