package test.java.com.qualitestgroup.qualidexexcelutility;

import main.java.com.qualitestgroup.data_extract_demo.damoregroup.Asserter;
import main.java.com.qualitestgroup.dataextract.library.QualidexLibrary;
import main.java.com.qualitestgroup.dataextract.utilities.TestContext;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

public class Unit_Test2 {
    static String Pdf = "C:\\PDF\\englishgrammarbook.pdf";
    static String ExcelPath = "C:\\PDF\\TestExcel.xlsx";
    static String PDF2 = "C:\\PDF\\German.pdf";
    private static String PDF = "C:\\PDF\\FoodhabitsofUttarakhandv1.pdf";
    private static String refImage = "C:\\PDF\\image03.png";
    private static String value = "Versicherung";
    private static String formatText = "Angebot";
    private static String formatTextFontstyle = "LiberationSansNarrow-Bold";
    private static String formatTextFontSize = "14";
    private static String sheet = "Sheet3";
    private static int cellNumber = 1;
    private static String filterCellValue = "Test2";
    private static int filterColumnNumber = 0;
    private static int filterCellNumber = 1;
    private final QualidexLibrary qualidexLibrary = new QualidexLibrary();
    private final Asserter asserter = new Asserter();

    @BeforeClass
    public void initialConfig() {
        // logger.info("Check is PDF consits of text")
        try {
            Reporter.log("Check is PDF consits of text");
            if (qualidexLibrary.isPdfEmpty(PDF2)) {
                Reporter.log("Pdf consits of text");
            } else {
                Reporter.log("Pdf doesn't consits of text");
            }

            Reporter.log("Check number of pages in PDF");
            qualidexLibrary.returnNumberOfPages(PDF2);

            Reporter.log("Set PDF and extract content");
            qualidexLibrary.setPDFLocationAndExtract(PDF2);
            Reporter.log("PDF contentent extracted successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void qualidexLibraryTest1() {

        try {
            Reporter.log("Set Excel location");
//			hooks.qualidexLibrary.setExcelLocation(ExcelPath);
            Reporter.log("Excel location has been set and start finding excel data in PDF");

            // List<>
            List<String> validationValues = qualidexLibrary.readCellValues(ExcelPath, sheet, cellNumber);
            for (String values : validationValues) {
                Reporter.log("Validation Text : " + values);
                if (qualidexLibrary.findValuesInPdf(values)) {
                    asserter.validateTrue(qualidexLibrary.findValuesInPdf(values), values + " is present in the PDF");
                    Reporter.log(values + " is present in the PDF");
                } else {
                    asserter.validateTrue(qualidexLibrary.findValuesInPdf(values), values + " is not present in the PDF");
                    Reporter.log(values + " is not present in the PDF");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        asserter.validateAssert.assertAll();

    }

    @Test
    public void qualidexLibraryTest2() {
        try {
            Reporter.log("Set Excel location");
//			hooks.qualidexLibrary.setExcelLocation(ExcelPath);
            Reporter.log("Excel location has been set and start finding excel data in PDF");
            List<String> validationValuess = qualidexLibrary.applyFilterAndStoreCellValues(ExcelPath, sheet, filterCellValue, filterColumnNumber, filterCellNumber);
            for (String values : validationValuess) {
                Reporter.log("Validation Text : " + values);

                if (qualidexLibrary.findValuesInPdf(values)) {

                    asserter.validateTrue(qualidexLibrary.findValuesInPdf(values), values + " is present in the PDF");
                    Reporter.log(values + " is present in the PDF");

                } else {

                    asserter.validateTrue(qualidexLibrary.findValuesInPdf(values), values + " is not present in the PDF");
                    Reporter.log(values + " is not present in the PDF");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        asserter.validateAssert.assertAll();
    }



    /*
     * @Test public static void hooks.qualidexLibraryTest2() { try {
     * Reporter.log("Set Header coordinates"); //
     * hooks.qualidexLibrary.setHeaderCoords("117,");
     * hooks.qualidexLibrary.setHeaderCoords("847.453,");
     * Reporter.log("Header coordinates set successfully");
     * Reporter.log("Check is header empty?"); if (hooks.qualidexLibrary.isEmptyHeader())
     * { Reporter.log("Header is not empty"); } else {
     * Reporter.log("Header is empty"); }
     * Reporter.log("validate content in Header"); //
     * if(hooks.qualidexLibrary.existsInHeader("Head")) if
     * (hooks.qualidexLibrary.existsInHeader("Versicherung 56-5013692")) { //
     * Reporter.log("Head exists in Header");
     * Reporter.log("Versicherung 56-5013692 exists in Header"); } else { //
     * Reporter.log("Head doesnot exists in Header");
     * Reporter.log("Versicherung 56-5013692 doesnot exists in Header"); }
     */

			/*
			 * Reporter.log("Set footer coordinates");
			 * hooks.qualidexLibrary.setFooterCoords("90, 108.45,");
			 * Reporter.log("footer coordinates set successfully");
			 *
			 * Reporter.log("Check is footer empty?"); if(hooks.qualidexLibrary.isEmptyFooter()) {
			 * Reporter.log("Footer is not empty"); } else {
			 * Reporter.log("Footer is empty"); }
			 *
			 * Reporter.log("validate content in footer");
			 * if(hooks.qualidexLibrary.existsInFooter("Foot")) {
			 * Reporter.log("Foot exists in footer"); } else {
			 * Reporter.log("Footer doesnot is exists in footer"); }

		} catch (Exception e) {
			e.printStackTrace();
		}
		hooks.asserter.validateAssert.assertAll();

	}*/

    @Test
    public void qualidexLibraryTest3() {
        try {

            // Find the occurrence of given text
            Reporter.log(" Find the occurence of text : " + value);
            if (qualidexLibrary.findOccurence(value) > 0) {

                asserter.validateTrue((qualidexLibrary.findOccurence(value) > 0), value + " text found " + qualidexLibrary.findOccurence(value) + " times in a PDF");
                Reporter.log(value + " text found " + qualidexLibrary.findOccurence(value) + " times in a PDF");
            } else {

                asserter.validateTrue((qualidexLibrary.findOccurence(value) > 0), value + " is not present in a PDF ");
                Reporter.log(value + " is not present in a PDF");
            }


            //validate whether text is in format or not
            Reporter.log(" Find the text Angebot is in proper format");
            boolean result = qualidexLibrary.findWithFormat(formatText, formatTextFontstyle, formatTextFontSize);

            if (result) {

                asserter.validateTrue(result, formatText + " text is in format");
                Reporter.log(formatText + " text is in format");
            } else {
                asserter.validateTrue(result, formatText + " text is not in format");

                Reporter.log(formatText + " text is not in format");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        asserter.validateAssert.assertAll();
    }

    @Test
    public void qualidexImageTest() {
        // hooks.qualidexLibrary.findText("Header");
        Reporter.log("Finding image in PDF");

        if (qualidexLibrary.findImage(PDF, refImage) == true) {

            asserter.validateTrue(qualidexLibrary.findImage(PDF, refImage), "Image found in the PDF");
            Reporter.log("Image found in the PDF");

        } else {

            asserter.validateTrue(qualidexLibrary.findImage(PDF, refImage), "Image not found in the PDF");
            Reporter.log("Image not found in the PDF");
        }
        asserter.validateAssert.assertAll();
    }

}
