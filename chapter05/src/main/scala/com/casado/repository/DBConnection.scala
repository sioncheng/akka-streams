package com.casado.repository

import slick.jdbc.MySQLProfile.api._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import slick.dbio.Effect.Read
import slick.basic.DatabasePublisher

trait DBConnection {
    val disableAutocommit = 
        SimpleDBIO(_.connection.setAutoCommit(false))
    
    def run[T](action: DBIOAction[T, NoStream, Nothing]): Future[T] =
        DBConnection.db.run(action)
    
    def stream[T](action: DBIOAction[Seq[T],Streaming[T], Read]): DatabasePublisher[T] = {
        DBConnection.db.stream(disableAutocommit andThen action.withStatementParameters(fetchSize = 1000))
    }
    
}


object DBConnection {
  lazy val db = Database.forConfig("mydb")
}
