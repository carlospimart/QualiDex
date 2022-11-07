package test.java.com.qualitestgroup.qualidexexcelutility;

import main.java.com.qualitestgroup.data_extract_demo.damoregroup.Asserter;
import main.java.com.qualitestgroup.dataextract.library.QualidexLibrary;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;


public class Unit_Test {
//    private final String Pdf = "src\\test\\resources\\englishgrammarbook.pdf";
//    private final String excelPath = "src\\test\\resources\\TestExcel.xlsx";
//    private final String pdfPath = "src\\test\\resources\\German.pdf";
    private final String pathOfPDFWithImages = "src\\test\\resources\\FoodhabitsofUttarakhandv1.pdf";
//    private final String refImagePath = "src\\test\\resources\\image03.png";
//    private final String refImagePath1 = "src\\test\\resources\\image01Fail.png";
//    private final String refImagePath2 = "src\\test\\resources\\image02.png";
    private final String refImageDir ="src\\test\\resources\\expectedimages";
//    private final String value = "Versicherung";
//    private final String formatText = "Angebot";
//    private final String formatTextFontstyle = "LiberationSansNarrow-Bold";
//    private final String formatTextFontSize = "14";
//    private final String sheetName = "Sheet1";
//    private final String sheet = "Sheet1";
//    //private  int cellNumber = 1;
//    private final String filterCellValue = "Test2";
//    private final int filterColumnNumber = 0;
//    private final int filterCellNumber = 1;
//    private final String columnWanted = "Test content";
    private final Asserter asserter = new Asserter();
    private final QualidexLibrary qualidexLibrary = new QualidexLibrary();

//    @BeforeClass
//    public void initialConfig() {
//        // logger.info("Check is PDF consits of text")
//        try {
//            Reporter.log("Check is PDF consits of text");
//            if (qualidexLibrary.isPdfEmpty(pdfPath)) {
//                Reporter.log("Pdf consits of text");
//            } else {
//                Reporter.log("Pdf doesn't consits of text");
//            }
//            Reporter.log("Check number of pages in PDF");
//            qualidexLibrary.returnNumberOfPages(pdfPath);
//            Reporter.log("Set PDF and extract content");
//            qualidexLibrary.setPDFLocationAndExtract(pdfPath);
//            Reporter.log("PDF contentent extracted successfully");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    @Test
//    public void qualidexLibraryFindValuesInPDFTest() {
//        try {
//            Reporter.log("Excel location has been set to " + excelPath + " and start finding excel data in PDF");
//            // List<>
//            List<String> validationValues = qualidexLibrary.readCellValues(excelPath, sheetName, columnWanted);
//            for (int i = 1; i < validationValues.size(); i++) {
//                Reporter.log(("Validation text : " + validationValues.get(i)));
//                asserter.validateTrue(qualidexLibrary.findValuesInPdf(validationValues.get(i)), validationValues.get(i) + " is not present in the PDF");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        asserter.assertAll();
//
//    }
//
//    @Test
//    public void qualidexLibraryTest2() {
//        try {
//            Reporter.log("Set Excel location");
////			qualidexLibrary.setExcelLocation(ExcelPath);
//            Reporter.log("Excel location has been set and start finding excel data in PDF");
//            List<String> validationValues = qualidexLibrary.applyFilterAndStoreCellValues(excelPath, sheet, filterCellValue, filterColumnNumber, filterCellNumber);
//            for (String values : validationValues) {
//                Reporter.log("Validation Text : " + values);
//                if (qualidexLibrary.findValuesInPdf(values)) {
//                    asserter.validateTrue(qualidexLibrary.findValuesInPdf(values), values + " is present in the PDF");
//                    Reporter.log(values + " is present in the PDF");
//                } else {
//                    asserter.validateTrue(qualidexLibrary.findValuesInPdf(values), values + " is not present in the PDF");
//                    Reporter.log(values + " is not present in the PDF");
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        asserter.assertAll();
//    }
//
//
//
//    /*
//     * @Test public static void qualidexLibraryTest2() { try {
//     * Reporter.log("Set Header coordinates"); //
//     * qualidexLibrary.setHeaderCoords("117,");
//     * qualidexLibrary.setHeaderCoords("847.453,");
//     * Reporter.log("Header coordinates set successfully");
//     * Reporter.log("Check is header empty?"); if (qualidexLibrary.isEmptyHeader())
//     * { Reporter.log("Header is not empty"); } else {
//     * Reporter.log("Header is empty"); }
//     * Reporter.log("validate content in Header"); //
//     * if(qualidexLibrary.existsInHeader("Head")) if
//     * (qualidexLibrary.existsInHeader("Versicherung 56-5013692")) { //
//     * Reporter.log("Head exists in Header");
//     * Reporter.log("Versicherung 56-5013692 exists in Header"); } else { //
//     * Reporter.log("Head doesnot exists in Header");
//     * Reporter.log("Versicherung 56-5013692 doesnot exists in Header"); }
//     */
//
//			/*
//			 * Reporter.log("Set footer coordinates");
//			 * qualidexLibrary.setFooterCoords("90, 108.45,");
//			 * Reporter.log("footer coordinates set successfully");
//			 *
//			 * Reporter.log("Check is footer empty?"); if(qualidexLibrary.isEmptyFooter()) {
//			 * Reporter.log("Footer is not empty"); } else {
//			 * Reporter.log("Footer is empty"); }
//			 *
//			 * Reporter.log("validate content in footer");
//			 * if(qualidexLibrary.existsInFooter("Foot")) {
//			 * Reporter.log("Foot exists in footer"); } else {
//			 * Reporter.log("Footer doesnot is exists in footer"); }
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		Asserter.validateAssert.assertAll();
//
//	}*/
//
//    @Test
//    public void qualifiedLibraryOccurrenceTest() {
//        try {
//            // Find the occurrence of given text
//            Reporter.log(" Find the occurence of text : " + value);
//            if (qualidexLibrary.findOccurrence(value) > 0) {
//                asserter.validateTrue((qualidexLibrary.findOccurrence(value) > 0), value + " text found " + qualidexLibrary.findOccurrence(value) + " times in a PDF");
//                Reporter.log(value + " text found " + qualidexLibrary.findOccurrence(value) + " times in a PDF");
//            } else {
//                asserter.validateTrue((qualidexLibrary.findOccurrence(value) > 0), value + " is not present in a PDF ");
//                Reporter.log(value + " is not present in a PDF");
//            }
//            //validate whether text is in format or not
//            Reporter.log(" Find the text Angebot is in proper format");
//            boolean result = qualidexLibrary.findWithFormat(formatText, formatTextFontstyle, formatTextFontSize);
//            if (result) {
//                asserter.validateTrue(result, formatText + " text is in format");
//                Reporter.log(formatText + " text is in format");
//            } else {
//                asserter.validateTrue(result, formatText + " text is not in format");
//                Reporter.log(formatText + " text is not in format");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        asserter.assertAll();
//    }

    @Test
    public void qualidexImageTest() {
        Reporter.log("Finding image in PDF");
        asserter.validateTrue(qualidexLibrary.findImage(pathOfPDFWithImages, refImageDir), "Image not found in the PDF");
      asserter.assertAll();
    }

}
