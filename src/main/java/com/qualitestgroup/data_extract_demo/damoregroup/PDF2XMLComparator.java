package main.java.com.qualitestgroup.data_extract_demo.damoregroup;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.asserts.SoftAssert;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;



public class PDF2XMLComparator {

	private static final Log logger = LogFactory.getLog(PDF2XMLComparator.class);
	private Asserter asserter = new Asserter();
	private SoftAssert softAssertion = new SoftAssert();
	private int countValue = 0;
	private static final String PARAGRAPH = "paragraph";

	public PDF2XMLComparator(){
	}

	/**
	 * @param pdfXml
	 * @param xmlFilePath
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * Method to Parse the document content and convert it into XML file
	 */
	public void stringToXML(String pdfXml, String xmlFilePath) {
		try {
			// Parse the given input
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc1 = builder.parse(new InputSource(new StringReader(pdfXml)));
			// Write the parsed document to an XML file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc1);
			StreamResult result = new StreamResult(new File(xmlFilePath));
			transformer.transform(source, result);
		} catch (Exception e) {
			logger.error(" Exception caught, Can't convert document to XML format"+ e.getMessage());
		}
	}

	/**
	 * @param xmlFilePath1
	 * @param xmlFilePath2 Method to Compare both PDFs data by paragraphs
	 */
	public void validatePDFFileData(String xmlFilePath1, String xmlFilePath2) {
		ArrayList<String> arrLst1 = new ArrayList<String>();
		ArrayList<String> arrLst2 = new ArrayList<String>();
		try {
			arrLst1.clear();
			arrLst2.clear();
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			Document doc1 = factory.newDocumentBuilder().parse(xmlFilePath1);
			Document doc2 = factory.newDocumentBuilder().parse(xmlFilePath2);
			NodeList nodes1 = doc1.getElementsByTagName(PARAGRAPH);
			NodeList nodes2 = doc2.getElementsByTagName(PARAGRAPH);
			if (nodes1.getLength() == nodes2.getLength()) {
				logger.info("Both PDf files have same number of paragraph : First PDF has " + nodes1.getLength()
						+ " paragraphs and Second PDF has " + nodes2.getLength() + " paragraphs");
				for (int i = 0; i < nodes1.getLength(); i++) {
					String value = doc1.getElementsByTagName(PARAGRAPH).item(i).getFirstChild().getNodeValue();
					arrLst1.add(value);
				}
				for (int i = 0; i < nodes2.getLength(); i++) {
					String value = doc2.getElementsByTagName(PARAGRAPH).item(i).getFirstChild().getNodeValue();
					arrLst2.add(value);
				}
			} else {
				for (int i = 0; i < nodes1.getLength(); i++) {
					String value = doc1.getElementsByTagName(PARAGRAPH).item(i).getFirstChild().getNodeValue();
					arrLst1.add(value);
				}
				for (int i = 0; i < nodes2.getLength(); i++) {
					String value = doc2.getElementsByTagName(PARAGRAPH).item(i).getFirstChild().getNodeValue();
					arrLst2.add(value);
				}
			}
			boolean isEqual = arrLst1.equals(arrLst2);
			if (!isEqual) {
				logger.info("Both PDf files doesn't have same data ");
				getAllDataDifference(arrLst1, arrLst2);
			} else {
				logger.info("Both PDf files have same data ");
				asserter.validateTrue(true);
			}
		} catch (Exception e) {
			logger.info("Exception caught in validatePDFFileData()  " + e.getMessage());
			logger.error(" Exception caught while validating PDF files data");
		}
	}

	/**
	 * @param li1
	 * @param li2 Method to get difference of the PDFs by Paragraphs, lines and
	 *            words
	 */
	static ArrayList<Object> paraNumArrLst = new ArrayList<Object>();
	static ArrayList<Object> lineNumArrLst = new ArrayList<Object>();
	static ArrayList<Object> wordNumArrLst = new ArrayList<Object>();

	public  void getAllDataDifference(List<String> li1, List<String> li2) {

		try {
			paraNumArrLst.clear();
			lineNumArrLst.clear();
			wordNumArrLst.clear();
			int diff = 0;
			for (int i = 0; i < li1.size(); i++) {
				if (li1.get(i).equals(li2.get(i))) {
				} else {
					// Paragraph comparison
					paraNumArrLst.add("Differences found in Paragraph number " + (i + 1));
					paraNumArrLst.add("First PDF Values :- " + li1.get(i));
					paraNumArrLst.add(" and Second PDF Values :- " + li2.get(i));
					logger.info("\n" + "" + "********** Paragraph Comparison **********");
					logger.info("Paragraph number " + (i + 1) + " has different data in both the files:- ");
					logger.info("First PDF Values:- " + li1.get(i) + "\n");
					logger.info("Second PDF Values:- " + li2.get(i));
					countValue++;
					// Line by line comparison
					String[] firstPdfLine = li1.get(i).split("\n");
					String[] secondPdfLine = li2.get(i).split("\n");
					int leftLineCount = firstPdfLine.length;
					int rightLineCount = secondPdfLine.length;
					if (leftLineCount == rightLineCount) {
						for (int j = 0; j < leftLineCount; j++) {
							if (firstPdfLine[j].equals(secondPdfLine[j])) {
							} else {
								lineNumArrLst.add("</br>");
								lineNumArrLst.add("Difference -> " + (diff + 1));
								lineNumArrLst.add("Line number " + (j + 1) + " has different data in both the files ");
								lineNumArrLst.add("First PDF Values :- " + firstPdfLine[j]);
								lineNumArrLst.add(" and Second PDF Values :- " + secondPdfLine[j]);
								diff++;
								countValue++;
								logger.info("/n" + "" + "********** Line by line Comparison **********");
								logger.info("Line number " + (j + 1) + " of Paragraph " + (i + 1)
										+ " has different data in both the files:- ");
								logger.info("First PDF Values:- " + firstPdfLine[j] + "\n");
								logger.info("Second PDF Values:- " + secondPdfLine[j]);
								// Word by word comparison
								String[] firstPdfWord = firstPdfLine[j].split(" ");
								String[] secondPdfWord = secondPdfLine[j].split(" ");
								int firstPdfWordLength = firstPdfWord.length;
								int secondPdfWordLength = secondPdfWord.length;
								if (firstPdfWordLength == secondPdfWordLength) {
								} else {
									logger.info("/n" + "" + "********** Word by word Comparison **********");
									int readEnd = Math.min(firstPdfWordLength, secondPdfWordLength);
									int strPtr;
									for (strPtr = 0; strPtr < readEnd; strPtr++) {
										if (firstPdfWord[strPtr].equals(secondPdfWord[strPtr])) {
										} else {
											wordNumArrLst.add("Word difference found in : Line number " + (j + 1));
											wordNumArrLst.add(" of Paragraph " + (i + 1));
											wordNumArrLst.add("First Pdf word is -> " + firstPdfWord[strPtr]);
											wordNumArrLst.add(" and Second Pdf word is -> " + secondPdfWord[strPtr]);
											logger.info("Word difference found in : Line number " + (j + 1)
													+ " of Paragraph " + (i + 1));
											logger.info("First Pdf word is -> " + firstPdfWord[strPtr]
													+ " and Second Pdf word is -> " + secondPdfWord[strPtr]);
											logger.info("");
											countValue++;
										}
									}
								}
							}
						}
					} else {
						logger.info("Both PDFs paragraph line count differs");
					}
				}
			}
			if (countValue > 0) {
				asserter.validateTrue(countValue == 0,
						"Both PDFs paragraph line count differs please refer console logs to see failure in detail "
								+ lineNumArrLst);
			}

		} catch (Exception e) {
			logger.info("Exception caught in getAllDataDifference()  " + e.getMessage());
			logger.error(" Exception caught while getting differences of PDF files data");
		}
	}

}
