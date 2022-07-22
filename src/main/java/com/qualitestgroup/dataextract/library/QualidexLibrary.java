package com.qualitestgroup.dataextract.library;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.PixelGrabber;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;

import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.testng.Assert;
import org.testng.Reporter;

import com.qualitestgroup.data_extract_demo.damoregroup.Asserter;
import com.qualitestgroup.data_extract_demo.damoregroup.PDF2XMLComparator;


/**
 * 
 * Qualidex library
 * 
 * @author pavan.kumar
 *
 */
public class QualidexLibrary {
	private final static Logger logger = Logger.getLogger(PDF2XMLComparator.class.getName());
	private static Workbook wb;
	private static Sheet sh;
	private static FileInputStream fis;
	private static Cell value;
	private static final String ValidateText = ".\\src\\test\\resources\\ExcelToText.txt";
	private static final String PdfToText = ".\\src\\test\\resources\\pdtxtoutputUTF8.txt";
	private static String PdfPath;
	private static String ExcelPath;
	private static String Coords;
	private static String Section;
	private static String FontStyle;
	private static int FontSize;

	/**
	 * This method set PDF and extarct PDF content
	 * 
	 * @param PDFPath - PDF path
	 * @return - Boolean True or False
	 */
	public static boolean setPDFLocationAndExtract(String PDFPath) {
		try {
			PdfPath = PDFPath;
			pdfExtracter();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * This method sets Header coordinates
	 * 
	 * @param coords - Header coodinates
	 * @return - Boolean True or False
	 */
	public static boolean setHeaderCoords(String coords) {
		try {
			Coords = coords;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}

	/**
	 * This method sets footer coordinates
	 * 
	 * @param coords - Footer coordinates
	 * @return - Boolean True or False
	 */
	public static boolean setFooterCoords(String coords) {
		try {
			Coords = coords;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * This method sets string section with font style and font size
	 * 
	 * @param section   - String section
	 * @param fontStyle - font style
	 * @param fontSize  - font size
	 * @return - Boolean True or False
	 */
	private static boolean setSection(String section, String fontStyle, int fontSize) {
		Section = section;
		FontStyle = fontStyle;
		FontSize = fontSize;
		return false;

	}

	/**
	 * 
	 * This method sets excel path
	 * 
	 * @param excelPath - Path of excel
	 * @return - Boolean True or False
	 */
	public static boolean setExcelLocation(String excelPath) {
		try {
			ExcelPath = excelPath;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 
	 * This method reads the cell value from excel 
	 * 
	 * @param ExcelSheetPath - Path of excel sheet
	 * @param Sheet          - Excel Sheet
	 * @param CellNumber     - cell number
	 * 
	 * @return cellValue
	 */
	public static String readCellValue(String excelSheetPath, String sheet, int cellNumber) {
		String cellValue = null;
		try {
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(excelSheetPath));
			wb = WorkbookFactory.create(bis);
			sh = wb.getSheet(sheet);
			int noOfRows = sh.getLastRowNum();
			for (int i = 1; i <= noOfRows; i++) {
				value = (sh.getRow(i).getCell(cellNumber));
				cellValue = value.toString();
				logger.info(cellValue);
				// System.out.println(cellValue);

			}

		} catch (EncryptedDocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cellValue;

	}

	/**
	 * 
	 * This Method reads cell value from excel and compares with PDF
	 * 
	 * @param excelSheetPath - Path of Excel
	 * @param sheet          - Excel Sheet
	 * @param cellNumber     - cell number
	 */
	public static void storeCellValueAndCompare(String excelSheetPath, String sheet, int cellNumber) {
		try {
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(excelSheetPath));
			wb = WorkbookFactory.create(bis);
			sh = wb.getSheet(sheet);
			int noOfRows = sh.getLastRowNum();
			for (int i = 1; i <= noOfRows; i++) {
				value = (sh.getRow(i).getCell(cellNumber));
				String cellValue = value.toString();
				logger.info(cellValue);
				//Call compare method
				compare(cellValue);
			}

		} catch (EncryptedDocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * 
	 * This method fetch text from PDF and store it in
	 * src\\test\\resources\\PdfToText.txt file
	 * 
	 * @param PDFPath - Path of PDF
	 * 
	 * 
	 */
	private static void storePdfToText(String PDFPath) {
		try {
			// Fetch the PDF
			File pdf_file = new File(PDFPath);
			PDDocument document = PDDocument.load(pdf_file);
			PDFTextStripper pdfstripper = new PDFTextStripper();
			String text = pdfstripper.getText(document);
			@SuppressWarnings("resource")
			// store the extracted text from PDF
			FileWriter fWriter = new FileWriter(".\\src\\test\\resources\\pdtxtoutputUTF8.txt",
					Charset.forName("utf-8"));
			fWriter.write(text);
			// System.out.println(text);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 *
	 * This method compares excel filtered value against PDF
	 * 
	 * @param excelSheetPath     - Path of excel sheet
	 * @param sheet              - Excel sheet
	 * @param filterCellValue    - value which is being filtered
	 * @param cellNumber         - cell number
	 * @param filterColumnNumber - Column where filter will be applied
	 * @throws Exception - Exception
	 * 
	 */
	public static void applyFilterAndCompare(String excelSheetPath, String sheet, String filterCellValue,
			int filterColumnNumber, int cellNumber) throws Exception {

		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(excelSheetPath));
		wb = WorkbookFactory.create(bis);
		sh = wb.getSheet(sheet);
		int noOfRows = sh.getLastRowNum();

		for (int i = 1; i <= noOfRows; i++) {
			value = (sh.getRow(i).getCell(filterColumnNumber));
			String cellValue = value.toString();

			if (cellValue.equals(filterCellValue)) {
				value = (sh.getRow(i).getCell(cellNumber));
				String cellValue1 = value.getStringCellValue();
				// System.out.println(cellValue1);
				logger.info(cellValue1);
				//Call compare method
				compare(cellValue1);

			}

		}
		
	}

	/**
	 * 
	 * This method compare given value with Pdf
	 * 
	 * @param value - Text Value which being compared against PDF
	 */

	private static void compare(String value) {
		{
			try {
				File file1 = new File(PdfToText);

				String st1 = value;
				// Condition holds true till

				// Print the string
				Reporter.log("Validation text is : " + st1);
				logger.info("Validation text is : " + st1);

				{
					BufferedReader br = new BufferedReader(new FileReader(file1));

					// Declaring a string variable

					String st;
					// Condition holds true till
					// there is character in a string
					while ((st = br.readLine()) != null) {

						String regexPattern = st1;
						// String regexPattern = sc.nextLine();
						Pattern pattern = Pattern.compile(regexPattern);
						Matcher matcher = pattern.matcher(st);
						boolean found = false;

						if (!(matcher.find())) {
							continue;
						} else {
							logger.info(st);
							logger.info("Found the match: " + matcher.group() + " is starting at index "
									+ matcher.start() + "  and ending at index " + matcher.end());

							Reporter.log("Found the match: " + matcher.group() + " is starting at index "
									+ matcher.start() + "  and ending at index " + matcher.end());
							break;

						}

					}
					if ((st = br.readLine()) == null) {
						logger.info("match not found");
						Asserter.validateTrue((st = br.readLine()) != null,st1+" is not found in the PDF");
						Reporter.log("match not found");

					}

				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * 
	 * This method verifies the value in the Header
	 * 
	 * 
	 * @param value - String value which is being verifie
	 * @return Boolean True or False
	 */
	public static boolean existsInHeader(String value) {
		boolean found = false;
		try {
			File file1 = new File(PdfToText);
			BufferedReader br = new BufferedReader(new FileReader(file1));

			// Declaring a string variable
			String st;
			// Condition holds true till
			// there is character in a string
			while ((st = br.readLine()) != null) {

				// Footer coords "90, 108.45,"
				String regexPattern = (Coords);
				// String regexPattern = sc.nextLine();
				Pattern pattern = Pattern.compile(regexPattern);
				Matcher matcher = pattern.matcher(st);
				
				String result = value;

				if (matcher.find()) {
					// System.out.println("found the Header " + matcher.group() + " starting at
					// index " + matcher.start()
					// + " and ending at index " + matcher.end());

					if (found = true) {
						logger.info("Header is present");
						if (st.contains(result)) {
							logger.info("value " + result + " is present in Header " + st);
							found = true;
						} else {
							logger.info("Value " + result + " is not Present in Header " + st);
							Asserter.validateTrue(st.contains(result),
									"Value " + result + " is not Present in Header " + st);
							found = false;
						}
					}
					break;

				}

			}
			if ((st = br.readLine()) == null) {
				logger.info("Header not found");
				Asserter.validateTrue((st = br.readLine()) != null, "Header not found");
				found = false;

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return found;
	}

	/**
	 * This method verifies the value in the Footer
	 * 
	 * @param value - String value which is being verified
	 * @return - Boolean True or False
	 */
	public static boolean existsInFooter(String value) {
		boolean found = false;
		try {
			File file1 = new File(PdfToText);
			BufferedReader br = new BufferedReader(new FileReader(file1));

			// Declaring a string variable
			String st;
			// Condition holds true till
			// there is character in a string
			while ((st = br.readLine()) != null) {

				// Footer coords "90, 108.45,"
				String regexPattern = (Coords);
				// String regexPattern = sc.nextLine();
				Pattern pattern = Pattern.compile(regexPattern);
				Matcher matcher = pattern.matcher(st);
				
				String result = value;

				if (matcher.find()) {
					// System.out.println("found the Header " + matcher.group() + " starting at
					// index " + matcher.start()
					// + " and ending at index " + matcher.end());

					if (found = true) {
						logger.info("Footer is present");
						if (st.contains(result)) {
							logger.info("value " + result + " is present in footer " + st);
							found = true;
						} else {
							logger.info("Value " + result + " is not Present in footer " + st);
							Asserter.validateTrue(found, "Value " + result + " is not Present in footer " + st);
							found = false ;
						}
					}
					break;

				}

				if ((st = br.readLine()) == null) {
					logger.info("Footer not found");
					Asserter.validateTrue((st = br.readLine()) != null, "Footer not found");
					found = false;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return found;
	}

	/**
	 * This method verifies whether Header is empty or not
	 * 
	 *
	 * @return - Boolean True or False
	 */
	public static boolean isEmptyHeader() {
		boolean found = false;
		try {
			File file1 = new File(PdfToText);

			BufferedReader br = new BufferedReader(new FileReader(file1));

			// Declaring a string variable
			String st;
			// Condition holds true till
			// there is character in a string
			while ((st = br.readLine()) != null) {

				{
					// Header coords "117,"
					String regexPattern = (Coords);
					// String regexPattern = sc.nextLine();
					Pattern pattern = Pattern.compile(regexPattern);
					Matcher matcher = pattern.matcher(st);
				

					// while (matcher.find())
					if (matcher.find()) {
						 found = true;
							logger.info("Header is present");
							logger.info(st);
							break;
							
						} else {
							continue;

						}
					}

				}

			
			if ((st = br.readLine()) == null) {
				found = false;
				logger.info(st);
				logger.info("Header not found");
				Asserter.validateTrue((st = br.readLine()) != null, "Header not found");
				//Reporter.log("Header not found");

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return found;
	}

	/**
	 * This method verifies whether footer is empty or not
	 * 
	 * 
	 * @return - Boolean True or False
	 */

	public static boolean isEmptyFooter() {
		boolean found = false;
		try {

			File file1 = new File(PdfToText);

			BufferedReader br = new BufferedReader(new FileReader(file1));

			// Declaring a string variable
			String st;
			// Condition holds true till
			// there is character in a string
			while ((st = br.readLine()) != null) {

				{
					// Footer coords "90, 108.45,"
					String regexPattern = (Coords);
					// String regexPattern = sc.nextLine();
					Pattern pattern = Pattern.compile(regexPattern);
					Matcher matcher = pattern.matcher(st);
					

					// while (matcher.find())
					if (matcher.find()) 
                       
						// System.out.println("found the Header " + matcher.group() + " starting at
						// index " + matcher.start()
						// + " and ending at index " + matcher.end());

						 {
						  found = true;
							// Asserter.validateTrue((found=true),"Footer is present");
							logger.info("Footer is present");
							logger.info(st);
							// Reporter.log("Footer is present");
							break;
							
						} else {
							continue;

						}

					}

				}

			
			if ((st = br.readLine()) == null) {
				found = false;
				logger.info(st);
				logger.info("Footer is empty");
				Asserter.validateTrue((st = br.readLine()) != null, "Footer not found");
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return found;

	}

	/**
	 * This method returns the number of pages in PDF
	 *
	 * @param PDF - Path of Pdf
	 * @return count - number of pages in pdf
	 */
	public static int returnNumberOfPages(String PDF) {
		int count = 0;
		try {
			PDDocument doc = PDDocument.load(new File(PDF));
			count = doc.getNumberOfPages();
			logger.info("Number of pages in Pdf :- " + count);
			Reporter.log("Number of pages in Pdf :- " + count);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;

	}

	/**
	 * This method converts text extracted from PDF which is in UTF-16LE to text
	 * UTF-8 format
	 * 
	 * @param pdtxtOutput - Pdf to text .txt file
	 * 
	 * @throws IOException
	 */
	private static void TextUTF16LEToTextUTF8(String pdtxtOutput) {
		try {
			FileInputStream fis = new FileInputStream(pdtxtOutput);
			InputStreamReader isr = new InputStreamReader(fis, "UTF-16LE");
			Reader in = new BufferedReader(isr);
			FileOutputStream fos = new FileOutputStream(".\\src\\test\\resources\\pdtxtoutputUTF8.txt");
			OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
			Writer out = new BufferedWriter(osw);

			int ch;
			while ((ch = in.read()) > -1) {
				out.write(ch);

			}

			out.close();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * This method finds the occurrence of given value in PDF
	 * 
	 * @param value - Text which is being validated
	 * 
	 * @return count
	 */
	@SuppressWarnings("resource")
	public static int findOccurence(String value) {
		int count = 0;
		try {

			File file1 = new File(PdfToText);

			logger.info("Validation text is : " + value);
			Reporter.log("Validation text is : " + value);

			{
				BufferedReader br = new BufferedReader(new FileReader(file1));

				// Declaring a string variable

				String st;

				while ((st = br.readLine()) != null) {

					if (st.contains(value)) {
						count++;

					}

					else {

						continue;

					}

				}
				if (count > 0) {

					logger.info("Validated text " + value + " appears " + count + " times");
					Reporter.log("Validated text " + value + " appears " + count + " times");
				} else {
					logger.info("Text not found");
					Asserter.validateTrue((count>0),value +  " appears " + count + " times and is not found in PDF");
					Reporter.log("Text not found");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;

	}

	/**
	 * 
	 * This method fetch the validation content from DB
	 * 
	 * @param url        - DB url
	 * @param userName   - DB user name
	 * @param password   - DB password
	 * @param tableName  - Table name
	 * @param CoulmnName - Column name
	 */
	private static void readFromDB(String url, String userName, String password, String tableName, String CoulmnName) {

		try (Connection conn = DriverManager.getConnection(url, userName, password)) {

			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName);
			while (rs.next()) {
				String content = rs.getString(CoulmnName);
				logger.info(content);
				Files.write(Paths.get(".\\src\\test\\resources\\ExcelToText.txt"), Arrays.asList(content),
						StandardCharsets.UTF_8);
			}
		}

		catch (SQLException ex) {
			logger.info(ex.getMessage());
			ex.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * This Method verifies the content in mentioned page
	 * 
	 * 
	 * @param PDFPath        - Path of Pdf
	 * @param pageNumber     - Page where content is being verified
	 * @param excelSheetPath - Path of excel sheet
	 * @param sheet          - Sheet number
	 * @param cellNumber     - Cell number
	 */
	private static void verifyContentInPage(String PDFPath, int pageNumber, String excelSheetPath, String sheet,
			int cellNumber) {
		try {
			PDDocument PdfPath1 = PDDocument.load(new File(PDFPath));
			// PDDocument PdfPath1 = PdfPath; // document
			int i = pageNumber; // page no.

			PDFTextStripper reader = new PDFTextStripper();
			reader.setStartPage(i);
			reader.setEndPage(i);
			String pageText = reader.getText(PdfPath1);
			FileWriter fWriter = new FileWriter(".\\src\\test\\resources\\pdtxtoutputUTF8.txt",
					Charset.forName("utf-8"));
			fWriter.write(pageText);
			storeCellValueAndCompare(excelSheetPath, sheet, cellNumber);
			// compare();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * This method verifies whether text is present in PDF
	 * 
	 * @param PDFPath - Path of Pdf
	 * @return - Boolean True or False
	 */

	public static boolean isTextPresentInPdf(String PDFPath) {
		boolean found = false;
		try {
			// Fetch the PDF
			File pdf_file = new File(PDFPath);
			PDDocument document = PDDocument.load(pdf_file);
			PDFTextStripper pdfstripper = new PDFTextStripper();
			String text = pdfstripper.getText(document);
			if (text != null) {
				logger.info("Text is Present in PDF");
				found = true;
			} else {
				logger.info("Text isn't Present in PDF");
				found = false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return found;

	}

	/**
	 * This method extract PDF data information into text file
	 * 
	 * @param PDFPath- Path of Pdf
	 * 
	 */
	private static void pdfExtracter() {
		try {

			Process process = Runtime.getRuntime().exec(
					"cmd /c start /wait java -Xmx2448m -cp  . pdf.PDFTextExtractor " + PdfPath, null,
					new File(".\\pdtextcreater"));
			process.waitFor();

			TextUTF16LEToTextUTF8("C:\\PDF\\pdtxtoutput.txt");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * This methods helps to find the image in the PDF
	 * 
	 * @param PDFPath   - Path of PDF
	 * @param refImagePath - Path of reference image
	 * 
	 * @return result
	 */
	public static boolean findImage(String PDFPath, String refImagePath) {
		boolean result = false;
		try {
			imageExtraction(PDFPath);
			File dir = new File(".\\Images\\");
			File[] directoryListing = dir.listFiles();
			ArrayList<Boolean> list = new ArrayList<Boolean>();
			String imageList = null;
		
			if (directoryListing != null) {
				for (File images : directoryListing) {

					result = processImage(images, refImagePath);
					list.add(result);
					imageList = list.toString();

				}

				searchImage(imageList, "true");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;

	}
	/**
	 * This method helps to find the table in PDF
	 * 
	 * @param PDFPath - Path of PDF
	 * @param refTable - Path of reference table
	 * @return
	 */
	private static boolean findTable(String PDFPath, String refTable) {
		// This method is not implimented
		return false;
		
	}

	/**
	 * This method extracts the images from PDF
	 * 
	 * @param PDF - PDF path
	 * @throws IOException
	 */
	private static void imageExtraction(String PDF) throws IOException

	{
		PDDocument document = PDDocument.load(new File(PDF));
		int NoOfPage = document.getNumberOfPages();

		for (int page = 0; page < NoOfPage; page++) {
			PDPage pdfpage = document.getPage(page);

			int i = 1;
			PDResources pdResources = pdfpage.getResources();
			for (COSName c : pdResources.getXObjectNames()) {
				PDXObject o = pdResources.getXObject(c);
				if (o instanceof org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject) {
					File file = new File(".\\Images\\image" + page + i + ".png");
					i++;
					ImageIO.write(((org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject) o).getImage(), "png",
							file);

				}
			}
		}
	}
    /**
     * This method finds the reference image with the PDF
     * 
     * @param imageList - List image extracted from PDF
     * @param value - value "true"
     * @return Boolean true or false
     */
	private static boolean searchImage(String imageList, String value) {
		try {

			String st = imageList;
			boolean result = st.contains(value);

			if (result == false) {
				logger.info("Image not found in PDF");
				Asserter.validateTrue(result, "Image not found in PDF");
				Reporter.log("Image not found");
			} else {

				logger.info("Image found in PDF");
				Asserter.validateTrue(result, "Image found in PDF");
				Reporter.log("Image found");

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;

	}
    /**
     * This method process the refImage with Images in PDF 
     * 
     * @param imageFiles - List of Images extracted from PDF
     * @param refImage - refernec image
     * @return Boolean true or false
     * @throws IOException
     */
	private static boolean processImage(File imageFiles, String refImage) throws IOException {
		boolean result = false;
		String file1 = refImage;
		// String file1 = "C:\\Users\\pavan.kumar\\Downloads\\Paid_Java_libreary_2.png";
		String file2 = imageFiles.toString();

		Image image1 = Toolkit.getDefaultToolkit().getImage(file1);
		Image image2 = Toolkit.getDefaultToolkit().getImage(file2);

		try {
			PixelGrabber grab1 = new PixelGrabber(image1, 0, 0, -1, -1, false);
			PixelGrabber grab2 = new PixelGrabber(image2, 0, 0, -1, -1, false);

			int[] data1 = null;

			if (grab1.grabPixels()) {
				int width = grab1.getWidth();
				int height = grab1.getHeight();
				data1 = new int[width * height];
				data1 = (int[]) grab1.getPixels();
			}

			int[] data2 = null;

			if (grab2.grabPixels()) {
				int width = grab2.getWidth();
				int height = grab2.getHeight();
				data2 = new int[width * height];
				data2 = (int[]) grab2.getPixels();
			}
			result = java.util.Arrays.equals(data1, data2);

		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		return result;

	}

	/**
	 * This method finds the given text in PDF
	 * 
	 * @param value - Text which is being searched
	 * @return boolean - bolean True or false
	 */
	public static boolean findText(String value) {
		boolean found = false;
		try {
			File file1 = new File(PdfToText);
			logger.info("Validation text is : " + value);
			Reporter.log("Validation text is : " + value);

			{
				BufferedReader br = new BufferedReader(new FileReader(file1));

				// Declaring a string variable

				String st;
				// Condition holds true till
				// there is character in a string
				while ((st = br.readLine()) != null) {

					String regexPattern = value;
					// String regexPattern = sc.nextLine();
					Pattern pattern = Pattern.compile(regexPattern);
					Matcher matcher = pattern.matcher(st);
					

					if (!(matcher.find())) {
						continue;
					} else {
						found=true;
						logger.info(st);
						logger.info("Found the match: " + matcher.group() + " is starting at index "
								+ matcher.start() + "  and ending at index " + matcher.end());

						Reporter.log("Found the match: " + matcher.group() + " is starting at index "
								+ matcher.start() + "  and ending at index " + matcher.end());
						break;

					}

				}
				if ((st = br.readLine()) == null) {
					found=false;
					logger.info("match not found");
					Asserter.validateTrue((st = br.readLine()) != null);
					Reporter.log("match not found");

				}

			}

		}catch (Exception e) {
			e.printStackTrace();
		}

		return found;

	}

	/**
	 * This method finds String value in the particular font format in PDF
	 * 
	 * @param value     - String value
	 * @param fontStyle - String font style
	 * @param fontSize  - String font size
	 * @return Boolean true or false
	 */
	public static boolean findWithFormat(String value, String fontStyle, String fontSize) {
		boolean found = false;
		try {
			// Declaring a string variable
			String st;
			File file1 = new File(PdfToText);
			BufferedReader br = new BufferedReader(new FileReader(file1));

			logger.info("Validation text is : " + value);
			Reporter.log("Validation text is : " + value);

			{

				while ((st = br.readLine()) != null) {

					if (st.contains(value)) {

						if (st.contains(fontStyle)) {

							if (st.contains(fontSize)) {
								logger.info(st);
								logger.info("Validation text found with format");
								Reporter.log("Validation text found with format");
								found = true;
								break;
							}
						}
					} else {

						continue;

					}

				}

			}
			if ((st = br.readLine()) == null) {
				logger.info("match not found");
				Asserter.validateTrue((st = br.readLine()) != null,value+" is not found in the PDF");
				Reporter.log("Validation text is not found");
				// Asserter.validateFalse(true,"match not found");
				// return false;
				found = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return found;

	}

}
