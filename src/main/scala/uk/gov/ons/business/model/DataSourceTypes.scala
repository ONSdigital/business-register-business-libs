package uk.gov.ons.business.model

object DataSourceTypes {
  val businessIndex = "BusinessIndex"
  val charityCommission = "CharityCommission"
  val companiesHouse = "CompaniesHouse"
  val vat = "Vat"
  val paye = "Paye"

  val all = List(companiesHouse, charityCommission, vat, paye)
}
