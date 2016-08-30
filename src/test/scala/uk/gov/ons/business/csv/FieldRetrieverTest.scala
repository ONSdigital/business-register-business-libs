package uk.gov.ons.business.csv

import org.scalatest.{FunSuite, Matchers}
import uk.gov.ons.business.model.{BusinessIndexRecord, DataRecord}

class FieldRetrieverTest extends FunSuite with Matchers {

  val charityCommission = "CharityCommission"
  val companiesHouse = "CompaniesHouse"
  val hmrcVisionVat = "HMRCVisionVat"
  val quarterlyPaye = "QuarterlyPaye"

  val fieldRetriever = new FieldRetriever()

  test("getField(BusinessIndexRecord) returns the right data field from the source") {
    val companiesRecord = getSampleDataRecord(dataSource = companiesHouse)
    val sourcesRecords: Map[String, DataRecord] = Map(
      companiesHouse -> companiesRecord
    )
    val businessIndexRecord = getSampleBusinessRecord(sourceRecords = sourcesRecords)

    val companyName = fieldRetriever.getField(businessIndexRecord, DataRecordField.CompanyName, List(companiesHouse))
    val postcode = fieldRetriever.getField(businessIndexRecord, DataRecordField.Postcode, List(companiesHouse))
    val industryCode = fieldRetriever.getField(businessIndexRecord, DataRecordField.IndustryCode, List(companiesHouse))
    val legalStatus = fieldRetriever.getField(businessIndexRecord, DataRecordField.LegalStatus, List(companiesHouse))
    val tradingStatus = fieldRetriever.getField(businessIndexRecord, DataRecordField.TradingStatus, List(companiesHouse))
    val turnover = fieldRetriever.getField(businessIndexRecord, DataRecordField.Turnover, List(companiesHouse))
    val totalEmployees = fieldRetriever.getField(businessIndexRecord, DataRecordField.TotalEmployees, List(companiesHouse))

    companyName should be(companiesRecord.companyName)
    postcode should be(companiesRecord.postcode.get)
    industryCode should be(companiesRecord.industryCode.get.toString)
    legalStatus should be(companiesRecord.legalStatus.get)
    tradingStatus should be(companiesRecord.tradingStatus.get)
    turnover should be(companiesRecord.turnover.map(_.toString()).get)
    totalEmployees should be(companiesRecord.totalEmployees.map(_.toString()).get)
  }

  test("getField(BusinessIndexRecord) returns an empty string when none of the given sources are present in record") {
    val prioritySources = List(companiesHouse, charityCommission, hmrcVisionVat)

    val sourcesRecords: Map[String, DataRecord] = Map(
      quarterlyPaye -> getSampleDataRecord(dataSource = quarterlyPaye, tradingStatus = Some("90009"))
    )
    val businessIndexRecord = getSampleBusinessRecord(sourceRecords = sourcesRecords)

    val tradingStatus = fieldRetriever.getField(businessIndexRecord, DataRecordField.TradingStatus, prioritySources)
    tradingStatus should be ("")
  }

  test("getField(BusinessIndexRecord) returns an empty string when the field is empty in each of the relevant sources") {
    val prioritySources = List(companiesHouse, quarterlyPaye, hmrcVisionVat)

    val companiesRecord = getSampleDataRecord(dataSource = companiesHouse, industryCode = None)
    val payeRecord = getSampleDataRecord(dataSource = quarterlyPaye, industryCode = None)
    val vatRecord = getSampleDataRecord(dataSource = hmrcVisionVat, industryCode = None)

    val sourceRecords: Map[String, DataRecord] = Map(
      companiesHouse -> companiesRecord,
      quarterlyPaye -> payeRecord,
      hmrcVisionVat -> vatRecord
    )
    val businessIndexRecord = getSampleBusinessRecord(sourceRecords = sourceRecords)

    val industryCode = fieldRetriever.getField(businessIndexRecord, DataRecordField.IndustryCode, prioritySources)
    industryCode should be ("")
  }

  test("getField(BusinessIndexRecord) returns the first present value based on the priority list") {
    val firstPriorityList = List(companiesHouse, charityCommission, hmrcVisionVat)
    val secondPriorityList =  List(companiesHouse, hmrcVisionVat, charityCommission)

    val companiesRecord = getSampleDataRecord(dataSource = companiesHouse, industryCode = None)
    val charityRecord = getSampleDataRecord(dataSource = charityCommission, industryCode = Some(90001))
    val vatRecord = getSampleDataRecord(dataSource = hmrcVisionVat, industryCode = Some(90008))

    val dataSources: Map[String, DataRecord] = Map(
      companiesHouse -> companiesRecord,
      charityCommission -> charityRecord,
      hmrcVisionVat -> vatRecord
    )
    val businessIndexRecord = getSampleBusinessRecord(sourceRecords = dataSources)

    val charityIndustryCode = fieldRetriever.getField(businessIndexRecord, DataRecordField.IndustryCode, firstPriorityList)
    charityIndustryCode should be ("90001")

    val vatIndustryCode = fieldRetriever.getField(businessIndexRecord, DataRecordField.IndustryCode, secondPriorityList)
    vatIndustryCode should be ("90008")
  }

  test("getField(DataRecord) returns the given field from the given record") {
    val sampleDataRecord = getSampleDataRecord()

    val companyName = fieldRetriever.getField(sampleDataRecord, DataRecordField.CompanyName)
    val postcode = fieldRetriever.getField(sampleDataRecord, DataRecordField.Postcode)
    val industryCode = fieldRetriever.getField(sampleDataRecord, DataRecordField.IndustryCode)
    val tradingStatus = fieldRetriever.getField(sampleDataRecord, DataRecordField.TradingStatus)
    val legalStatus = fieldRetriever.getField(sampleDataRecord, DataRecordField.LegalStatus)
    val turnover = fieldRetriever.getField(sampleDataRecord, DataRecordField.Turnover)
    val totalEmployees = fieldRetriever.getField(sampleDataRecord, DataRecordField.TotalEmployees)

    companyName should be(sampleDataRecord.companyName)
    postcode should be(sampleDataRecord.postcode.get)
    industryCode should be(sampleDataRecord.industryCode.get.toString)
    legalStatus should be(sampleDataRecord.legalStatus.get)
    tradingStatus should be(sampleDataRecord.tradingStatus.get)
    turnover should be(sampleDataRecord.turnover.map(_.toString()).get)
    totalEmployees should be(sampleDataRecord.totalEmployees.map(_.toString()).get)
  }

  test("getField(DataRecord) returns an empty string when the given field is absent") {
    val dataRecord: DataRecord = getSampleDataRecord(companyName = null, tradingStatus = None, postcode = None,
      turnover = None, legalStatus = None, totalEmployees = None, industryCode = None)

    val companyName = fieldRetriever.getField(dataRecord, DataRecordField.CompanyName)
    val postcode = fieldRetriever.getField(dataRecord, DataRecordField.Postcode)
    val tradingStatus = fieldRetriever.getField(dataRecord, DataRecordField.TradingStatus)
    val turnover = fieldRetriever.getField(dataRecord, DataRecordField.Turnover)
    val legalStatus = fieldRetriever.getField(dataRecord, DataRecordField.LegalStatus)
    val totalEmployees = fieldRetriever.getField(dataRecord, DataRecordField.TotalEmployees)
    val industryCode = fieldRetriever.getField(dataRecord, DataRecordField.IndustryCode)

    postcode should be ("")
    tradingStatus should be ("")
    turnover should be ("")
    legalStatus should be ("")
    totalEmployees should be ("")
    industryCode should be ("")
  }

  private def getSampleBusinessRecord(businessId: String = "190093",
                              sourceRecords: Map[String, DataRecord]) = {

    BusinessIndexRecord(
      id = businessId,
      matchKey = "COMPANYNAME",
      sourceRecords = sourceRecords
    )
  }

  private def getSampleDataRecord(dataSource: String = "CompaniesHouse",
                          companyName: String = "COMPANY NAME",
                          industryCode: Option[Int] = Some(90000),
                          tradingStatus: Option[String] = Some("INSOLVENT"),
                          legalStatus: Option[String] = Some("1"),
                          turnover: Option[Int] = Some(100000),
                          totalEmployees: Option[Int] = Some(1000),
                          postcode: Option[String] = Some("BT45 4TY")) = {

    DataRecord(
      dataSource = dataSource,
      recordDigest = "dc3bebea9bf2cce",
      companyName = companyName,
      address = Some("74, STONE LANE"),
      postcode = postcode,
      turnover = turnover,
      industryCode = industryCode,
      legalStatus = legalStatus,
      tradingStatus = tradingStatus,
      totalEmployees = totalEmployees,
      additionalData = Map(
      )
    )
  }
}
