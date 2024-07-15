package actors

import akka.actor.typed.SpawnProtocol
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.Behavior
import akka.actor.typed.Terminated

object ActorSetup {
//   def apply(): Behavior[SpawnProtocol.Command] = 
//     Behaviors.setup {context =>
//         context.spawn(CallerActor(), "CallerActor")

//         Behaviors.receiveSignal[SpawnProtocol.Command] {
//             case (_, Terminated(_)) =>
//                 Behaviors.stopped
//         }

//         SpawnProtocol()
//     }
}
