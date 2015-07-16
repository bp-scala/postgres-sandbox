package com.samebug.sandbox

import java.sql.Timestamp

import org.scalatest.{BeforeAndAfter, BeforeAndAfterAll, FlatSpec, Matchers}

import scala.slick.driver.PostgresDriver
import scala.slick.driver.PostgresDriver.profile.simple._
import scala.slick.jdbc.meta.MTable

/**
 * Created by poroszd on 7/15/15.
 */
class DocumentsTest extends FlatSpec with Matchers with BeforeAndAfterAll with BeforeAndAfter {
  private var db: PostgresDriver.profile.backend.DatabaseDef = _

  behavior of "documents table"

  it should "have no rows" in {
    val size = db.withSession { implicit s =>
      TestSchema.documents.size.run
    }
    size shouldBe 0
  }

  it should "have a row inserted with autoincremented id" in {
    val row = TestSchema.DocumentRow(-1, "me", new Timestamp(100))
    val id = db.withSession { implicit s =>
      TestSchema.documentsInserter += row
    }

    val rows = db.withSession { implicit s =>
      TestSchema.documents.list
    }
    rows should have size 1
    rows.head shouldBe row.copy(id = id)
  }

  it should "query rows by the date field" in {
    val rowsToInsert = Seq(TestSchema.DocumentRow(-1, "me", new Timestamp(10)), TestSchema.DocumentRow(-1, "you", new Timestamp(20)))
    val ids = db.withSession { implicit s =>
      TestSchema.documentsInserter ++= rowsToInsert
    }

    val rows = db.withSession { implicit s =>
      TestSchema.documents.filter {_.created > new Timestamp(15)}.list
    }
    val secondRow = rowsToInsert(1).copy(id = ids(1))
    rows should have size 1
    rows.head shouldBe secondRow
  }

  override def beforeAll = {
    db = Database.forURL("jdbc:postgresql://localhost:5432/test")
    db.withSession { implicit s =>
      if (MTable.getTables(TestSchema.documents.baseTableRow.tableName).firstOption.nonEmpty) TestSchema.documents.ddl.drop
      TestSchema.documents.ddl.create
    }
  }

  before {
    db.withSession { implicit s =>
      TestSchema.documents.delete
    }
  }
}
