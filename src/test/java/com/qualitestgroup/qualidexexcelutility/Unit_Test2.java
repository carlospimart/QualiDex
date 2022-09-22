package com.qualitestgroup.qualidexexcelutility;

import java.io.IOException;
import java.util.List;

import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.qualitestgroup.data_extract_demo.damoregroup.Asserter;
import com.qualitestgroup.dataextract.library.QualidexLibrary;




public class Unit_Test2 {

	static String Pdf = "C:\\PDF\\englishgrammarbook.pdf";
	static String ExcelPath = "C:\\PDF\\TestExcel.xlsx";
	static String PDF2 = "C:\\PDF\\German.pdf";
	private static String PDF = "C:\\PDF\\FoodhabitsofUttarakhandv1.pdf";
	private static String refImage = "C:\\PDF\\image03.png";
	private static String value = "Versicherung";
	private static String formatText = "Angebot" ;
	private static String formatTextFontstyle = "LiberationSansNarrow-Bold";
	private static String formatTextFontSize= "14" ;
	private static String sheet = "Sheet3";
	private static int cellNumber = 1;
	private static String filterCellValue = "Test2";
	private static int filterColumnNumber = 0;
	private static int filterCellNumber = 1;
	

	@BeforeClass
	public void initialConfig() {
		// logger.info("Check is PDF consits of text")
		try {
			Reporter.log("Check is PDF consits of text");
			if (QualidexLibrary.isTextPresentInPdf(PDF2)) {
				Reporter.log("Pdf consits of text");
			} else {
				Reporter.log("Pdf doesn't consits of text");
			}

			Reporter.log("Check number of pages in PDF");
			QualidexLibrary.returnNumberOfPages(PDF2);

			Reporter.log("Set PDF and extract content");
			QualidexLibrary.setPDFLocationAndExtract(PDF2);
			Reporter.log("PDF contentent extracted successfully");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public static void qualidexLibraryTest1() {

		try {
			Reporter.log("Set Excel location");
			QualidexLibrary.setExcelLocation(ExcelPath);
			Reporter.log("Excel location has been set and start finding excel data in PDF");

			// List<>
			List<String> validationValues = QualidexLibrary.readCellValues(ExcelPath, sheet, cellNumber);
			for (String values : validationValues) {
				Reporter.log("Validation Text : " + values);
				if (QualidexLibrary.findValuesInPdf(values)) {
					Asserter.validateTrue(QualidexLibrary.findValuesInPdf(values), values + " is present in the PDF");
					Reporter.log(values + " is present in the PDF");
				} else {
					Asserter.validateTrue(QualidexLibrary.findValuesInPdf(values),values + " is not present in the PDF");
					Reporter.log(values + " is not present in the PDF");
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Asserter.validateAssert.assertAll();

	}

	@Test
	public static void qualidexLibraryTest2() {
		try {
			Reporter.log("Set Excel location");
			QualidexLibrary.setExcelLocation(ExcelPath);
			Reporter.log("Excel location has been set and start finding excel data in PDF");


			List<String> validationValuess = QualidexLibrary.applyFilterAndStoreCellValues(ExcelPath, sheet, filterCellValue , filterColumnNumber, filterCellNumber);
			for (String values : validationValuess) {
				Reporter.log("Validation Text : " + values);
				
				if (QualidexLibrary.findValuesInPdf(values)) {
					
					Asserter.validateTrue(QualidexLibrary.findValuesInPdf(values), values + " is present in the PDF");
					Reporter.log(values + " is present in the PDF");
					
				} else {
					
					Asserter.validateTrue(QualidexLibrary.findValuesInPdf(values),values + " is not present in the PDF");
					Reporter.log(values + " is not present in the PDF");
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Asserter.validateAssert.assertAll();
	}

	

	/*
	 * @Test public static void qualidexLibraryTest2() { try {
	 * Reporter.log("Set Header coordinates"); //
	 * QualidexLibrary.setHeaderCoords("117,");
	 * QualidexLibrary.setHeaderCoords("847.453,");
	 * Reporter.log("Header coordinates set successfully");
	 * Reporter.log("Check is header empty?"); if (QualidexLibrary.isEmptyHeader())
	 * { Reporter.log("Header is not empty"); } else {
	 * Reporter.log("Header is empty"); }
	 * Reporter.log("validate content in Header"); //
	 * if(QualidexLibrary.existsInHeader("Head")) if
	 * (QualidexLibrary.existsInHeader("Versicherung 56-5013692")) { //
	 * Reporter.log("Head exists in Header");
	 * Reporter.log("Versicherung 56-5013692 exists in Header"); } else { //
	 * Reporter.log("Head doesnot exists in Header");
	 * Reporter.log("Versicherung 56-5013692 doesnot exists in Header"); }
	 */

			/*
			 * Reporter.log("Set footer coordinates");
			 * QualidexLibrary.setFooterCoords("90, 108.45,");
			 * Reporter.log("footer coordinates set successfully");
			 * 
			 * Reporter.log("Check is footer empty?"); if(QualidexLibrary.isEmptyFooter()) {
			 * Reporter.log("Footer is not empty"); } else {
			 * Reporter.log("Footer is empty"); }
			 * 
			 * Reporter.log("validate content in footer");
			 * if(QualidexLibrary.existsInFooter("Foot")) {
			 * Reporter.log("Foot exists in footer"); } else {
			 * Reporter.log("Footer doesnot is exists in footer"); }
			 
		} catch (Exception e) {
			e.printStackTrace();
		}
		Asserter.validateAssert.assertAll();

	}*/

	@Test
	public static void qualidexLibraryTest3() {
		try {
			
			// Find the occurrence of given text
			Reporter.log(" Find the occurence of text : " + value);
			if (QualidexLibrary.findOccurence(value)>0) {
				
				Asserter.validateTrue((QualidexLibrary.findOccurence(value)>0),  value + " text found " + QualidexLibrary.findOccurence(value)+ " times in a PDF");
				Reporter.log(value + " text found "+ QualidexLibrary.findOccurence(value)+ " times in a PDF");
			}
			else {
				
				Asserter.validateTrue((QualidexLibrary.findOccurence(value)>0),  value + " is not present in a PDF ");
				Reporter.log(value + " is not present in a PDF");
			}
			
			
			//validate whether text is in format or not
			Reporter.log(" Find the text Angebot is in proper format");
			boolean result = QualidexLibrary.findWithFormat(formatText, formatTextFontstyle , formatTextFontSize);
			
			if (result) {
				
				Asserter.validateTrue(result, formatText + " text is in format");
				Reporter.log(formatText + " text is in format");
			}
			
			else {
				Asserter.validateTrue(result, formatText+ " text is not in format");
				
				Reporter.log(formatText+" text is not in format");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		Asserter.validateAssert.assertAll();
	}

	@Test
	public static void qualidexImageTest() {
		// QualidexLibrary.findText("Header");
		Reporter.log("Finding image in PDF");
		
		if (QualidexLibrary.findImage(PDF, refImage) == true) {
			
			Asserter.validateTrue(QualidexLibrary.findImage(PDF, refImage),"Image found in the PDF");
			Reporter.log("Image found in the PDF");
			
		} else {
			
			Asserter.validateTrue(QualidexLibrary.findImage(PDF, refImage),"Image not found in the PDF");
			Reporter.log("Image not found in the PDF");
		}
		Asserter.validateAssert.assertAll();
	}

}
