package uk.gov.ons.business.io

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SQLContext
import uk.gov.ons.business.model.BusinessIndexRecord

class BusinessIndexWriter(implicit sqlContext: SQLContext) extends Serializable {

  def write(data: RDD[BusinessIndexRecord], filePath: String) {
    import sqlContext.implicits._

    data.toDF().write.parquet(filePath)
  }
}
