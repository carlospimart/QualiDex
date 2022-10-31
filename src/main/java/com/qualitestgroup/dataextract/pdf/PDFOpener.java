/*
 * Copyright (c) 2017-2018. QualiTest Software Testing Limited.
 */

package main.java.com.qualitestgroup.dataextract.pdf;

import main.java.com.qualitestgroup.dataextract.utilities.FileHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.IOException;

/**
 * Simple utility to locate a PDF on the classpath and open it.
 *
 * @author Daniel Geater
 */
public class PDFOpener {
    private static final Log LOG = LogFactory.getLog(PDFOpener.class);

    private FileHelper fileHelper = new FileHelper();

    /**
     * Opens a PDF.  Attempts to locate the PDF on the classpath first, then the file system if that is unsuccessful
     *
     * @param filename filename to locate
     * @return PDDocument instance of the PDF
     * @throws IOException - if document is encrypted or has no content
     */
    public PDDocument openDocument(String filename) throws IOException {
        return openDocumentFromFile(fileHelper.openFile(filename));
    }

    /**
     * Opens a PDF from the classpath
     *
     * @param filename filename to locate
     * @return PDDocument instance of the PDF
     * @throws IOException - if document is encrypted or has no content
     */
    public PDDocument openDocumentFromClasspath(String filename) throws IOException {

        return openDocumentFromFile(fileHelper.openFileFromClassPath(filename));
    }

    /**
     * Opens a PDF from the Filepath
     *
     * @param filename filename to locate
     * @return PDDocument instance of the PDF
     * @throws IOException - if document is encrypted or has no content
     */
    public PDDocument openDocumentFromFilepath(String filename) throws IOException {

        return openDocumentFromFile(fileHelper.openFileFromFilesystem(filename));
    }

    /**
     * Opens a PDF from the passed in file and return a PDocument handle on it
     *
     * @param pdf - handle on the PDF File to load
     * @return PDDocument instance of the PDF
     * @throws IOException - if document is encrypted or has no content
     */
    public PDDocument openDocumentFromFile(File pdf) throws IOException {

        if (pdf == null) {
            throw new IllegalArgumentException("null pdf requested for opening.");
        }
        LOG.info("Loading PDF content from file: " + pdf.getName());
        PDDocument document = PDDocument.load(pdf);
        if (document.isEncrypted()) {
            throw new IOException("Encrypted documents are not supported for this opener");
        }
        if (document.getNumberOfPages() < 1) {
            throw new IOException("Error: A PDF document must have at least one page, " +
                    "cannot remove the last page!");
        }
        LOG.info("Load completed for file: " + pdf.getName() + ".");
        return document;
    }
}
