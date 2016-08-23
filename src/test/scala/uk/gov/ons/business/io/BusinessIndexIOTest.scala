package uk.gov.ons.business.io

import org.apache.spark.rdd.RDD
import org.scalatest.{FlatSpec, Matchers}
import uk.gov.ons.business.TestData
import uk.gov.ons.business.model.BusinessIndexRecord
import uk.gov.ons.business.test.SparkContextsSuiteMixin
import uk.gov.ons.business.test.helpers.FileHelpers

class BusinessIndexIOTest extends FlatSpec with SparkContextsSuiteMixin with FileHelpers with TestData with Matchers {

  private val path: String = createTemporaryOutputPath("business-index")

  behavior of "Business Index IO tools"

  it should "be able to use BI reader to load business index saved by BI writer" in {
    val writer: BusinessIndexWriter = new BusinessIndexWriter()
    val reader: BusinessIndexReader = new BusinessIndexReader()

    writer.write(bussinessIndexContaining(businessIndexRecord), path)
    val businessIndex: RDD[BusinessIndexRecord] = reader.read(path)

    businessIndex.count() should be(1)
    businessIndex.first() should be(businessIndexRecord)
  }

  private def bussinessIndexContaining(businessIndexRecord: BusinessIndexRecord): RDD[BusinessIndexRecord] = {
    context.makeRDD(Seq(businessIndexRecord))
  }

}
