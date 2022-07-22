package com.qualitestgroup.qualidexexcelutility;

import java.io.IOException;

import org.testng.Reporter;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.qualitestgroup.data_extract_demo.damoregroup.Asserter;
import com.qualitestgroup.dataextract.library.QualidexLibrary;




public class Unit_Test {

    static String Pdf = "C:\\PDF\\englishgrammarbook.pdf";
	static String ExcelPath = "C:\\PDF\\TestExcel.xlsx";
	//static String PDF2= "C:\\PDF\\PavanTest1.pdf";
	static String PDF2="C:\\PDF\\SamplePdf.pdf";
	private static String PDF = "C:/Users/pavan.kumar/Downloads/FoodhabitsofUttarakhandv1.pdf";
    private static String refImage= "C:\\PDF\\image03.png";
    //private static String refImage="C:\\Users\\pavan.kumar\\Downloads\\image-20211111-141726.png";
	
	
    @BeforeTest
	public void initialConfig() {
		//logger.info("Check is PDF consits of text")
		try {
		Reporter.log("Check is PDF consits of text");
		if(QualidexLibrary.isTextPresentInPdf(PDF2)) {
			Reporter.log("Pdf consits of text");
		}
		else {
			Reporter.log("Pdf doesn't consits of text");
		}
		
		Reporter.log("Check number of pages in PDF");
		QualidexLibrary.returnNumberOfPages(PDF2);
		
		Reporter.log("Set PDF and extract content");
		QualidexLibrary.setPDFLocationAndExtract(PDF2);
		Reporter.log("PDF contentent extracted successfully");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public static void qualidexLibraryTest1() {
		try {
		Reporter.log("Set Excel location");
		QualidexLibrary.setExcelLocation(ExcelPath);
		Reporter.log("Excel location has been set and start finding excel data in PDF");
		//Reporter.log("Started comparing cell value with PDF");
		QualidexLibrary.storeCellValueAndCompare(ExcelPath, "Sheet3", 1) ;
	
		
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		Asserter.validateAssert.assertAll();
	}
	
	@Test
	public static void qualidexLibraryTest2() {
		try {
		Reporter.log("Set Header coordinates");
		QualidexLibrary.setHeaderCoords("117,");
		Reporter.log("Header coordinates set successfully");
		Reporter.log("Check is header empty?");
		if(QualidexLibrary.isEmptyHeader()) {
			Reporter.log("Header is not empty");
		}
		else {
			Reporter.log("Header is empty");
		}
		Reporter.log("validate content in Header");
		if(QualidexLibrary.existsInHeader("Head")) {
			Reporter.log("Head exists in Header");
		}
		else {
			Reporter.log("Head doesnot exists in Header"); 
		}
		
		Reporter.log("Set footer coordinates");
		QualidexLibrary.setFooterCoords("90, 108.45,");
		Reporter.log("footer coordinates set successfully");
		
		Reporter.log("Check is footer empty?");
		if(QualidexLibrary.isEmptyFooter()) {
			Reporter.log("Footer is not empty");
		}
		else {
			Reporter.log("Footer is empty");
		}
		
		Reporter.log("validate content in footer");
		if(QualidexLibrary.existsInFooter("Foot")) {
			Reporter.log("Foot exists in footer"); 
		}
		else {
			Reporter.log("Footer doesnot is exists in footer");
		}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	  Asserter.validateAssert.assertAll();
		
	}
	
	@Test
	public static void qualidexLibraryTest3() {
		try {
			Reporter.log("Set Excel location");
			QualidexLibrary.setExcelLocation(ExcelPath);
			Reporter.log("Excel location has been set and start finding excel data in PDF");
			
			QualidexLibrary.applyFilterAndCompare(ExcelPath, "Sheet3", "Test", 1, 0);
			
		}
		catch (Exception e){
			e.printStackTrace();
		}
		Asserter.validateAssert.assertAll();
	}
	
	@Test
	public static void qualidexLibraryTest4() {
		try {
			//Reporter.log("Find the occurence of Header in PDF");	
			QualidexLibrary.findOccurence("Header");
			QualidexLibrary.findWithFormat("Head", "ZLXSHE+Calibri", "9");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		Asserter.validateAssert.assertAll();
	}
	@Test
	public static void qualidexImageTest() {
		QualidexLibrary.findText("Header");
		Reporter.log("Finding image in PDF");
		QualidexLibrary.findImage(PDF,refImage);
		Asserter.validateAssert.assertAll();
	}
	
	
}
	

