package com.qualitestgroup.dataextract.library;

import java.awt.Desktop;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.PixelGrabber;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;

import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import com.qualitestgroup.data_extract_demo.damoregroup.Asserter;
import com.qualitestgroup.data_extract_demo.damoregroup.PDF2XMLComparator;

import name.fraser.neil.plaintext.diff_match_patch;
import name.fraser.neil.plaintext.diff_match_patch.Diff;
import name.fraser.neil.plaintext.diff_match_patch.Operation;

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
	private static Cell value;
	private static final String PdfToText = ".\\src\\test\\resources\\pdtxtoutputUTF8.txt";
	private static String PdfPath;
	private static String headerCoords;
	private static String footerCoords;

	/**
	 * This method set PDF and extarct PDF content
	 * 
	 * @param PDFPath - PDF path
	 * @return - Boolean True or False
	 */
	public static void setPDFLocationAndExtract(String PDFPath) {
		try {
			PdfPath = PDFPath;
			pdfExtracter();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * This method sets Header coordinates
	 * 
	 * @param coords - Header coodinates
	 * @return - Boolean True or False
	 */
	public static boolean setHeaderCoords(String coords) {
		try {
			headerCoords = coords;
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
			footerCoords = coords;
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
	@SuppressWarnings("unused")
	private static boolean setSection(String section, String fontStyle, int fontSize) {
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
	@SuppressWarnings("unused")
	private static String readCellValue(String excelSheetPath, String sheet, int cellNumber) {
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
	public static List<String> readCellValues(String excelSheetPath, String sheet, int cellNumber) {

		List<String> validationCellValues = new ArrayList<>();
		try {

			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(excelSheetPath));
			wb = WorkbookFactory.create(bis);
			sh = wb.getSheet(sheet);
			int noOfRows = sh.getLastRowNum();

			for (int i = 1; i <= noOfRows; i++) {
				value = (sh.getRow(i).getCell(cellNumber));
				String cellValue = value.toString();

				byte[] bytes = cellValue.getBytes(StandardCharsets.UTF_8);
				String cellValues = new String(bytes);

				validationCellValues.add(cellValues);

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

		return validationCellValues;

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
	public static List<String> applyFilterAndStoreCellValues(String excelSheetPath, String sheet,
			String filterCellValue, int filterColumnNumber, int cellNumber) {
		List<String> validationCellValues = new ArrayList<>();
		try {

			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(excelSheetPath));
			wb = WorkbookFactory.create(bis);
			sh = wb.getSheet(sheet);
			int noOfRows = sh.getLastRowNum();

			for (int i = 1; i <= noOfRows; i++) {
				value = (sh.getRow(i).getCell(filterColumnNumber));
				String cellValue = value.toString();

				if (cellValue.equals(filterCellValue)) {
					value = (sh.getRow(i).getCell(cellNumber));
					String cellValue1 = value.toString();

					logger.info(cellValue1);

					byte[] bytes = cellValue1.getBytes(StandardCharsets.UTF_8);
					String cellValues = new String(bytes);

					validationCellValues.add(cellValues);

				}
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
		return validationCellValues;
	}

	/**
	 * 
	 * This method verifies the value in the Header
	 * 
	 * 
	 * @param value - String value which is being verifie
	 * @return Boolean True or False
	 */
	@SuppressWarnings("unused")
	private static boolean existsInHeader(String value) {
		boolean found = false;
		try {
			File file1 = new File(PdfToText);
			BufferedReader br = new BufferedReader(new FileReader(file1));

			// Declaring a string variable
			String st;
			// Condition holds true till
			// there is character in a string
			while ((st = br.readLine()) != null) {

				String regexPattern = (headerCoords);
				Pattern pattern = Pattern.compile(regexPattern);
				Matcher matcher = pattern.matcher(st);

				String result = value;

				if (matcher.find()) {

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
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return found;
	}

	/**
	 * 
	 * This method compare given value with Pdf
	 * 
	 * @param value - Text Value which being compared against PDF
	 */

	public static boolean findValuesInPdf(String value) {

		boolean result = false;
		try {
			File file1 = new File(PdfToText);

			String st1 = value;

			logger.info("Validation text is : " + st1);

			{
				BufferedReader br = new BufferedReader(new FileReader(file1));

				String st;
				// Condition holds true till
				while ((st = br.readLine()) != null) {

					if (!(st.contains(st1))) {
						continue;
					} else {

						logger.info("Found the match: " + st1 + " is present in the PDF");
						result = true;
						break;

					}

				}
				if ((st = br.readLine()) == null) {
					logger.info("match not found");
					result = false;
				}
				br.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * This method verifies the value in the Footer
	 * 
	 * @param value - String value which is being verified
	 * @return - Boolean True or False
	 */
	@SuppressWarnings("unused")
	private static boolean existsInFooter(String value) {
		boolean found = false;
		try {
			File file1 = new File(PdfToText);
			BufferedReader br = new BufferedReader(new FileReader(file1));

			String st;
			// Condition holds true till there is character in a string
			while ((st = br.readLine()) != null) {

				String regexPattern = (footerCoords);

				Pattern pattern = Pattern.compile(regexPattern);
				Matcher matcher = pattern.matcher(st);

				String result = value;

				if (matcher.find()) {
					if (found = true) {
						logger.info("Footer is present");
						if (st.contains(result)) {
							logger.info("value " + result + " is present in footer " + st);
							found = true;
						} else {
							logger.info("Value " + result + " is not Present in footer " + st);
							Asserter.validateTrue(found, "Value " + result + " is not Present in footer " + st);
							found = false;
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
			br.close();
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
	@SuppressWarnings("unused")
	private static boolean isEmptyHeader() {
		boolean found = false;
		try {
			File file1 = new File(PdfToText);

			BufferedReader br = new BufferedReader(new FileReader(file1));

			String st;
			// Condition holds true till
			// there is character in a string
			while ((st = br.readLine()) != null) {

				{

					String regexPattern = (headerCoords);

					Pattern pattern = Pattern.compile(regexPattern);
					Matcher matcher = pattern.matcher(st);

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

			}
			br.close();
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

	@SuppressWarnings("unused")
	private static boolean isEmptyFooter() {
		boolean found = false;
		try {

			File file1 = new File(PdfToText);

			BufferedReader br = new BufferedReader(new FileReader(file1));

			String st;
			// Condition holds true till
			// there is character in a string
			while ((st = br.readLine()) != null) {

				{

					String regexPattern = (footerCoords);

					Pattern pattern = Pattern.compile(regexPattern);
					Matcher matcher = pattern.matcher(st);

					if (matcher.find()) {
						found = true;

						logger.info("Footer is present");
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
				logger.info("Footer is empty");
				Asserter.validateTrue((st = br.readLine()) != null, "Footer not found");

			}
			br.close();
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;

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

			File file = new File(".\\src\\test\\resources");
			file.mkdir();

			TextUTF16LEToTextUTF8("C:\\PDF\\pdtxtoutput.txt");

		} catch (Exception e) {
			e.printStackTrace();
		}

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
	 * @return count of string in PDF
	 */
	@SuppressWarnings("resource")
	public static int findOccurence(String value) {
		int count = 0;
		try {

			File file1 = new File(PdfToText);

			logger.info("Validation text is : " + value);

			{
				BufferedReader br = new BufferedReader(new FileReader(file1));

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

					logger.info("Validated text " + value + " appears " + count + " times in a PDF");

				} else {
					logger.info("Text" + value + " not found in PDF");

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

	@SuppressWarnings("unused")
	private static void readFromDB(String url, String userName, String password, String tableName, String CoulmnName) {

		try (Connection conn = DriverManager.getConnection(url, userName, password)) {

			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName);
			while (rs.next()) {
				// This method is not implemented
			}
		}

		catch (SQLException ex) {
			logger.info(ex.getMessage());
			ex.printStackTrace();
		}
	}

	/**
	 * This method verifies whether text is present in PDF
	 * 
	 * @param PDFPath - Path of Pdf
	 * @return - Boolean True or False
	 */

	public static boolean isPdfEmpty(String PDFPath) {
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
	 * This method helps to find the table in PDF
	 * 
	 * @param PDFPath  - Path of PDF
	 * @param refTable - Path of reference table
	 * @return
	 */
	@SuppressWarnings("unused")
	private static boolean findTable(String PDFPath, String refTable) {
		// This method is not implimented
		return false;

	}

	/**
	 * This methods helps to find the image in the PDF
	 * 
	 * @param PDFPath      - Path of PDF
	 * @param refImagePath - Path of reference image
	 * 
	 * @return result
	 */
	public static boolean findImage(String PDFPath, String refImagePath) {
		boolean result = false;
		boolean results = false;
		try {
			// Extract images from the PDF
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

				results = searchImage(imageList, "true");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return results;

	}

	/**
	 * This method extracts the images from PDF
	 * 
	 * @param PDF - PDF path
	 * @throws IOException
	 */
	private static void imageExtraction(String PDF) throws IOException

	{
		File theDir = new File(".\\Images\\");
		if (!theDir.exists()) {
			theDir.mkdirs();
		}
		PDDocument document = PDDocument.load(new File(PDF));
		int NoOfPage = document.getNumberOfPages();

		for (int page = 0; page < NoOfPage; page++) {
			PDPage pdfpage = document.getPage(page);

			int i = 1;
			PDResources pdResources = pdfpage.getResources();
			for (COSName c : pdResources.getXObjectNames()) {
				PDXObject o = pdResources.getXObject(c);
				if (o instanceof org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject) {
					File file = new File(".\\Images\\page_" + page + "_image_" + i + ".png");
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
	 * @param value     - value "true"
	 * @return Boolean true or false
	 */
	private static boolean searchImage(String imageList, String value) {
		boolean result = false;
		try {

			String st = imageList;
			boolean results = st.contains(value);

			if (results == false) {
				result = false;
				logger.info("Image not found in PDF");

			} else {
				result = true;
				logger.info("Image found in PDF");

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;

	}

	/**
	 * This method process the refImage with Images in PDF
	 * 
	 * @param imageFiles - List of Images extracted from PDF
	 * @param refImage   - refernec image
	 * @return Boolean true or false
	 * @throws IOException
	 */
	private static boolean processImage(File imageFiles, String refImage) throws IOException {
		boolean result = false;
		String file1 = refImage;

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

			{
				BufferedReader br = new BufferedReader(new FileReader(file1));

				String st;
				// Condition holds true till
				// there is character in a string
				while ((st = br.readLine()) != null) {

					String regexPattern = value;

					Pattern pattern = Pattern.compile(regexPattern);
					Matcher matcher = pattern.matcher(st);

					if (!(matcher.find())) {
						continue;
					} else {
						found = true;
						logger.info(st);
						logger.info("Found the match: " + matcher.group() + " is starting at index " + matcher.start()
								+ "  and ending at index " + matcher.end());

						break;

					}

				}
				if ((st = br.readLine()) == null) {
					found = false;
					logger.info("match not found");

				}
				br.close();
			}

		} catch (Exception e) {
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
		String stringLine = null;
		try {
			// Declaring a string variable
			String st;
			File extractedFile = new File(PdfToText);
			BufferedReader extractedBr = new BufferedReader(new FileReader(extractedFile));

			{

				while ((st = extractedBr.readLine()) != null) {
					// Decode string using UTF_8 encoder
					byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
					String decodedFile = new String(bytes);

					stringLine = decodedFile;

					if (st.contains(stringLine)) {

						// regex to match the exact font style
						String fontStyleRegexPattern = "\\+" + fontStyle + "\"," + "\\s";
						Pattern fontStylePattern = Pattern.compile(fontStyleRegexPattern);
						Matcher fontStyleMatcher = fontStylePattern.matcher(st);

						if (fontStyleMatcher.find()) {

							// regex to match the exact font size
							String fontSizeRegexPattern = "\\s" + fontSize + "," + "\\s";
							Pattern fontSizePattern = Pattern.compile(fontSizeRegexPattern);
							Matcher fontSizeMatcher = fontSizePattern.matcher(st);

							if (fontSizeMatcher.find()) {

								logger.info(st);
								logger.info("Validation text " + stringLine + " is found with a expected format");

								found = true;
								break;
							}
						}
					}

					else {

						continue;

					}
				}
			}
			if ((st = extractedBr.readLine()) == null) {

				logger.info(stringLine + ": text isn't found in the expected format");

				found = false;
			}
			extractedBr.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return found;

	}

	/**
	 * This method helps highlight the PDF file differences
	 * 
	 * @param basePDF     : base PDF file path
	 * @param refPDF      : refernce PDF file path
	 * @param diff        : difference between two PDF files
	 * @param toBeRemoved : removed content from reference PDF
	 * @param newlyAdded  : newly added content in reference PDF
	 * @return
	 */

	private static String qualiDexCompare(String basePDF, String refPDF, Map<String, String> diff,
			List<String> toBeRemoved, List<String> newlyAdded) {
		try {
			String PDFText1 = PDF_Extractor(basePDF);
			String PDFText2 = PDF_Extractor(refPDF);

			// create html file
			File filz = new File(".\\Difference.html");
			if (!filz.exists()) {

				File htmlFile = new File(".\\Difference.html");

				// file creation
				htmlFile.createNewFile();
				System.out.println("File created");
			}

			else {
				System.out.println("File exists");
			}

			HashMap<String, String> diffString = new HashMap<String, String>();
			diffString.put("", "");

			diff_match_patch dmp = new diff_match_patch();

			LinkedList<diff_match_patch.Diff> diff2 = dmp.diff_main(PDFText1, PDFText2);

			dmp.diff_cleanupEfficiency(diff2);

			LinkedList<diff_match_patch.Diff> diffFinalRepor = new LinkedList<Diff>();
			String earlierDiffState = "";
			String textInEarlierDiff = "";
			for (int i = 0; i < diff2.size(); i++) {
				Diff f = diff2.get(i);

				// boolean v = f.equals(k[1]);

				System.out.println("Printing diff2: " + f.text);

				String b = f.toString().replace(f.text, "");

				if (b.contains("EQUAL")) {
					System.out.println(" ");
					String regexForSplit = "(";
					if (earlierDiffState.equals("DELETE")) {
						// in this case it means string is deleted

						int loopCount = 0;
						for (String s : toBeRemoved) {

							if (textInEarlierDiff.contains(s)) {
								if (loopCount > 0) {
									regexForSplit = regexForSplit + "|";
									regexForSplit = regexForSplit + "(";
								}
								regexForSplit = regexForSplit + "(?<=" + s + ")|(?=" + s + ")";
								regexForSplit = regexForSplit + ")";
								loopCount++;
							}

						}
						if (loopCount > 0) {
							String[] splitValue = textInEarlierDiff.split(regexForSplit);
							for (String s : splitValue) {
								if (toBeRemoved.contains(s)) {
									diffFinalRepor.add(new Diff(Operation.EQUAL, s));

								} else {
									diffFinalRepor.add(new Diff(Operation.DELETE, s));

								}
							}

						} else {

							diffFinalRepor.add(new Diff(Operation.DELETE, textInEarlierDiff));
						}
					}

					diffFinalRepor.add(f);
					earlierDiffState = "EQUAL";

				}

				// We assume we will not have 2 delete in sequence after diff_cleanupEfficiency
				// we also assume a string replace will always have Delete, Insert and not
				// Insert, Delete

				else if (b.contains("DELETE")) {
					System.out.println(" ");

					earlierDiffState = "DELETE";

				} else if (b.contains("INSERT")) {
					String regexForSplit = "(";
					String regexForSplitModified = "(";
					if (earlierDiffState.equals("EQUAL")) {

						int loopCount = 0;
						for (String s : newlyAdded) {

							if (f.text.contains(s)) {
								if (loopCount > 0) {
									regexForSplit = regexForSplit + "|";
									regexForSplit = regexForSplit + "(";
								}
								regexForSplit = regexForSplit + "(?<=" + s + ")|(?=" + s + ")";
								regexForSplit = regexForSplit + ")";
								loopCount++;
							}

						}
						if (loopCount > 0) {
							String[] splitValue = f.text.split(regexForSplit);
							for (String s : splitValue) {
								if (newlyAdded.contains(s)) {
									diffFinalRepor.add(new Diff(Operation.EQUAL, s));

								} else {
									diffFinalRepor.add(new Diff(Operation.INSERT, s));

								}
							}
						} else {

							diffFinalRepor.add(new Diff(Operation.INSERT, f.text));
						}

					} else if (earlierDiffState.equals("DELETE")) {

						// in this case it means string is deleted
						if (diff == null || diff.isEmpty()) {
							diffFinalRepor.add(new Diff(Operation.DELETE, textInEarlierDiff));
							diffFinalRepor.add(f);
						} else {

							List<String> baseString = new ArrayList<String>();
							Set<String> keys = diff.keySet();

							for (String k : keys) {
								baseString.add(k);
								System.out.println("key: " + k);
							}

							int loopCount = 0;
							for (String s : baseString) {

								if (textInEarlierDiff.contains(s)) {

									if (f.text.contains(diff.get(s))) {

										if (loopCount > 0) {
											regexForSplit = regexForSplit + "|(";

											regexForSplitModified = regexForSplitModified + "|(";
										}
										regexForSplit = regexForSplit + "(?<=" + s + ")|(?=" + s + "))";
										regexForSplitModified = regexForSplitModified + "(?<=" + diff.get(s) + ")|(?="
												+ diff.get(s) + "))";
										loopCount++;
									}
								}

							}
							if (loopCount > 0) {
								String[] baseValue = textInEarlierDiff.split(regexForSplit);
								String[] modifiedValue = f.text.split(regexForSplitModified);
								int q = 0;
								for (String s : baseValue) {
									if (baseString.contains(s)) {
										diffFinalRepor.add(new Diff(Operation.EQUAL, diff.get(s)));

									} else {

										diffFinalRepor.add(new Diff(Operation.DELETE, s));
										diffFinalRepor.add(new Diff(Operation.INSERT, modifiedValue[q]));

									}
									q++;
								}
							} else {

								diffFinalRepor.add(new Diff(Operation.DELETE, textInEarlierDiff));
								diffFinalRepor.add(f);
							}
						}
					}
					System.out.println(" ");
					earlierDiffState = "INSERT";
				}
				textInEarlierDiff = f.text;

			}
			if (earlierDiffState.equals("DELETE")) {
				diffFinalRepor.add(new Diff(Operation.DELETE, textInEarlierDiff));
			}

			String result = dmp.diff_prettyHtml(diffFinalRepor);

			Path path = Paths.get(".\\Difference.html");

			try {
				// Now calling Files.writeString() method
				// with path , content & standard charsets
				Files.writeString(path, result.replaceAll("&para;", ""), StandardCharsets.UTF_8);

			}

			// Catch block to handle the exception
			catch (IOException ex) {
				// Print messqage exception occurred as
				// invalid. directory local path is passed
				System.out.print("Invalid Path");
			}

			File f = new File("Difference.html");

			// Display PDF difference in HTML format
			Desktop.getDesktop().browse(f.toURI());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * This method highlights Pdf differences by ignoring expected deleted strings ,
	 * newly inserted strings and modified strings in ref. PDF
	 * 
	 * @param basePDF     Base PDF file path
	 * @param refPDF      ref. PDF file path
	 * @param diff        modified strings of comparison between base PDF vs ref.
	 *                    PDF
	 * @param toBeRemoved deleted strings in ref. PDF
	 * @param newlyAdded  newly inserted strings in base PDF
	 * @return returns PDF comparison
	 */

	public static String qualiDexCompares(String basePDF, String refPDF, Map<String, String> diff,
			List<String> toBeRemoved, List<String> newlyAdded) {
		return qualiDexCompare(basePDF, refPDF, diff, toBeRemoved, newlyAdded);
	}

	/**
	 * This method highlights Pdf differences by comparing base PDF vs Ref PDF
	 * 
	 * @param basePDF Base PDF file path
	 * @param refPDF  ref. PDF file path
	 * @return returns PDF comparison
	 */

	public static String qualiDexCompares(String basePDF, String refPDF) {
		List<String> deletedStrings = Arrays.asList("");
		List<String> newlyAdded = Arrays.asList("");
		Map<String, String> diff = new HashMap<>();
		return qualiDexCompare(basePDF, refPDF, diff, deletedStrings, newlyAdded);

	}

	/**
	 * This method highlights Pdf differences by ignoring expected modified Strings
	 * in ref PDF
	 * 
	 * @param basePDF Base PDF file path
	 * @param refPDF  ref. PDF file path
	 * @param diff    modified strings of comparison between base PDF vs ref. PDF
	 * @return returns PDF comparison
	 */

	public static String qualiDexCompares(String basePDF, String refPDF, Map<String, String> diff) {
		List<String> deletedStrings = Arrays.asList("");
		List<String> newlyAdded = Arrays.asList("");
		return qualiDexCompare(basePDF, refPDF, diff, deletedStrings, newlyAdded);

	}

	/**
	 * This method highlights Pdf differences by ignoring expected newly inserted
	 * strings in ref PDF
	 * 
	 * @param basePDF    Base PDF file path
	 * @param refPDF     ref. PDF file path
	 * @param newlyAdded newly inserted strings in base PDF
	 * @return returns PDF comparison
	 */

	public static String qualiDexCompares(String basePDF, String refPDF, List<String> newlyAdded) {
		List<String> deletedStrings = Arrays.asList("");
		Map<String, String> diff = new HashMap<>();
		return qualiDexCompare(basePDF, refPDF, diff, deletedStrings, newlyAdded);
	}

	/**
	 * This method highlights Pdf differences by ignoring expected deleted and newly
	 * inserted strings in ref PDF
	 * 
	 * @param basePDF     Base PDF file path
	 * @param refPDF      ref. PDF file path
	 * @param toBeRemoved deleted strings in ref. PDF
	 * @param newlyAdded  newly inserted strings in base PDF
	 * @return returns PDF comparison
	 */

	public static String qualiDexCompares(String basePDF, String refPDF, List<String> toBeRemoved,
			List<String> newlyAdded) {
		Map<String, String> diff = new HashMap<>();
		return qualiDexCompare(basePDF, refPDF, diff, toBeRemoved, newlyAdded);
	}

	/**
	 * This method extracts PDF content
	 * 
	 * @param PDF Pdf file
	 * @return extracted Pdf Texts
	 * @throws IOException
	 */

	private static String PDF_Extractor(String PDF) throws IOException {
		File pdf_file = new File(PDF);
		PDDocument document = PDDocument.load(pdf_file);
		PDFTextStripper pdfstripper = new PDFTextStripper();
		String text = pdfstripper.getText(document);
		return text;
	}
}
