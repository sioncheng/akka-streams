package com.casado

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model._

import com.casado.repository._
import akka.actor.typed.ActorRef
import com.casado.service.ProductService
import com.casado.routes.ProductRoute

object Boot extends App {

  // val db = repository.DBConnection.db

  // println(s"db $db")

  println("Boot App")

  implicit val system = ActorSystem(Behaviors.empty, "Boot")

  implicit val ec = system.executionContext

  implicit val scheduler = system.scheduler

  val hello = 
    path("hello") {
      get {
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, """
          <html>
            <head>
              <title>hello</title>
            </head>
            <body>
              <h1>Hello Akka!</h1>
            </body>
          </html>
        """))
      }
    }

  val productService: ActorRef[ProductService.ProductMessage] =
    system.systemActorOf(ProductService(), "ProductService")

  val productRoute = ProductRoute.routes(productService)  
  val route = hello ~ productRoute

  val bindingFuture = Http().newServerAt("0.0.0.0", 8080).bind(route)

  // ProductRepository.create(Product(0, "p1", 100)).onComplete(p => println(p))

  scala.io.StdIn.readLine("press enter to exit\r\n")

  bindingFuture.flatMap(_.unbind()).onComplete(_ => system.terminate())
}
