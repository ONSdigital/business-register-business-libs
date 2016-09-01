package uk.gov.ons.business.model

import scala.collection.Map

case class DataRecord(dataSource: String,
                      recordDigest: String,
                      companyName: String,
                      industryCode: Option[Int] = None,
                      legalStatus: Option[String] = None,
                      tradingStatus: Option[String] = None,
                      turnover: Option[Int] = None,
                      address: Option[String],
                      postcode: Option[String],
                      totalEmployees: Option[Int] = None,
                      additionalData: Map[String, String] = Map(),
                      matchScore: Option[Double] = None,
                      // TODO: this field is populated for VAT only. Populate for other sources.
                      sourceSpecificId: Option[String] = None)
