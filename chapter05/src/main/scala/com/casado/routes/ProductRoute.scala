package com.casado.routes

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import com.casado.repository.Product
import scala.concurrent.Future
import com.casado.service.ProductService
import akka.actor.typed.ActorRef
import akka.actor.typed.scaladsl.AskPattern._
import scala.concurrent.duration._
import akka.util.Timeout
import scala.concurrent.ExecutionContext
import akka.actor.typed.Scheduler
import scala.util.Success
import akka.http.scaladsl.model.StatusCodes
import scala.util.Failure
import com.casado.Boot.productService
import com.casado.Boot.scheduler
import com.casado.service.ProductService.ProductFound
import akka.stream.scaladsl.Source
import akka.stream.scaladsl.Sink
import akka.actor.typed.ActorSystem
import akka.http.scaladsl.server.StandardRoute
import akka.http.scaladsl.server.RouteResult
import akka.http.scaladsl.server.RequestContext

object ProductRoute extends ProductSerializerJSON {

  implicit val timeout = Timeout(3.seconds)
  
  def routes(productService: ActorRef[ProductService.ProductMessage])
    (implicit system: ActorSystem[_]): Route = 
        createProduct(productService) ~ 
            updateProduct(productService) ~
            removeProduct(productService) ~
            findById(productService) ~
            findByDesc(productService)

  def createProduct(productService: ActorRef[ProductService.ProductMessage])
    (implicit system: ActorSystem[_]): Route = post {
    path("product") {
        entity(as[Product]) { product =>
            val scheduler = system.scheduler
            val response : Future[ProductService.ProductResponse] =
                productService.ask(ref => ProductService.CreateProduct(product, ref))
                
            onComplete(response) {
                case Success(value) => 
                    value match {
                        case ProductService.ProductManagementSuccess(v) =>
                            complete(Response.ResponseSuccess(true))
                        case _ =>
                            complete(StatusCodes.BadRequest)
                    }
                case Failure(exception) => 
                    failWith(exception)
            }
        }
    }
  }

  def updateProduct(productService: ActorRef[ProductService.ProductMessage])
    (implicit system: ActorSystem[_]): Route = patch {
    path("product") {
        entity(as[Product]) { product =>
            val scheduler = system.scheduler
            val response : Future[ProductService.ProductResponse] =
                productService.ask(ref => ProductService.UpdateProduct(product, ref))
                
            onComplete(response) {
                case Success(value) => 
                    value match {
                        case ProductService.ProductManagementSuccess(v) =>
                            complete(Response.ResponseSuccess(true))
                        case _ =>
                            complete(StatusCodes.BadRequest)
                    }
                case Failure(exception) => 
                    failWith(exception)
            }
        }
    }
  }

  def removeProduct(productService: ActorRef[ProductService.ProductMessage])
    (implicit system: ActorSystem[_]): Route = delete {
    path("product" / Segment) {
        id =>
        val scheduler = system.scheduler
        val response : Future[ProductService.ProductResponse] =
            productService.ask(ref => ProductService.RemoveProduct(id.toLong, ref))
            
        onComplete(response) {
            case Success(value) => 
                value match {
                    case ProductService.ProductManagementSuccess(v) =>
                        complete(Response.ResponseSuccess(true))
                    case _ =>
                        complete(StatusCodes.BadRequest)
                }
            case Failure(exception) => 
                failWith(exception)
        }
        
    }
  }

  def findById(productService: ActorRef[ProductService.ProductMessage])
    (implicit system: ActorSystem[_]): Route = get {
        path("product" / "id" / Segment) { id =>
            val scheduler = system.scheduler
            val response: Future[ProductService.ProductResponse] =
                productService.ask(ref => ProductService.FindProduct(id.toLong, ref))
            
            completeResponse(response)
        }
    }

  def findByDesc(productService: ActorRef[ProductService.ProductMessage])
    (implicit system: ActorSystem[_]): Route = get {
        path("product" / "desc" / Segment) { desc =>
            val scheduler = system.scheduler
            val response: Future[ProductService.ProductResponse] =
                productService.ask(ref => ProductService.SearchProduct(desc, ref))

            completeResponse(response)
        }
    }

    private def completeResponse(response : Future[ProductService.ProductResponse])
        (implicit system: ActorSystem[_]):(RequestContext => Future[RouteResult]) = {
        onComplete(response) {
                case Success(value) => 
                    value match {
                        case ProductFound(publisher) => 
                            implicit val ec = system.executionContext
                            val data = Source.fromPublisher(publisher)
                                .runWith(Sink.collection[Product, List[Product]])
                                .map {
                                    productList =>
                                        Response.ResponseProduct(productList)
                                }
                            complete(data)
                        case _ =>
                            complete(StatusCodes.BadRequest)
                        }
                case Failure(ex) =>
                    failWith(ex)
            }
    }
}
