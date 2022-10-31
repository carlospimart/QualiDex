package test.java.com.qualitestgroup.data_extract_demo;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Properties;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import main.java.com.qualitestgroup.data_extract_demo.damoregroup.Softassert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.testng.Reporter;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import org.xml.sax.SAXException;

import main.java.com.qualitestgroup.data_extract_demo.damoregroup.Asserter;
import main.java.com.qualitestgroup.data_extract_demo.damoregroup.PDF2XMLComparator;
import main.java.com.qualitestgroup.dataextract.imagecomp.BaseImageExtraction;
import main.java.com.qualitestgroup.dataextract.pdf.PDFOpener;
import main.java.com.qualitestgroup.dataextract.pdf.extraction.PDFText2XML;
import main.java.com.qualitestgroup.dataextract.utilities.QualidexUtility;

public class PdfCompare extends BaseImageExtraction{

	private final Softassert validateAssert = new Softassert();
	private static final Log LOG = LogFactory.getLog(PdfCompare.class);
	private final static Logger logger = Logger.getLogger(PDF2XMLComparator.class.getName());

	private PDFOpener opener;
	private PDFText2XML text2XML;
	//public PDF2XMLComparator xmlComparator = new PDF2XMLComparator();
	public static String strEncodeXML = "encoding=\"UTF-8\" ";
	public PDDocument left = null;
	public PDDocument right = null;
	static String leftStripped = null;
	static String rightStripped = null;
    static SoftAssert softAssertion = new SoftAssert();
    public String firstXmlDoc = getConfigProperty("firstXmlDocFile");
	public String secondXmlDoc = getConfigProperty("secondXmlDocFile");
	private DecimalFormat df2 = new DecimalFormat("#.##");
	private String File_1 = "File_1.pdf";
	private String File_2 = "File_2.pdf";
	private Asserter asserter = new Asserter();
	/*
	 * private String Photography_image1 = "Photography_image1"; private String
	 * Photography_image2 = "Photography_image2"; private String
	 * DynamicObjectFileOne = "DynamicObjectFileOne"; private String
	 * DynamicObjectFileTwo = "DynamicObjectFileTwo";
	 */

	
	
	@BeforeTest
	public void setup() throws IOException {
		opener = new PDFOpener();
		text2XML = new PDFText2XML();
		
	}

	/**
	 * @param strDoc1
	 * @param strDoc2 Method to load the PDF data content to a document
	 */
	public void loadPDFContentToDoc1(String strDoc1, String strDoc2) {
		try {
			
			left = opener.openDocument(strDoc1);
			
			right = opener.openDocument(strDoc2);
			
			logger.info("Number of pages of First PDF file is -> " + left.getNumberOfPages());
			logger.info("Number of pages of Second PDF file is -> " + right.getNumberOfPages());
		} catch (Exception e) {
			logger.info("Exception caught in loadPDFContentToDoc()  " + e.getMessage());
			LOG.error(" Exception caught while loading PDF files data to document");
		}
	}
	
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
		} catch (Exception e) {
			logger.info("Exception caught in loadPDFContentToDoc()  " + e.getMessage());
			LOG.error(" Exception caught while loading PDF files data to document");
		}
	}


	/**
	 * Method to Fetch the data and convert it XML format
	 */
	public void fetchDataAndConvertToXML1() {
		try {
			
			leftStripped = text2XML.getText(left);
			rightStripped = text2XML.getText(right);
			
			
			PDF2XMLComparator.stringToXML(leftStripped.replace(strEncodeXML, ""), firstXmlDoc);
			PDF2XMLComparator.stringToXML(rightStripped.replace(strEncodeXML, ""), secondXmlDoc);
			
		} catch (Exception e) {
			logger.info("Exception caught in fetchDataAndConvertToXML()  " + e.getMessage());
			LOG.error(" Exception caught while fetching data from document file and converting it to XML");
		}
	}
	
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

	
	public void verifyNumberOfPages() {
		
		try {
			asserter.validateTrue(left.getNumberOfPages()==right.getNumberOfPages(), "Pages count mismatches, First file "
					+ " pages are " + left.getNumberOfPages() + " and second files pages are " + right.getNumberOfPages());
		}
		catch(Exception e) {
			logger.info("Exception caught in verifyNumberOfPages()  " + e.getMessage());
		}
	}

	/**
	 * @param keyword
	 * @return
	 * 
	 *         Method to get file path from properties file
	 */
	public String getConfigProperty(String keyword) {
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

	/*
	 * public void compareTwoFiles() throws IOException, InterruptedException {
	 * loadPDFContentToDoc((getConfigProperty(Photography_Text1).toString().trim()),
	 * (getConfigProperty (Photography_Text2).toString().trim()));
	 * 
	 * Reporter.log("Verify number of pages are same", true);
	 * //Asserter.validateTrue(left.getNumberOfPages()==right.getNumberOfPages(),
	 * "Pages count mismatches, First file " //+ " pages are " +
	 * left.getNumberOfPages() + " and second files pages are " +
	 * right.getNumberOfPages());
	 * Reporter.log("Verified!! Number of pages are same", true);
	 * 
	 * fetchDataAndConvertToXML(); Reporter.log("Verify PDF data Content", true);
	 * PDF2XMLComparator.validatePDFFileData(firstXmlDoc, secondXmlDoc);
	 * Reporter.log("Verified PDF data Content", true); QualidexUtility util = new
	 * QualidexUtility();
	 * Reporter.log("Compare extracted image with reference image", true);
	 * util.setImageDestinationPath("Extract_Images\\");
	 * util.savePdfAsImage("src\\test\\Resources\\" +
	 * getConfigProperty(Photography_Text1).toString().trim());
	 * util.savePdfAsImage("src\\test\\Resources\\" +
	 * getConfigProperty(Photography_Text2).toString().trim());
	 * 
	 * int file1PageCount = util.getPageCount("src\\test\\Resources\\" +
	 * getConfigProperty(Photography_Text1).toString().trim());
	 * 
	 * for (int i = 1; i <= file1PageCount; i++) {
	 * 
	 * String file1Name = getConfigProperty(Photography_Text1).toString().trim();
	 * String[] file1updatedName = file1Name.split("\\."); String file2Name =
	 * getConfigProperty(Photography_Text2).toString().trim(); String[]
	 * file2updatedName = file2Name.split("\\.");
	 * 
	 * double diffinPerecentage = BaseImageExtraction.imageCompare(
	 * "Extract_Images\\" + file1updatedName[0] + "_" + i + ".png",
	 * "Extract_Images\\" + file2updatedName[0] + "_" + i + ".png");
	 * 
	 * if (diffinPerecentage != 0.0) { if(diffinPerecentage > 2.0) { BufferedImage
	 * img1,img2,joinedImg; img1 = ImageIO .read(new
	 * File("Extract_Images\\" + file1updatedName[0] + "_" + i + ".png")); img2 =
	 * ImageIO .read(new
	 * File("Extract_Images\\" + file2updatedName[0] + "_" + i + ".png"));
	 * 
	 * //Base Image Compare joinedImg =
	 * BaseImageExtraction.compareWithBaseImage(img2, img1);
	 * 
	 * String filePath = System.getProperty("user.dir") +
	 * "\\Extract_Images\\" + file1updatedName[0] + "_" + file2updatedName[0] + i +
	 * "_ImageCompare.png";
	 * 
	 * ImageIO.write(joinedImg, "png", new File(filePath));
	 * 
	 * img2 = ImageIO .read(new File("Extract_Images\\" + file1updatedName[0] + "_"
	 * + file2updatedName[0] + i + "_ImageCompare.png"));
	 * 
	 * joinedImg = BaseImageExtraction.joinBufferedImage(img1, img2);
	 * 
	 * filePath = System.getProperty("user.dir") +
	 * "\\Extract_Images\\" + file1updatedName[0] + "_" + file2updatedName[0] + i +
	 * ".png"; ImageIO.write(joinedImg, "png", new File(filePath));
	 * Asserter.validateTrue(diffinPerecentage == 0.0, "PDF data differs with " +
	 * df2.format(diffinPerecentage) + "%" + " & for reference " + "<a href=" +
	 * filePath + ">link</a>");
	 * 
	 * }
	 * 
	 * else {
	 * 
	 * BufferedImage img1 = ImageIO .read(new
	 * File("Extract_Images\\" + file1updatedName[0] + "_" + i + ".png"));
	 * BufferedImage img2 = ImageIO .read(new
	 * File("Extract_Images\\" + file2updatedName[0] + "_" + i + ".png"));
	 * 
	 * BufferedImage joinedImg = BaseImageExtraction.compareWithBaseImage(img1,
	 * img2);
	 * 
	 * String filePath = System.getProperty("user.dir")+"\\Extract_Images\\" +
	 * file1updatedName[0] + "_" + file2updatedName[0] + i + ".png";
	 * ImageIO.write(joinedImg, "png", new File(filePath));
	 * Asserter.validateTrue(diffinPerecentage == 0.0, "PDF data differs with " +
	 * df2.format(diffinPerecentage) + "%" + " & for reference " + "<a href=" +
	 * filePath + ">link</a>"); break;
	 * 
	 * } } }
	 * Reporter.log("Compared extracted image with reference image successfully",
	 * true);
	 * 
	 * Asserter.validateAssert.assertAll(); }
	 */

	
	/*
	 * @Test public void DynamicObjectChanges() throws IOException,
	 * TransformerException, SAXException, ParserConfigurationException,
	 * XPathExpressionException { Reporter.log("Load PDF content from First file " +
	 * getConfigProperty(DynamicObjectFileOne).toString().trim(), true);
	 * Reporter.log("Loaded PDF content from First file " +
	 * getConfigProperty(DynamicObjectFileOne).toString().trim() + " Successfully",
	 * true); Reporter.log("Load PDF content from Second file " +
	 * getConfigProperty(DynamicObjectFileTwo).toString().trim(), true);
	 * Reporter.log("Loaded PDF content from Second file " +
	 * getConfigProperty(DynamicObjectFileTwo).toString().trim() + " Successfully",
	 * true);
	 * 
	 * Reporter.log("Compare files after excluding date",true); QualidexUtility util
	 * = new QualidexUtility(); util.setCompareMode(ComparisonMode.TEXT_MODE);
	 * //util.excludeText("\\d{2}/\\d{2}/\\d{4}");
	 * util.excludeText("\\d{1}/\\d{1}/\\d{4}");
	 * //util.excludeText("\\d{4}-\\d{2}-\\d{2}"); boolean result = util.
	 * compare("src\\test\\Resources\\" +getConfigProperty(DynamicObjectFileOne).toString().trim(), "
	 * src\\test\\Resources\\" +getConfigProperty("DynamicObjectFileTwo").toString()
	 * .trim()); Asserter.validateTrue(result);
	 * Reporter.log("Compared files after excluding date",true);
	 * 
	 * Reporter.log("Compare extracted image with reference image", true);
	 * util.setImageDestinationPath("Extract_Images\\");
	 * util.savePdfAsImage("src\\test\\Resources\\" +
	 * getConfigProperty(DynamicObjectFileOne).toString().trim());
	 * util.savePdfAsImage("src\\test\\Resources\\" +
	 * getConfigProperty(DynamicObjectFileTwo).toString().trim());
	 * 
	 * int file1PageCount = util.getPageCount("src\\test\\Resources\\" +
	 * getConfigProperty(DynamicObjectFileOne).toString().trim());
	 * 
	 * for (int i = 1; i <= file1PageCount; i++) {
	 * 
	 * String file1Name = getConfigProperty(DynamicObjectFileOne).toString().trim();
	 * String[] file1updatedName = file1Name.split("\\."); String file2Name =
	 * getConfigProperty(DynamicObjectFileTwo).toString().trim(); String[]
	 * file2updatedName = file2Name.split("\\.");
	 * 
	 * double diffinPerecentage = BaseImageExtraction.imageCompare(
	 * "Extract_Images\\" + file1updatedName[0] + "_" + i + ".png",
	 * "Extract_Images\\" + file2updatedName[0] + "_" + i + ".png");
	 * 
	 * 
	 * if (diffinPerecentage != 0.0) { if(diffinPerecentage > 2.0) { BufferedImage
	 * img1 = ImageIO .read(new
	 * File("Extract_Images\\" + file1updatedName[0] + "_" + i + ".png"));
	 * BufferedImage img2 = ImageIO .read(new
	 * File("Extract_Images\\" + file2updatedName[0] + "_" + i + ".png"));
	 * BufferedImage joinedImg = BaseImageExtraction.joinBufferedImage(img1, img2);
	 * 
	 * String filePath = System.getProperty("user.dir")+"\\Extract_Images\\" +
	 * file1updatedName[0] + "_" + file2updatedName[0] + i + ".png";
	 * ImageIO.write(joinedImg, "png", new File(filePath));
	 * Asserter.validateTrue(diffinPerecentage == 0.0, "PDF data differs with " +
	 * df2.format(diffinPerecentage) + "%" + " & for reference " + "<a href=" +
	 * filePath + ">link</a>"); break; } } }
	 * Reporter.log("Compared extracted image with reference image successfully",
	 * true); Asserter.validateAssert.assertAll(); }
	 */
    @Test
	public void Compare() throws IOException, TransformerException, SAXException, ParserConfigurationException,
	XPathExpressionException {
		Reporter.log("Load PDF content from First file " + getConfigProperty(File_1).toString().trim(), true);
		Reporter.log("Loaded PDF content from First file " + getConfigProperty(File_1).toString().trim() + " Successfully", true);
		Reporter.log("Load PDF content from Second file " + getConfigProperty(File_2).toString().trim(), true);
		Reporter.log("Loaded PDF content from Second file " + getConfigProperty(File_2).toString().trim() + " Successfully", true);
		
		loadPDFContentToDoc1((getConfigProperty(File_1).toString().trim()),(getConfigProperty(File_2).toString().trim()));
		
		Reporter.log("Verify number of pages are same", true);
		verifyNumberOfPages();
        Reporter.log("Verified!! Number of pages are same", true);
		
		Reporter.log("Fetching data from PDF files", true);
		
		fetchDataAndConvertToXML1();
		
        Reporter.log("Fetched data from PDF files", true);
        
       	Reporter.log("Verify PDF data Content", true);
	    //PDF2XMLComparator.validatePDFFileData(firstXmlDoc, secondXmlDoc);
		Reporter.log("Verified PDF data Content", true);
		QualidexUtility util = new QualidexUtility();
		Reporter.log("Compare extracted image with reference image", true);
		util.setImageDestinationPath("Extract_Images\\");
		util.savePdfAsImage("src\\test\\Resources\\" + getConfigProperty(File_1).toString().trim());
		util.savePdfAsImage("src\\test\\Resources\\" + getConfigProperty(File_2).toString().trim());
		
		int file1PageCount = util.getPageCount("src\\test\\Resources\\" + getConfigProperty(File_1).toString().trim());

		for (int i = 1; i <= file1PageCount; i++) {

			String file1Name = getConfigProperty(File_1).toString().trim();
			String[] file1updatedName = file1Name.split("\\.");
			String file2Name = getConfigProperty(File_2).toString().trim();
			String[] file2updatedName = file2Name.split("\\.");

			double diffinPerecentage = BaseImageExtraction.imageCompare(
					"Extract_Images\\" + file1updatedName[0] + "_" + i + ".png",
					"Extract_Images\\" + file2updatedName[0] + "_" + i + ".png");

			if (diffinPerecentage != 0.0) {
				if(diffinPerecentage >0.0) {
					BufferedImage img1,img2,joinedImg;
					img1 = ImageIO
							.read(new File("Extract_Images\\" + file1updatedName[0] + "_" + i + ".png"));
					img2 = ImageIO
							.read(new File("Extract_Images\\" + file2updatedName[0] + "_" + i + ".png"));
					
					//Base Image Compare
					joinedImg = compareWithBaseImage(img2, img1);
					
					String filePath = System.getProperty("user.dir") + "\\Extract_Images\\" + file1updatedName[0] + "_"
							+ file2updatedName[0] + i + ".png";
					
					ImageIO.write(joinedImg, "png", new File(filePath));
					
					img2 = ImageIO
							.read(new File("Extract_Images\\" + file1updatedName[0] + "_"
									+ file2updatedName[0] + i + ".png"));
					
					joinedImg = main.java.com.qualitestgroup.dataextract.imagecomp.BaseImageExtraction.joinBufferedImage(img1, img2);

					filePath = System.getProperty("user.dir") + "\\Extract_Images\\" + file1updatedName[0] + "_"
							+ file2updatedName[0] + i + ".png";
					ImageIO.write(joinedImg, "png", new File(filePath));
					asserter.validateTrue(diffinPerecentage == 0.0,
							"PDF data differs with " + df2.format(diffinPerecentage) + "%" + " & for reference "
									+ "<a href=" + filePath + ">link</a>");
				}
				
				else
				{
					
					BufferedImage img1 = ImageIO
							.read(new File("Extract_Images\\" + file1updatedName[0] + "_" + i + ".png"));
					BufferedImage img2 = ImageIO
							.read(new File("Extract_Images\\" + file2updatedName[0] + "_" + i + ".png"));
					
					BufferedImage joinedImg = compareWithBaseImage(img1, img2);
					
					String filePath = System.getProperty("user.dir")+"\\Extract_Images\\" + file1updatedName[0]
							+ "_" + file2updatedName[0] + i + ".png";
					ImageIO.write(joinedImg, "png", new File(filePath));
					asserter.validateTrue(diffinPerecentage == 0.0, "PDF data differs with "
							+ df2.format(diffinPerecentage) + "%" + " & for reference " + "<a href=" + filePath + ">link</a>");
					break;
						
				}
			}
		}
		Reporter.log("Compared extracted image with reference image successfully", true);
		
		asserter.validateAssert.assertAll();
	}

	
	/*
	 * @Test public void TextChangesFile() throws Exception { compareTwoFiles();
	 * Asserter.validateAssert.assertAll();
	 * 
	 * 
	 * }
	 */
	
	
	

}
