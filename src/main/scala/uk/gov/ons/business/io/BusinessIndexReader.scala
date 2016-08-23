package uk.gov.ons.business.io

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SQLContext
import uk.gov.ons.business.model.BusinessIndexRecord

class BusinessIndexReader(implicit sqlContext: SQLContext) extends Serializable {

  def read(filePath: String): RDD[BusinessIndexRecord] = {
    import sqlContext.implicits._

    sqlContext.read.parquet(filePath).as[BusinessIndexRecord].rdd
  }
}
