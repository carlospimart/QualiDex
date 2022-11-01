package test.java.test.qualidexJarTest;

import main.java.com.qualitestgroup.data_extract_demo.damoregroup.Asserter;
import main.java.com.qualitestgroup.dataextract.library.QualidexLibrary;
import main.java.com.qualitestgroup.dataextract.utilities.TestContext;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PDFTest{

	private String basePDF= "src\\test\\resources\\basepdf.pdf";
	public String refPDF= "src\\test\\resources\\referencepdf.pdf";
	public List<String> deletedStrings = Arrays.asList("");
	public List<String> newStrings = Arrays.asList("[1]");
	public Map<String, String> diff =new HashMap<>();
	private final QualidexLibrary qualidexLibrary = new QualidexLibrary();

	@Test
	public void test() {
//		diff.put("Herr","Frau");
		diff.put("","[1]");
		qualidexLibrary.qualiDexCompares(basePDF, refPDF, diff, deletedStrings, newStrings);
	}
}
