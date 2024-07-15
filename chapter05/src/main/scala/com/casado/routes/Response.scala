package com.casado.routes

import com.casado.repository.Product
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol
import com.casado.routes.Response.ResponseSuccess
import com.casado.routes.Response.ResponseProduct

object Response {
  case class ResponseSuccess(success: Boolean)
  case class ResponseProduct(list: List[Product])
}

trait ProductSerializerJSON extends SprayJsonSupport 
  with DefaultJsonProtocol {
  
  implicit val productJsonFormat = jsonFormat3(Product)
  implicit val responseSuccessJsonFormat = jsonFormat1(ResponseSuccess)
  implicit val responseProductJsonFormat = jsonFormat1(ResponseProduct)
}