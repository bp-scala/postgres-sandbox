package com.samebug.sandbox

import java.sql.Timestamp

import scala.slick.driver.PostgresDriver.profile.simple._

object TestSchema {
  val documents = TableQuery[DocumentsTable]
  val documentsInserter = documents returning (documents.map {_.id})

  case class DocumentRow(id: Int, author: String, created: Timestamp)

  class DocumentsTable(tag: Tag) extends Table[DocumentRow](tag, "documents") {
    val id = column[Int]("id", O.AutoInc, O.PrimaryKey)
    val author = column[String]("name", O.DBType("VARCHAR(255)"))
    val created = column[Timestamp]("created")
    def * = (id, author, created) <>(DocumentRow.tupled, DocumentRow.unapply)

  }

}
