package test.java.test.qualidexJarTest;

import main.java.com.qualitestgroup.dataextract.library.QualidexLibrary;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PDFTest{

	private String basePDF= "src\\test\\resources\\Pdf1.pdf";
	public String refPDF= "src\\test\\resources\\Pdf2.pdf";
	public List<String> deletedStrings = Arrays.asList("Congratulations","Yukon Department of Education Box 2703 Whitehorse");
	public List<String> newStrings = Arrays.asList("folks",". Rest Limited,Double Box");
	public Map<String, String> diff =new HashMap<>();
	private final QualidexLibrary qualidexLibrary = new QualidexLibrary();

	@Test
	public void test() {
//		diff.put("Herr","Frau");
//		diff.put("Congratulations","folks");
//		diff.put("Yukon Department of Education Box 2703 Whitehorse",". Rest Limited,Double Box");

		//this will work if we give inputs for deleteString and newStrings and doesn't bother about diff values
//		qualidexLibrary.qualiDexCompares(basePDF, refPDF,diff,deletedStrings, newStrings);
//		qualidexLibrary.qualiDexCompares(basePDF, refPDF,deletedStrings, newStrings);

//		both pdf should be same or added any new strings(diff, newStrings parameters are not necessary pass as diff match patch lib
//		would differentiate the operation on the pdf)
//		qualidexLibrary.qualiDexCompares(basePDF, refPDF);

//		//the below 2 methods will work as expected if refPdf has added new lines of content and even if we don't pass the values for diff and newString list
//		qualidexLibrary.qualiDexCompares(basePDF,refPDF,diff);
		qualidexLibrary.qualiDexCompares(basePDF,refPDF,newStrings);
	}
}
