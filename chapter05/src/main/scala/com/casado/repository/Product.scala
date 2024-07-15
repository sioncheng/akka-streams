package com.casado.repository

import scala.concurrent.{Future, Await}
import scala.concurrent.duration.Duration
import slick.jdbc.MySQLProfile.api._
import java.sql.Date
import scala.reflect.ClassTag
import slick.lifted.ProvenShape
import com.casado.repository
import scala.concurrent.ExecutionContext
import slick.basic.DatabasePublisher

case class Product(id: Long, description: String, price: Double)

class ProductSchema(tag: Tag) extends Table[Product](tag, "product") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def description = column[String]("description")
    def price = column[Double]("price")
    def * = (id, description, price) <> (Product.tupled, Product.unapply)
}

object ProductRepository extends DBConnection {
    private val table = TableQuery[ProductSchema]
    val schema = table.schema
    run(DBIO.seq(schema.createIfNotExists))

    def create(product: Product): Future[Product] = {
        val insertSql = table returning(table).map(_.id) into ((x, id) => x.copy(id = id))
        val action = insertSql += product
        run {
            println(s"##action $action")
            action
        }
    }

    def update(product: Product) : Future[Int] = {
        val updateSql = table.filter(_.id === product.id).update(product)
        run(updateSql)
    }

    def remove(id: Long): Future[Int] = {
        run(table.filter(_.id === id).delete)
    } 

    def fetchById(id: Long): DatabasePublisher[Product] = {
        stream(table.filter(_.id === id).result)
    }

    def fetchByDescription(desc: String): DatabasePublisher[Product] = {
        stream(table.filter(_.description like s"${desc}%").result)
    }

}