package main.java.com.qualitestgroup.dataextract.library;

import main.java.com.qualitestgroup.data_extract_demo.damoregroup.Asserter;
import main.java.com.qualitestgroup.data_extract_demo.damoregroup.PDF2XMLComparator;
import name.fraser.neil.plaintext.diff_match_patch;
import name.fraser.neil.plaintext.diff_match_patch.Diff;
import name.fraser.neil.plaintext.diff_match_patch.Operation;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.PixelGrabber;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.List;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Qualidex library
 *
 * @author pavan.kumar
 */
public class QualidexLibrary {
    private static final Logger logger = Logger.getLogger(PDF2XMLComparator.class.getName());
    QualidexBuilder qualidexBuilder = QualidexBuilder.builder().build();
    private Asserter asserter;

    /**
     * This method set PDF and extract PDF content
     *
     * @param pdfPath - path of the pdf
     */
    public void setPDFLocationAndExtract(String pdfPath) {
        qualidexBuilder.setPdfPath(pdfPath);
        qualidexBuilder.setPdfToText(pdfExtracter());
    }


    /**
     * This method sets Header coordinates
     *
     * @param coords - Header coordinates
     * @return - Boolean True or False
     */
    @SuppressWarnings("unused")
    public boolean setHeaderCoords(String coords) {
        try {
            qualidexBuilder.setHeaderCoords(coords);
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
    @SuppressWarnings("unused")
    public boolean setFooterCoords(String coords) {
        try {
            qualidexBuilder.setFooterCoords(coords);
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
    private boolean setSection(String section, String fontStyle, int fontSize) {
        return false;
    }

    /**
     *
     * This method sets excel path
     *
     * @param excelPath - Path of excel
     * @return - Boolean True or False
     */
	/*public boolean setExcelLocation(String excelPath) {
		try {
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}*/

    /**
     * This method reads the cell value from excel
     *
     * @param excelSheetPath - Path of excel sheet
     * @param sheet          - Excel Sheet
     * @param cellNumber     - cell number
     * @return cellValue
     */
    @SuppressWarnings("unused")
    private String readCellValue(String excelSheetPath, String sheet, int cellNumber) {
        String cellValue = null;
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(excelSheetPath));
            qualidexBuilder.setWorkbook(WorkbookFactory.create(bis));
            qualidexBuilder.setSheet(qualidexBuilder.getWorkbook().getSheet(sheet));
            int noOfRows = qualidexBuilder.getSheet().getLastRowNum();
            for (int i = 1; i <= noOfRows; i++) {
                qualidexBuilder.setValue(qualidexBuilder.getSheet().getRow(i).getCell(cellNumber));
                cellValue = qualidexBuilder.getValue().toString();
                logger.info(cellValue);
            }
        } catch (EncryptedDocumentException | InvalidFormatException | IOException e) {
            e.printStackTrace();
        }
        return cellValue;
    }

    /**
     * This Method reads cell value from excel and returns as list of strings
     *
     * @param excelSheetPath - Path of Excel
     * @param sheet          - Excel Sheet
     * @param cellNumber     - cell number
     * @return string validationCellValues
     */
    public List<String> readCellValues(String excelSheetPath, String sheet, int cellNumber) {
        List<String> validationCellValues = new ArrayList<>();
        String cellValue;
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(excelSheetPath))) {
            qualidexBuilder.setWorkbook(WorkbookFactory.create(bis));
            qualidexBuilder.setSheet(qualidexBuilder.getWorkbook().getSheet(sheet));
            int noOfRows = qualidexBuilder.getSheet().getLastRowNum();
            for (int i = 1; i <= noOfRows; i++) {
                qualidexBuilder.setValue(qualidexBuilder.getSheet().getRow(i).getCell(cellNumber));
                cellValue = qualidexBuilder.getValue().toString();
                byte[] bytes = cellValue.getBytes(StandardCharsets.UTF_8);
                String cellValues = new String(bytes);
                validationCellValues.add(cellValues);
            }
        } catch (EncryptedDocumentException | InvalidFormatException | IOException e) {
            e.printStackTrace();
        }
        return validationCellValues;
    }

    /**
     * This method apply filter to excel and returns as list of string
     *
     * @param excelSheetPath     - Path of excel sheet
     * @param sheet              - Excel sheet
     * @param filterCellValue    - value which is being filtered
     * @param cellNumber         - cell number
     * @param filterColumnNumber - Column where filter will be applied
     * @return string validationCellValues
     */
    public List<String> applyFilterAndStoreCellValues(String excelSheetPath, String sheet, String filterCellValue, int filterColumnNumber, int cellNumber) {
        List<String> validationCellValues = new ArrayList<>();
        String cellValue;
        String cellAnotherValue;
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(excelSheetPath))) {
            qualidexBuilder.setWorkbook(WorkbookFactory.create(bis));
            qualidexBuilder.setSheet(qualidexBuilder.getWorkbook().getSheet(sheet));
            int noOfRows = qualidexBuilder.getSheet().getLastRowNum();
            for (int i = 1; i <= noOfRows; i++) {
                qualidexBuilder.setValue(qualidexBuilder.getSheet().getRow(i).getCell(filterColumnNumber));
                cellValue = qualidexBuilder.getValue().toString();
                if (cellValue.equals(filterCellValue)) {
                    qualidexBuilder.setValue(qualidexBuilder.getSheet().getRow(i).getCell(cellNumber));
                    cellAnotherValue = qualidexBuilder.getValue().toString();
                    logger.info(cellAnotherValue);
                    byte[] bytes = cellAnotherValue.getBytes(StandardCharsets.UTF_8);
                    String cellValues = new String(bytes);
                    validationCellValues.add(cellValues);
                }
            }
        } catch (EncryptedDocumentException | InvalidFormatException | IOException e) {
            e.printStackTrace();
        }
        return validationCellValues;
    }

    /**
     * This method verifies the value in the Header
     *
     * @param value - String value which is being verifie
     * @return - Boolean True or False
     */
    @SuppressWarnings("unused")
    public boolean existsInHeader(String value) {
        boolean found = false;
        try (BufferedReader br = new BufferedReader(new FileReader(qualidexBuilder.getPdfToText()))) {
            String st;
            // Condition holds true till
            // there is character in a string
            while ((st = br.readLine()) != null) {
                String regexPattern = (qualidexBuilder.getHeaderCoords());
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
                            asserter.validateTrue(st.contains(result), "Value " + result + " is not Present in Header " + st);
                            found = false;
                        }
                    }
                    break;

                }

            }
            if ((st = br.readLine()) == null) {
                logger.info("Header not found");
                asserter.validateTrue((st = br.readLine()) != null, "Header not found");
                found = false;

            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return found;
    }

    /**
     * This method finds the given string values in Pdf
     *
     * @param value - Text Value which being compared against PDF
     * @return boolean result
     */

    public boolean findValuesInPdf(String value) {
        boolean result = false;
        try {
            String st1 = value;
            logger.info("Validation text is : " + st1);
            {
                BufferedReader br = new BufferedReader(new StringReader(qualidexBuilder.getPdfToText()));
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
    public boolean existsInFooter(String value) {
        boolean found = false;
        try (BufferedReader br = new BufferedReader(new FileReader(qualidexBuilder.getPdfToText()))) {
            String st;
            // Condition holds true till there is character in a string
            while ((st = br.readLine()) != null) {
                String regexPattern = (qualidexBuilder.getFooterCoords());
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
                            asserter.validateTrue(found, "Value " + result + " is not Present in footer " + st);
                            found = false;
                        }
                    }
                    break;

                }

                if ((st = br.readLine()) == null) {
                    logger.info("Footer not found");
                    asserter.validateTrue((st = br.readLine()) != null, "Footer not found");
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
     * @return - Boolean True or False
     */
    @SuppressWarnings("unused")
    public boolean isEmptyHeader() {
        boolean found = false;
        try (BufferedReader br = new BufferedReader(new FileReader(qualidexBuilder.getPdfToText()))) {
            String st;
            // Condition holds true till
            // there is character in a string
            while ((st = br.readLine()) != null) {
                {
                    String regexPattern = (qualidexBuilder.getHeaderCoords());
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
                asserter.validateTrue((st = br.readLine()) != null, "Header not found");

            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return found;
    }

    /**
     * This method verifies whether footer is empty or not
     *
     * @return - Boolean True or False
     */

    @SuppressWarnings("unused")
    public boolean isEmptyFooter() {
        boolean found = false;
        try (BufferedReader br = new BufferedReader(new FileReader(qualidexBuilder.getPdfToText()))) {
            String st;
            // Condition holds true till
            // there is character in a string
            while ((st = br.readLine()) != null) {

                {
                    String regexPattern = (qualidexBuilder.getFooterCoords());
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
                asserter.validateTrue((st = br.readLine()) != null, "Footer not found");

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
    public int returnNumberOfPages(String PDF) {
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
     * This method extract PDF data into text file
     */
    private String pdfExtracter() {
        String PdfToText = null;
        try {
            Process process = Runtime.getRuntime().exec("cmd /c start /wait java -Xmx2448m -cp  . pdf.PDFTextExtractor " + qualidexBuilder.getPdfPath(), null, new File(".\\pdtextcreater"));
            process.waitFor();
            PdfToText = TextUTF16LEToTextUTF8("C:\\PDF\\pdtxtoutput.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return PdfToText;

    }

    /**
     * This method converts text extracted from PDF which is in UTF-16LE to text
     * UTF-8 format
     *
     * @param pdtxtOutput - Pdf to text .txt file
     */

    private String TextUTF16LEToTextUTF8(String pdtxtOutput) throws FileNotFoundException {
        String cellValues = null;
        FileInputStream fis = new FileInputStream(pdtxtOutput);
        try (BufferedReader in = new BufferedReader(new InputStreamReader(fis, StandardCharsets.UTF_16LE))) {
            StringBuilder stringBuilder = new StringBuilder();
            String line = null;
            String ls = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }
            String content = stringBuilder.toString();
            byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
            cellValues = new String(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cellValues;
    }

    /**
     * This method finds the occurrence of given string value in PDF
     *
     * @param value - Text which is being validated
     * @return - count of string in PDF
     */
    @SuppressWarnings("resource")
    public int findOccurence(String value) {
        int count = 0;
        try {
            logger.info("Validation text is : " + value);
            Pattern pattern = Pattern.compile(value);
            Matcher matcher = pattern.matcher(qualidexBuilder.getPdfToText());
            while (matcher.find()) {
                count++;
            }
            if (count > 0) {
                logger.info("Validated text " + value + " appears " + count + " times in a PDF");
            } else {
                logger.info("Text" + value + " not found in PDF");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;

    }

    /**
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
        } catch (SQLException ex) {
            logger.info(ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * This method verifies whether text is present in PDF
     *
     * @param pdfpath - Path of Pdf
     * @return - Boolean True or False
     */

    public boolean isPdfEmpty(String pdfpath) {
        boolean found = false;
        try {
            // Fetch the PDF
            File pdf_file = new File(pdfpath);
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
     * @return - boolean result
     */
    public boolean findImage(String PDFPath, String refImagePath) {
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
    private void imageExtraction(String PDF) throws IOException {
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
                    ImageIO.write(((org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject) o).getImage(), "png", file);

                }
            }
        }
    }

    /**
     * This method finds the reference image with the PDF
     *
     * @param imageList - List image extracted from PDF
     * @param value     - value "true"
     * @return - Boolean true or false
     */
    private boolean searchImage(String imageList, String value) {
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
    private boolean processImage(File imageFiles, String refImage) throws IOException {
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
     * This method finds the given string value in PDF
     *
     * @param value - Text which is being searched
     * @return boolean - bolean True or false
     */
    public boolean findText(String value) {
        boolean found = false;
        try {
            logger.info("Validation text is : " + value);
            BufferedReader br = new BufferedReader(new StringReader(qualidexBuilder.getPdfToText()));
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
                    logger.info("Found the match: " + matcher.group() + " is starting at index " + matcher.start() + "  and ending at index " + matcher.end());
                    break;
                }
            }
            if ((st = br.readLine()) == null) {
                found = false;
                logger.info("match not found");
            }
            br.close();

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

    public boolean findWithFormat(String value, String fontStyle, String fontSize) {
        boolean found = false;
        String stringLine = null;
        try {
            String st;
            BufferedReader extractedBr = new BufferedReader(new StringReader(qualidexBuilder.getPdfToText()));
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
                } else {
                    continue;
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

    private void htmlFileCreation() {
        try {
            File htmlFile = new File(".\\Difference.html");
            if (htmlFile.createNewFile()) {
                asserter.validateTrue(htmlFile.createNewFile());
            }
        } catch (IOException e) {
            logger.info(e.getMessage());
        }
    }

    private void earlyDiffStateForDelete(final List<String> toBeRemoved, final LinkedList<diff_match_patch.Diff> diffFinalReport) {
        // in this case it means string is deleted
        int loopCount = 0;
        for (String removeString : toBeRemoved) {
            if (qualidexBuilder.getTextInEarlierDiff().contains(removeString)) {
                if (loopCount > 0) {
                    qualidexBuilder.getRegexForSplit().append("|").append("(");
                }
                qualidexBuilder.getRegexForSplit().append("(?<=").append(removeString).append(")|(?=").append(removeString).append(")").append(")");
                loopCount++;
            }
        }
        if (loopCount > 0) {
            String[] earlyDiffTexts = qualidexBuilder.getTextInEarlierDiff().split(qualidexBuilder.getRegexForSplit().toString());
            for (String earlyDiffText : earlyDiffTexts) {
                if (toBeRemoved.contains(earlyDiffText)) {
                    diffFinalReport.add(new Diff(Operation.EQUAL, earlyDiffText));
                } else {
                    diffFinalReport.add(new Diff(Operation.DELETE, earlyDiffText));
                }
            }
        } else {
            diffFinalReport.add(new Diff(Operation.DELETE, qualidexBuilder.getTextInEarlierDiff()));
        }

    }

    private void earlyDiffStateForEqual(List<String> newlyAddedString, LinkedList<diff_match_patch.Diff> diffFinalReport) {
        int loopCount = 0;
        for (String newString : newlyAddedString) {
            if (qualidexBuilder.getPdfDiff().text.contains(newString)) {
                if (loopCount > 0) {
                    qualidexBuilder.getRegexForSplit().append("|").append("(");
                }
                qualidexBuilder.getRegexForSplit().append("(?<=").append(newString).append(")|(?=").append(newString).append(")").append(")");
                loopCount++;
            }
        }
        if (loopCount > 0) {
            String[] pdfDifferences = qualidexBuilder.getPdfDiff().text.split(qualidexBuilder.getRegexForSplit().toString());
            for (String pdfDifference : pdfDifferences) {
                if (newlyAddedString.contains(pdfDifference)) {
                    diffFinalReport.add(new Diff(Operation.EQUAL, pdfDifference));
                } else {
                    diffFinalReport.add(new Diff(Operation.INSERT, pdfDifference));
                }
            }
        } else {
            diffFinalReport.add(new Diff(Operation.INSERT, qualidexBuilder.getPdfDiff().text));
        }
    }

    private void baseModifyValues(int loopCount, StringBuilder regexForSplitModified, List<String> baseString, Map<String, String> diff, LinkedList<diff_match_patch.Diff> diffFinalReport) {
        if (loopCount > 0) {
            String[] baseValues = qualidexBuilder.getTextInEarlierDiff().split(qualidexBuilder.getRegexForSplit().toString());
            String[] modifiedValue = qualidexBuilder.getPdfDiff().text.split(regexForSplitModified.toString());
            int q = 0;
            for (String baseValue : baseValues) {
                if (baseString.contains(baseValue)) {
                    diffFinalReport.add(new Diff(Operation.EQUAL, diff.get(baseValue)));
                } else {
                    diffFinalReport.add(new Diff(Operation.DELETE, baseValue));
                    diffFinalReport.add(new Diff(Operation.INSERT, modifiedValue[q]));
                }
                q++;
            }
        } else {
            diffFinalReport.add(new Diff(Operation.DELETE, qualidexBuilder.getTextInEarlierDiff()));
            diffFinalReport.add(qualidexBuilder.getPdfDiff());
        }
    }

    private void earlyDiffStateForDeleteInInsert(Map<String, String> diff, LinkedList<diff_match_patch.Diff> diffFinalReport) {
        StringBuilder regexForSplitModified = new StringBuilder("(");
        int loopCount = 0;
        if (diff == null || diff.isEmpty()) {
            diffFinalReport.add(new Diff(Operation.DELETE, qualidexBuilder.getTextInEarlierDiff()));
            diffFinalReport.add(qualidexBuilder.getPdfDiff());
        } else {
            List<String> baseString = new ArrayList<String>();
            Set<String> keys = diff.keySet();
            for (String k : keys) {
                baseString.add(k);
                System.out.println("key: " + k);
            }
            for (String s : baseString) {
                if (qualidexBuilder.getTextInEarlierDiff().contains(s)) {
                    if (qualidexBuilder.getPdfDiff().text.contains(diff.get(s))) {
                        if (loopCount > 0) {
                            qualidexBuilder.getRegexForSplit().append("|(");
                            regexForSplitModified.append("|(");
                        }
                        qualidexBuilder.getRegexForSplit().append("(?<=").append(s).append(")|(?=").append(s).append("))");
                        regexForSplitModified.append("(?<=").append(diff.get(s)).append(")|(?=").append(diff.get(s)).append("))");
                        loopCount++;
                    }
                }
            }
            baseModifyValues(loopCount, regexForSplitModified, baseString, diff, diffFinalReport);
        }
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

    private String qualiDexCompare(final String basePDF, final String refPDF, final Map<String, String> diff, List<String> toBeRemoved, final List<String> newlyAdded) {
        final String EQUAL = "EQUAL";
        final String INSERT = "INSERT";
        final String DELETE = "DELETE";
        final String PDFText1;
        final String PDFText2;
        try {
            PDFText1 = PDF_Extractor(basePDF);
            PDFText2 = PDF_Extractor(refPDF);
            htmlFileCreation();
            diff_match_patch dmp = new diff_match_patch();
            LinkedList<diff_match_patch.Diff> pdfDifferences = dmp.diff_main(PDFText1, PDFText2);
            dmp.diff_cleanupEfficiency(pdfDifferences);
            LinkedList<diff_match_patch.Diff> diffFinalReport = new LinkedList<>();

            for (int i = 0; i < pdfDifferences.size(); i++) {
                qualidexBuilder.setPdfDiff(pdfDifferences.get(i));
                System.out.println("Printing pdfDifferences: " + qualidexBuilder.getPdfDiff().text);
                String pdfOperations = qualidexBuilder.getPdfDiff().toString().replace(qualidexBuilder.getPdfDiff().text, "");
                if (pdfOperations.contains(EQUAL)) {
                    qualidexBuilder.setPdfOperation(EQUAL);
                } else if (pdfOperations.contains(INSERT)) {
                    qualidexBuilder.setPdfOperation(INSERT);
                } else {
                    qualidexBuilder.setPdfOperation(DELETE);
                }
                switch (qualidexBuilder.getPdfOperation()) {
                    case EQUAL:
                        System.out.println(" ");
                        if (qualidexBuilder.getEarlierDiffState().equals(DELETE)) {
                            earlyDiffStateForDelete(toBeRemoved, diffFinalReport);
                        }
                        diffFinalReport.add(qualidexBuilder.getPdfDiff());
                        qualidexBuilder.setEarlierDiffState(EQUAL);
                        break;
                    case DELETE:
                        System.out.println(" ");
                        qualidexBuilder.setEarlierDiffState(DELETE);
                        break;
                    case INSERT:
                        switch (qualidexBuilder.getEarlierDiffState()) {
                            case EQUAL:
                                earlyDiffStateForEqual(newlyAdded, diffFinalReport);
                                break;
                            case DELETE:
                                earlyDiffStateForDeleteInInsert(diff, diffFinalReport);
                                diffFinalReport.add(new Diff(Operation.DELETE, qualidexBuilder.getTextInEarlierDiff()));
                                break;
                            default:
                                logger.info("EarlierDiffState operation is not matched");
                        }
                        System.out.println(" ");
                        qualidexBuilder.setEarlierDiffState(INSERT);
                        break;
                    default:
                        logger.info("PDFOperation operation is not matched");
                }
                qualidexBuilder.setTextInEarlierDiff(qualidexBuilder.getPdfDiff().text);
            }

            String result = dmp.diff_prettyHtml(diffFinalReport);
            Path path = Paths.get(".\\Difference.html");
            // with path , content & standard charsets
            Files.write(path, Collections.singleton(result.replace("&para;", "")), StandardCharsets.UTF_8);
            File f = new File("Difference.html");
            // Display PDF difference in HTML format
            Desktop.getDesktop().browse(f.toURI());
        } catch (IOException e) {
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

    public String qualiDexCompares(String basePDF, String refPDF, Map<String, String> diff, List<String> toBeRemoved, List<String> newlyAdded) {
        return qualiDexCompare(basePDF, refPDF, diff, toBeRemoved, newlyAdded);
    }

    /**
     * This method highlights Pdf differences by comparing base PDF vs Ref PDF
     *
     * @param basePDF Base PDF file path
     * @param refPDF  ref. PDF file path
     * @return returns PDF comparison
     */

    public String qualiDexCompares(String basePDF, String refPDF) {
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

    public String qualiDexCompares(String basePDF, String refPDF, Map<String, String> diff) {
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

    public String qualiDexCompares(String basePDF, String refPDF, List<String> newlyAdded) {
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

    public String qualiDexCompares(String basePDF, String refPDF, List<String> toBeRemoved, List<String> newlyAdded) {
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

    private String PDF_Extractor(String PDF) throws IOException {
        File pdf_file = new File(PDF);
        PDDocument document = PDDocument.load(pdf_file);
        PDFTextStripper pdfstripper = new PDFTextStripper();
        String text = pdfstripper.getText(document);
        return text;
    }
}
