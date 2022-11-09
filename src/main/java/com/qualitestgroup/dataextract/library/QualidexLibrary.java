package main.java.com.qualitestgroup.dataextract.library;

import com.spire.pdf.PdfDocument;
import com.spire.pdf.utilities.PdfTable;
import com.spire.pdf.utilities.PdfTableExtractor;
import main.java.com.qualitestgroup.data_extract_demo.damoregroup.Asserter;
import main.java.com.qualitestgroup.dataextract.utilities.QualidexUtility;
import name.fraser.neil.plaintext.diff_match_patch;
import name.fraser.neil.plaintext.diff_match_patch.Diff;
import name.fraser.neil.plaintext.diff_match_patch.Operation;
import org.apache.logging.log4j.Level;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Qualidex library
 *
 * @author pavan.kumar, Ramkumar Raja, Stewart Jumbe
 */
public class QualidexLibrary {
    private static final Logger logger = LoggerFactory.getLogger(QualidexLibrary.class);
    private final QualidexBuilder qualidexBuilder = QualidexBuilder.builder().build();
    private final Asserter asserter = new Asserter();
    private final QualidexUtility qualidexUtility = new QualidexUtility();

    /**
     * This method helps to extract the table in PDF and store into excel
     *
     * @param pdfPath      - Path of PDF
     * @param sheetName    - name of the sheet
     * @param filePathName - path of the file to store
     * @param fileName     - name of the excel file
     */
    public void extractTableFromPdf(String pdfPath, String sheetName, String filePathName, String fileName) {
        try (FileOutputStream fileOut = new FileOutputStream(filePathName + fileName + "_" + qualidexUtility.getSystemTime("yyyy-dd-M HH-mm-ssSS") + ".xlsx")) {
            XSSFSheet sheet = qualidexBuilder.getMyWorkBook().createSheet(sheetName);
            PdfDocument pdf = new PdfDocument(pdfPath);
            PdfTableExtractor extractor = new PdfTableExtractor(pdf);
            //Loop through the pages in the PDF
            for (int pageIndex = 0; pageIndex < pdf.getPages().getCount(); pageIndex++) {
                //Extract tables from the current page into a PdfTable array
                PdfTable[] tableLists = extractor.extractTable(pageIndex);
                //If any tables are found
                if (tableLists != null && tableLists.length > 0) {
                    for (PdfTable table : tableLists) {
                        //Loop through the rows in the current table
                        for (int i = 0; i < table.getRowCount(); i++) {
                            //Creating a row
                            qualidexBuilder.setRowhead(sheet.createRow(i));
                            //Loop through the columns in the current table
                            for (int j = 0; j < table.getColumnCount(); j++) {
                                //Extract data from the current table cell and append to the StringBuilder
                                String text = table.getText(i, j);
                                qualidexBuilder.getRowhead().createCell(j).setCellValue(text);
                            }
                        }
                    }
                }
            }
            qualidexBuilder.getMyWorkBook().write(fileOut);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
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
    private void readFromDB(String url, String userName, String password, String tableName, String CoulmnName) {
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
     * This method set PDF and extract PDF content
     *
     * @param pdfPath - path of the pdf
     */
    public void setPDFLocationAndExtract(String pdfPath) {
        try {
            qualidexBuilder.setPdfPath(pdfPath);
            qualidexBuilder.setPdfToText(pdfExtractor(pdfPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method sets Header coordinates
     *
     * @param coords - Header coordinates
     * @return - Boolean True or False
     */
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
    private boolean setSection(String section, String fontStyle, int fontSize) {
        return false;
    }

    /**
     * This method reads the cell value from excel
     *
     * @param excelSheetPath - Path of excel sheet
     * @param sheet          - Excel Sheet
     * @param cellNumber     - cell number
     * @return cellValue
     */
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
        } catch (EncryptedDocumentException | IOException e) {
            e.printStackTrace();
        }
        return cellValue;
    }

    /**
     * This Method reads cell value from excel and returns as list of strings
     *
     * @param excelSheetPath - Path of Excel
     * @param sheetName      - Excel Sheet
     * @param columnIndex    -Column in excel sheet to analyse
     * @return string validationCellValues
     */
    public List<String> readCellValues(String excelSheetPath, String sheetName, int columnIndex) {
        List<String> validationCellValues = new ArrayList<>();
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(excelSheetPath))) {
            qualidexBuilder.setWorkbook(WorkbookFactory.create(bis));
            qualidexBuilder.setSheet(qualidexBuilder.getWorkbook().getSheet(sheetName));
            int noOfRows = qualidexBuilder.getSheet().getLastRowNum();
            for (int i = 1; i <= noOfRows; i++) {
                qualidexBuilder.setValue(qualidexBuilder.getSheet().getRow(i).getCell(columnIndex));
                String cellValue = qualidexBuilder.getValue().toString();
                byte[] bytes = cellValue.getBytes(StandardCharsets.UTF_8);
                String cellValues = new String(bytes);
                validationCellValues.add(cellValues);
            }
        } catch (EncryptedDocumentException | IOException e) {
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
        } catch (EncryptedDocumentException | IOException e) {
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
    public boolean existsInHeader(String value) {
        boolean found = false;
        String pdfText;
        try (BufferedReader br = new BufferedReader(new FileReader(qualidexBuilder.getPdfToText()))) {
            while ((pdfText = br.readLine()) != null) {
                String regexPattern = (qualidexBuilder.getHeaderCoords());
                Pattern pattern = Pattern.compile(regexPattern);
                Matcher matcher = pattern.matcher(pdfText);
                if (matcher.find()) {
                    logger.info("Header is present");
                    if (pdfText.contains(value)) {
                        logger.info("value " + value + " is present in Header " + pdfText);
                        found = true;
                    } else {
                        logger.info("Value " + value + " is not Present in Header " + pdfText);
                    }
                }
                break;
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
     * This method returns the number of pages in pdf
     *
     * @param pdfPath - Path of Pdf
     * @return count - number of pages in pdf
     */
    public int returnNumberOfPages(String pdfPath) {
        PDDocument doc = null;
        try {
            doc = PDDocument.load(new File(pdfPath));
            logger.info("Number of pages in Pdf :- " + doc.getNumberOfPages());
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert doc != null;
        return doc.getNumberOfPages();
    }

    /**
     * This method extract PDF data into text file
     */
    private String pdfExtracter() {
        String PdfToText = null;
        try {
            Process process = Runtime.getRuntime().exec("cmd /c start /wait java -Xmx2448m -cp  . pdf.PDFTextExtractor " + qualidexBuilder.getPdfPath(), null, new File(".\\pdtextcreater"));
            process.waitFor();
            PdfToText = TextUTF16LEToTextUTF8("src\\test\\resources\\output\\pdtxtoutput.txt");
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
     * @param referenceString - Text which is being validated
     * @return - count of string in PDF
     */
    @SuppressWarnings("resource")
    public int findOccurrence(String referenceString) {
        int count = 0;
        try {
            logger.info("Validation text is:{}", referenceString);
            Pattern pattern = Pattern.compile(referenceString);
            Matcher matcher = pattern.matcher(qualidexBuilder.getPdfToText());
            while (matcher.find()) {
                count++;
            }
            if (count > 0) {
                logger.info("Text {} appears {} times in a pdf", referenceString, count);
            } else {
                logger.info("Text {} is not found in pdf", referenceString);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * This method verifies whether text is present in PDF
     *
     * @param pdfPath - Path of Pdf
     * @return - Boolean True or False
     */

    public boolean isPdfEmpty(String pdfPath) {
        boolean found = false;
        try {
            found = pdfExtractor(pdfPath) != null;
            asserter.validateTrue(found, "PDF is empty");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return found;
    }

    /**
     * This method gets expected images from a specific folder and compares them to images
     * extracted from a pdf
     *
     * @param pdfImageDirectoryPath      - Directory of the pdf with images to be extracted and compared against
     *                                   expected images
     * @param expectedImageDirectoryPath - Directory of the expected images
     * @return - boolean result
     */
    public boolean findImage(String pdfImageDirectoryPath, String expectedImageDirectoryPath) {
        boolean result = false;
        Map<File, Boolean> imageResult = new HashMap<>();
        String imageExportDir = "Images\\" + qualidexUtility.getSystemTime("yyyy-dd-M HH-mm-ssSS");
        try {
            // Extract images from the PDF
            imageExtraction(pdfImageDirectoryPath, imageExportDir);
            File pdfImageDir = new File(imageExportDir);
            File expectedImageDir = new File(expectedImageDirectoryPath);
            File[] pdfDirectoryListing = pdfImageDir.listFiles();
            File[] expectedImageDirectoryListing = expectedImageDir.listFiles();
            //verifying that both directories have images present
            if (Objects.requireNonNull(pdfDirectoryListing).length != 0 && Objects.requireNonNull(expectedImageDirectoryListing).length != 0) {
                for (File expectedImage : expectedImageDirectoryListing) {
                    for (File images : pdfDirectoryListing) {
                        result = processImage(images, String.valueOf(expectedImage));
                        if (result) {
                            imageResult.put(expectedImage, true);
                        }
                    }
                }
            } else {
                logger.error("Either PDFImageDirectory or ExpectedImageDirectory is empty");
            }
            assert expectedImageDirectoryListing != null;
            if (imageResult.size() != expectedImageDirectoryListing.length) {
                for (File expectedImage : expectedImageDirectoryListing) {
                    if (!imageResult.containsKey(expectedImage)) {
                        imageResult.put(expectedImage, false);
                    }
                }
            }
            for (Map.Entry<File, Boolean> image : imageResult.entrySet()) {
                if (Boolean.TRUE.equals(image.getValue())) {
                    logger.info("Image with name {} found in PDFImageDirectory:{} ", getNameFromPath(image.getKey().toString()), pdfImageDirectoryPath);
                } else {
                    logger.error("Image with name {} not found in PDFImageDirectory: {}", getNameFromPath(image.getKey().toString()), pdfImageDirectoryPath);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return !imageResult.containsValue(false);
    }

    /**
     * This method extracts the images from PDF
     *
     * @param pdf            - PDF path
     * @param imageExportDir - The directory that images are exported to during comparison process
     * @throws IOException
     */
    private void imageExtraction(String pdf, String imageExportDir) throws IOException {
        File theDir = new File(imageExportDir);
        if (!theDir.exists()) {
            theDir.mkdirs();
        }
        PDDocument document = PDDocument.load(new File(pdf));
        int NoOfPage = document.getNumberOfPages();
        for (int page = 0; page < NoOfPage; page++) {
            PDPage pdfpage = document.getPage(page);
            int i = 1;
            PDResources pdResources = pdfpage.getResources();
            for (COSName c : pdResources.getXObjectNames()) {
                PDXObject o = pdResources.getXObject(c);
                if (o instanceof PDImageXObject) {
                    File file = new File(imageExportDir + "\\page_" + page + "_image_" + i + "_" + qualidexUtility.getSystemTime("yyyy-dd-M HH-mm-ssSS") + ".png");
                    i++;
                    ImageIO.write(((PDImageXObject) o).getImage(), "png", file);
                }
            }
        }
    }

//    /**
//     * This method finds the reference image with the PDF
//     *
//     * @param imageList - List image extracted from PDF
//     * @param value     - value "true"
//     * @return - Boolean true or false
//     */
//    private boolean searchImage(String imageList, String value) {
//        boolean result = false;
//        try {
//            String st = imageList;
//            boolean results = st.contains(value);
//            if (results == false) {
//                result = false;
//                logger.info("Image not found in PDF");
//            } else {
//                result = true;
//                logger.info("Image found in PDF");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return result;
//
//    }

    /**
     * This method process the refImage with Images in PDF
     *
     * @param imageFiles - List of Images extracted from PDF
     * @param refImage   - refernec image
     * @return Boolean true or false
     * @throws IOException
     */
    private boolean processImage(File imageFiles, String refImage) {
        boolean result = false;
        List<int[]> compareImage = new ArrayList<>();
        List<String> files = Arrays.asList(refImage, imageFiles.toString());
        try {
            for (String file : files) {
                Image image = Toolkit.getDefaultToolkit().getImage(file);
                PixelGrabber imagePixel = new PixelGrabber(image, 0, 0, -1, -1, false);
                if (imagePixel.grabPixels()) {
                    compareImage.add((int[]) imagePixel.getPixels());
                }
            }
            if (compareImage.size() == files.size()) {
                result = Arrays.equals(compareImage.get(0), compareImage.get(1));
            }
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
        return result;
    }

    /**
     * This method finds the given string value in PDF
     *
     * @param textToFind - Text which is being searched
     * @return boolean - return the boolean value
     */
    public boolean findText(String textToFind) {
        boolean found = false;
        String content;
        try (BufferedReader br = new BufferedReader(new StringReader(qualidexBuilder.getPdfToText()))) {
            while ((content = br.readLine()) != null) {
                Pattern pattern = Pattern.compile(textToFind);
                Matcher matcher = pattern.matcher(content);
                if (matcher.find()) {
                    logger.info("Found the match: {} is starting at index {} and ending at index {}", matcher.group(), matcher.start(), matcher.end());
                    found = true;
                }
            }
            if (!found) {
                logger.error("Text is not matching");
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
        int loopCount = 0;
        try {
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
        } catch (PatternSyntaxException e) {
            e.printStackTrace();
            logger.error("Please give deleted string/newly added string");
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
            keys.stream().map(baseString::add).forEach(System.out::println);
           /* for (String key : keys) {
                baseString.add(key);
                System.out.println("key: " + key);
            }*/
            baseString(baseString, loopCount, regexForSplitModified, diff);
            baseModifyValues(loopCount, regexForSplitModified, baseString, diff, diffFinalReport);
        }
    }

    private void baseString(List<String> baseString, int loopCount,
                            StringBuilder regexForSplitModified, Map<String, String> diff) {
        for (String baseStr : baseString) {
            if (qualidexBuilder.getTextInEarlierDiff().contains(baseStr)) {
                if (qualidexBuilder.getPdfDiff().text.contains(diff.get(baseStr))) {
                    if (loopCount > 0) {
                        qualidexBuilder.getRegexForSplit().append("|(");
                        regexForSplitModified.append("|(");
                    }
                    qualidexBuilder.getRegexForSplit().append("(?<=").append(baseStr).append(")|(?=").append(baseStr).append("))");
                    regexForSplitModified.append("(?<=").append(diff.get(baseStr)).append(")|(?=").append(diff.get(baseStr)).append("))");
                    loopCount++;
                }
            }
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
        LinkedList<diff_match_patch.Diff> diffFinalReport = new LinkedList<>();
        try {
            PDFText1 = pdfExtractor(basePDF);
            PDFText2 = pdfExtractor(refPDF);
            htmlFileCreation();
            diff_match_patch dmp = new diff_match_patch();
            LinkedList<diff_match_patch.Diff> pdfDifferences = dmp.diff_main(PDFText1, PDFText2);
            dmp.diff_cleanupEfficiency(pdfDifferences);
            for (int i = 0; i < pdfDifferences.size(); i++) {
                qualidexBuilder.setPdfDiff(pdfDifferences.get(i));
                logger.info("Printing PDFContent: {} : {}", qualidexBuilder.getPdfDiff().operation.name(), qualidexBuilder.getPdfDiff().text);
                switch (qualidexBuilder.getPdfDiff().operation) {
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
//            diffFinalReport.add(new Diff(Operation.DELETE, qualidexBuilder.getTextInEarlierDiff()));
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
     * @param pdf Pdf file
     * @return extracted Pdf Texts
     * @throws IOException
     */

    private String pdfExtractor(String pdf) throws IOException {
        File pdf_file = new File(pdf);
        PDDocument document = PDDocument.load(pdf_file);
        PDFTextStripper pdfstripper = new PDFTextStripper();
        String text = pdfstripper.getText(document);
        return text;
    }

    /**
     * This method extracts the name of an object given its path
     *
     * @param objectPath path to the image
     * @return image name extracted from imagePath
     */
    private String getNameFromPath(String objectPath) {
        List<String> splitString = Arrays.asList(objectPath.split("\\\\"));
        return splitString.get(splitString.size() - 1);
    }
}
