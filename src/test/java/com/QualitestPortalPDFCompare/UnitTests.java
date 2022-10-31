package test.java.com.QualitestPortalPDFCompare;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.util.SystemOutLogger;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.Reporter;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import org.xml.sax.SAXException;

import main.java.com.qualitestgroup.data_extract_demo.damoregroup.Asserter;
import main.java.com.qualitestgroup.data_extract_demo.damoregroup.PDF2XMLComparator;
import main.java.com.qualitestgroup.dataextract.imagecomp.BaseImageExtraction;
import main.java.com.qualitestgroup.dataextract.pdf.PDFOpener;
import main.java.com.qualitestgroup.dataextract.pdf.extraction.PDFText2XML;
import main.java.com.qualitestgroup.dataextract.utilities.ComparisonMode;
import main.java.com.qualitestgroup.dataextract.utilities.QualidexUtility;



public class UnitTests {

	private static final Log LOG = LogFactory.getLog(UnitTests.class);
	private final static Logger logger = Logger.getLogger(PDF2XMLComparator.class.getName());

	private PDFOpener opener;
	private PDFText2XML text2XML;
	public PDF2XMLComparator xmlComparator = new PDF2XMLComparator();
	public static String strEncodeXML = "encoding=\"UTF-8\" ";
	public static PDDocument left = null;
	public static PDDocument right = null;
	static String leftStripped = null;
	static String rightStripped = null;
	static SoftAssert softAssertion = new SoftAssert();

	public static String firstXmlDoc = getConfigProperty("firstXmlDocFile");
	public static String secondXmlDoc = getConfigProperty("secondXmlDocFile");
	private static DecimalFormat df2 = new DecimalFormat("#.##");

	@BeforeTest
	public void setup() throws IOException {
		opener = new PDFOpener();
		text2XML = new PDFText2XML();
	}

	/**
	 * @param strDoc1
	 * @param strDoc2 Method to load the PDF data content to a document
	 */
	public void loadPDFContentToDoc(String strDoc1, String strDoc2) {
		try {
			Reporter.log("Load PDF content from First file " + strDoc1, true);
			left = opener.openDocument(strDoc1);
			
			Reporter.log("Loaded PDF content from First file " + strDoc1 + " Successfully", true);
			logger.info("Number of pages of First PDF file is -> " + left.getNumberOfPages());
			Reporter.log("Load PDF content from Second file " + strDoc2, true);
			right = opener.openDocument(strDoc2);
			Reporter.log("Loaded PDF content from Second file " + strDoc2 + " Successfully", true);
			logger.info("Number of pages of Second PDF file is -> " + right.getNumberOfPages());
			
			left.close();
			right.close();
		} catch (Exception e) {
			logger.info("Exception caught in loadPDFContentToDoc()  " + e.getMessage());
			LOG.error(" Exception caught while loading PDF files data to document");
		}
	}

	/**
	 * Method to Fetch the data and convert it XML format
	 */
	public void fetchDataAndConvertToXML() {
		try {
			Reporter.log("Fetching data from PDF files", true);
			leftStripped = text2XML.getText(left);
			rightStripped = text2XML.getText(right);
			Reporter.log("Fetched data from PDF files", true);
			Reporter.log("Convert First and Second PDF files data into XML format successfully", true);
			PDF2XMLComparator.stringToXML(leftStripped.replace(strEncodeXML, ""), firstXmlDoc);
			PDF2XMLComparator.stringToXML(rightStripped.replace(strEncodeXML, ""), secondXmlDoc);
			Reporter.log("Converted First and Second PDF files data into XML format successfully", true);
		} catch (Exception e) {
			logger.info("Exception caught in fetchDataAndConvertToXML()  " + e.getMessage());
			LOG.error(" Exception caught while fetching data from document file and converting it to XML");
		}
	}

	/**
	 * @param keyword
	 * @return
	 * 
	 *         Method to get file path from properties file
	 */
	public static String getConfigProperty(String keyword) {
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream("./Config/TestConfiguration.properties"));
		} catch (FileNotFoundException e) {
			Reporter.log("File Not Found Exception thrown while getting value of " + keyword
					+ " from Test Configuration file");
		} catch (IOException e) {
			Reporter.log("IO Exception thrown while getting value of " + keyword + " from Test Configuration file");
		}
		logger.info(
				"Getting value of " + keyword + " from Test Configuration file : " + properties.getProperty(keyword));
		return properties.getProperty(keyword);
	}

	public void compareTwoFiles(String file1, String file2) throws IOException, SAXException {
		//Reading file path from configuration.property to get the entire path
		//loadPDFContentToDoc(getConfigProperty(file1).toString().trim(), getConfigProperty(file2).toString().trim());
		loadPDFContentToDoc(file1, file2);
		Reporter.log("Verify number of pages in PDF's", true);
		//Reporter.log("Verify number of pages, Pages in Base file : "+left.getNumberOfPages()+", Pages in reference file : "+right.getNumberOfPages(), true);
		Reporter.log("Verified number of pages, Pages in Base file : "+left.getNumberOfPages()+", Pages in reference file : "+right.getNumberOfPages(), true);
		
		//Asserter.validateTrue(left.getNumberOfPages() == right.getNumberOfPages(), "Pages count mismatches, First file "
			//	+ " pages are " + left.getNumberOfPages() + " and second files pages are " + right.getNumberOfPages());
		
		//Reporter.log("Verified!! Number of pages are same", true);

		Reporter.log("Compare PDF text content ", true);
		QualidexUtility util = new QualidexUtility();
		util.setCompareMode(ComparisonMode.TEXT_MODE);

		boolean result = util.compare(file1, file2);
		Asserter.validateTrue(result);

		Reporter.log("Compared PDF text content ", true);

		Reporter.log("Compare pdf files after converting into images ", true);
		//String referenceLink = "";
		util.setImageDestinationPath("Extract_Images\\");

		util.savePdfAsImage(file1);
		util.savePdfAsImage(file2);


		int file1PageCount = util.getPageCount(file1);
		for (int i = 1; i <= file1PageCount; i++) {

			String [] str1 = file1.split("\\\\");
			String file1Name = str1[str1.length-1];
			String[] file1updatedName = file1Name.split("\\.");
			
			String [] str2 = file2.split("\\\\");
			String file2Name = str2[str2.length-1];
			String[] file2updatedName = file2Name.split("\\.");

			double diffinPerecentage = BaseImageExtraction.imageCompare(
					"Extract_Images\\" + file1updatedName[0] + "_" + i + ".png",
					"Extract_Images\\" + file2updatedName[0] + "_" + i + ".png");

			if (diffinPerecentage!= 0.0) {
				if (diffinPerecentage >0.0) {
					BufferedImage img1,img2,joinedImg;
					img1 = ImageIO
							.read(new File("Extract_Images\\" + file1updatedName[0] + "_" + i + ".png"));
					img2 = ImageIO
							.read(new File("Extract_Images\\" + file2updatedName[0] + "_" + i + ".png"));
					
					//Base Image Compare
					joinedImg = BaseImageExtraction.compareWithBaseImage(img2, img1);
					
					String filePath = System.getProperty("user.dir") + "\\Extract_Images\\" + file1updatedName[0] + "_"
							+ file2updatedName[0] + i + "_ImageCompare.png";
					
					ImageIO.write(joinedImg, "png", new File(filePath));
					
					img2 = ImageIO
							.read(new File("Extract_Images\\" + file1updatedName[0] + "_"
									+ file2updatedName[0] + i + "_ImageCompare.png"));
					
					joinedImg = BaseImageExtraction.joinBufferedImage(img1, img2);

					filePath = System.getProperty("user.dir") + "\\Extract_Images\\" + file1updatedName[0] + "_"
							+ file2updatedName[0] + i + ".png";
					ImageIO.write(joinedImg, "png", new File(filePath));
					Asserter.validateTrue(diffinPerecentage == 0.0,
							"PDF data differs with " + df2.format(diffinPerecentage) + "%" + " & for reference "
									+ "<a href=" + filePath + ">link</a>");
					
					break;
				}else {
					BufferedImage img1 = ImageIO
							.read(new File("Extract_Images\\" + file1updatedName[0] + "_" + i + ".png"));
					BufferedImage img2 = ImageIO
							.read(new File("Extract_Images\\" + file2updatedName[0] + "_" + i + ".png"));

					BufferedImage joinedImg = BaseImageExtraction.compareWithBaseImage(img1, img2);

					String filePath = System.getProperty("user.dir") + "\\Extract_Images\\" + file1updatedName[0] + "_"
							+ file2updatedName[0] + i + ".png";
					ImageIO.write(joinedImg, "png", new File(filePath));
					Asserter.validateTrue(diffinPerecentage == 0.0,
							"PDF Image differs with " + df2.format(diffinPerecentage) + "%" + " & for reference "
									+ "<a href=" + filePath + ">link</a>");
					break;
				}
				
			}
			
		}
		
		Reporter.log("Compared extracted image with reference image successfully ", true);
		//Asserter.validateAssert.assertAll();
	
	}
	
	@Test
	public void TextChanges2File() throws Exception {
		compareTwoFiles("Path to Pdf file 1", "Path to Pdf file 2");
	}
	
	
	@SuppressWarnings({ "unused", "rawtypes", "resource" })
	@Test
	public void DataCompareTest() throws Exception	
	{  
		String filePath = getConfigProperty("ExcelFilePath");
		String executeCell=null;
		if(filePath!=null && filePath!="") {
		File file = new File(filePath);  
		FileInputStream fis = new FileInputStream(file);   //obtaining bytes from the file  
		//creating Workbook instance that refers to .xlsx file  
		XSSFWorkbook wb = new XSSFWorkbook(fis); 
		
		String sheetNo= getConfigProperty("ExcelSheetNo");
		
		if(sheetNo == null || sheetNo == "") sheetNo = "0";
		
		int sheetNumber = Integer.parseInt(sheetNo);
		XSSFSheet sheet = wb.getSheetAt(sheetNumber);  
		String fileName="";
		String cellValue="";
		Iterator rowIterator = sheet.rowIterator();
		for (Row row : sheet) {
			try {
				executeCell = row.getCell(1).getStringCellValue();
			}catch (Exception e) {
			}			
			if (executeCell.equals("Y")) {
				for (int i=2; i<row.getLastCellNum(); i++)
				{
					executeCell = row.getCell(i).getStringCellValue();
					String test = row.getCell(0).getStringCellValue();
					System.out.println(executeCell);
					compareTwoFiles(test, executeCell);
					System.out.println("-------- Run Completed--------");
					break;
				}
			}
		}
		Asserter.validateAssert.assertAll();
	}}

	private String getconfigproperty(String string) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
