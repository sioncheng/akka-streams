package com.casado.service

import akka.event.slf4j.Logger
import com.casado.repository
import akka.actor.typed.ActorRef

import repository.Product
import repository.ProductRepository
import slick.basic.DatabasePublisher
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.SupervisorStrategy
import scala.util.Success
import scala.util.Failure

object ProductService {
  val logger = Logger("ProductService")

  sealed trait ProductMessage
  sealed trait ProductResponse

  case class CreateProduct(product: Product,
    replyTo: ActorRef[ProductResponse]
  ) extends ProductMessage

  case class UpdateProduct(product: Product,
    replyTo: ActorRef[ProductResponse]
  ) extends ProductMessage

  case class RemoveProduct(id: Long,
    replyTo: ActorRef[ProductResponse]
  ) extends ProductMessage

  case class FindProduct(id: Long,
      replyTo: ActorRef[ProductResponse]
  ) extends ProductMessage

  case class SearchProduct(desc: String,
      replyTo: ActorRef[ProductResponse]
  ) extends ProductMessage

  case class ProductManagementSuccess(value: Long) 
    extends ProductResponse
  case class ProductManagementFailed(e: Throwable) 
    extends ProductResponse
  case class ProductFound(publisher: DatabasePublisher[Product]) 
    extends ProductResponse


  def apply(): Behavior[ProductMessage] =
    Behaviors.supervise[ProductMessage](behavior())
        .onFailure[Exception](SupervisorStrategy.restart)

  def behavior(): Behavior[ProductMessage] =
    Behaviors.receive { (ctx,msg) =>
        implicit val ec = ctx.executionContext

        msg match {
            case CreateProduct(product, replyTo) => 
                ProductRepository.create(product)
                    .onComplete {
                        case Success(p) => 
                            replyTo ! ProductManagementSuccess(p.id)
                        case Failure(e) => 
                            replyTo ! ProductManagementFailed(e)
                    }
                Behaviors.same
            case UpdateProduct(product, replyTo) => 
                ProductRepository.update(product)
                    .onComplete {
                        case Success(value) => 
                            replyTo ! ProductManagementSuccess(value)
                        case Failure(e) => 
                            replyTo ! ProductManagementFailed(e)
                    }
                Behaviors.same
            case RemoveProduct(id, replyTo) => 
                ProductRepository.remove(id)
                    .onComplete {
                      case Success(value) => 
                        replyTo ! ProductManagementSuccess(value)
                      case Failure(e) =>
                        replyTo ! ProductManagementFailed(e)
                    }
                Behaviors.same
            case FindProduct(id, replyTo) => 
              val pub = ProductRepository.fetchById(id)
              replyTo ! ProductFound(pub)
              Behaviors.same
            case SearchProduct(desc, replyTo) => 
              val pub = ProductRepository.fetchByDescription(desc)
              replyTo ! ProductFound(pub)
              Behaviors.same
        }
    }
}
