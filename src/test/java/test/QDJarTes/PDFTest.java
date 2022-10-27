package test.java.test.QDJarTes;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import main.java.com.qualitestgroup.dataextract.library.QualidexLibrary;

//import test.Check_Java_Functionalities.PDFDiff;

public class PDFTest {

	public static String basePDF= "src\\test\\resources\\basepdf.pdf";
	public static String refPDF= "src\\test\\resources\\basepdf.pdf";
	public static List<String> deletedStrings = Arrays.asList("");
	public static List<String> newStrings = Arrays.asList("Zusatzvereinbarung/en:", "ZV 83 - Erwerbsunfähigkeitsklausel");
	public static Map<String, String> diff =new HashMap<>();
	
	
	@Test
	public static void test() {
		
		diff.put("Herr","Frau");
		//diff.put("Herr","Frau" );
		diff.put("Blixa Bargeld", "Annette Meyer");
		//diff.put("78,97 7.578,97", "23,14 8.072,62 0,00 8.665,26 9.214,74");
		//diff.put("Regressionsteststraße" ,"Döppersberg 37");
										
		//diff.put("50669 Köln", "42103 Wuppertal");
		//diff.put("Regressionsteststraße\n"+ "50669 Köln", "Döppersberg 37\n" + "42103 Wuppertal ");
		
		
		//PDFDiff.qualiDexCompare("C:\\PDF\\Geman1.pdf", "C:\\PDF\\Geman2.pdf");
		QualidexLibrary.qualiDexCompares(basePDF, refPDF, diff, deletedStrings, newStrings);
		//PDFDiff.qualiDexCompare(basePDF, refPDF);
		
	}

}
