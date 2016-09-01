package uk.gov.ons.business.csv

import uk.gov.ons.business.csv.{DataRecordField => Field}
import uk.gov.ons.business.model.DataSourceTypes._
import uk.gov.ons.business.model.{BusinessIndexRecord, DataRecord}

class BusinessFieldsRetriever(fieldRetriever: FieldRetriever) extends Serializable {

  @transient
  private lazy val businessFields = List(
    Field.CompanyName,
    Field.Postcode,
    Field.IndustryCode,
    Field.LegalStatus,
    Field.TradingStatus,
    Field.Turnover,
    Field.TotalEmployees
  )

  @transient
  private lazy val sources = Map(
    Field.CompanyName -> List(companiesHouse, vat, charityCommission, paye),
    Field.Postcode -> List(companiesHouse, charityCommission, vat, paye),
    Field.IndustryCode -> List(vat, companiesHouse, paye),
    Field.LegalStatus -> List(companiesHouse, vat, paye),
    Field.TradingStatus -> List(companiesHouse, vat, paye),
    Field.Turnover -> List(vat),
    Field.TotalEmployees -> List(paye)
  )

  def getEmptyFields: List[String] = {
    List.fill(businessFields.length)("")
  }

  def getFieldsForBusinessIndex(businessIndexRecord: BusinessIndexRecord): List[String] = {
    val getField = getBusinessField(businessIndexRecord) _

    businessFields.map(getField)
  }

  private def getBusinessField(businessIndexRecord: BusinessIndexRecord)(dataRecordField: Field.Value) = {
    fieldRetriever.getField(businessIndexRecord, dataRecordField, sources(dataRecordField))
  }

  def getFieldsForDataRecord(dataRecord: DataRecord): List[String] = {
    val getField = getBusinessField(dataRecord) _

    businessFields.map(getField)
  }

  private def getBusinessField(dataRecord: DataRecord)(dataRecordField: Field.Value) = {
    fieldRetriever.getField(dataRecord, dataRecordField)
  }

}

class FieldRetriever extends Serializable {
  def getField(businessIndexRecord: BusinessIndexRecord, field: DataRecordField.Value, sourcesByPriority: List[String]) = {
    val fieldInSources = sourcesByPriority.iterator.flatMap { source =>
      businessIndexRecord.sourceRecords.get(source).flatMap(record => getFieldValue(record, field))
    }.toIterable

    fieldInSources.headOption.getOrElse("")
  }

  def getField(dataRecord: DataRecord, field: DataRecordField.Value) = getFieldValue(dataRecord, field).getOrElse("")

  private def getFieldValue(dataRecord: DataRecord, field: DataRecordField.Value): Option[String] = {
    field match {
      case Field.CompanyName => Some(dataRecord.companyName)
      case Field.Postcode => dataRecord.postcode
      case Field.IndustryCode => dataRecord.industryCode.map(_.toString)
      case Field.LegalStatus => dataRecord.legalStatus
      case Field.TradingStatus => dataRecord.tradingStatus
      case Field.Turnover => dataRecord.turnover.map(_.toString)
      case Field.TotalEmployees => dataRecord.totalEmployees.map(_.toString)
    }
  }
}

object DataRecordField extends Enumeration {
  type DataRecordField = Value
  val CompanyName, Postcode, IndustryCode, LegalStatus, TradingStatus, Turnover, TotalEmployees = Value
}

