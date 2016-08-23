package uk.gov.ons.business.model

import scala.collection.Map

case class BusinessIndexRecord(id: String,
                               matchKey: String,
                               sourceRecords: Map[String, DataRecord])
