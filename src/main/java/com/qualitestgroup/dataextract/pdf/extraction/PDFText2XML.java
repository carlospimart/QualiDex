/*
 * Copyright (c) 2017-2018. QualiTest Software Testing Limited.
 */

package main.java.com.qualitestgroup.dataextract.pdf.extraction;

/**
 * @author Daniel Geater
 */

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * XML generating implementation derived from the {@link org.apache.pdfbox.tools.PDFText2HTML} implementation.
 */
public class PDFText2XML extends PDFTextStripper {
    private static final int INITIAL_PDF_TO_XML_BYTES = 8192;
    private final PDFText2XML.FontState fontState = new PDFText2XML.FontState();

    /**
     * Constructor.
     *
     * @throws IOException If there is an error during initialization.
     */
    public PDFText2XML() throws IOException {
        super();
        setShouldSeparateByBeads(true);
        setLineSeparator(LINE_SEPARATOR);
        setParagraphStart("<paragraph>");
        setParagraphEnd("</paragraph>" + LINE_SEPARATOR);
        setPageStart("<page>");
        setPageEnd("</page>" + LINE_SEPARATOR);
        setArticleStart(LINE_SEPARATOR);
        setArticleEnd(LINE_SEPARATOR);
    }

    /**
     * Escape some HTML characters.
     *
     * @param chars String to be escaped
     * @return returns escaped String.
     */
    private static String escape(String chars) {
        StringBuilder builder = new StringBuilder(chars.length());
        for (int i = 0; i < chars.length(); i++) {
            appendEscaped(builder, chars.charAt(i));
        }
        return builder.toString();
    }

    private static void appendEscaped(StringBuilder builder, char character) {
        // write non-ASCII as named entities
        if ((character < 32) || (character > 126)) {
            int charAsInt = character;
            builder.append("&#").append(charAsInt).append(";");
        } else {
            switch (character) {
                case 34:
                    builder.append("&quot;");
                    break;
                case 38:
                    builder.append("&amp;");
                    break;
                case 60:
                    builder.append("&lt;");
                    break;
                case 62:
                    builder.append("&gt;");
                    break;
                default:
                    builder.append(String.valueOf(character));
            }
        }
    }

    @Override
    protected void startDocument(PDDocument document) throws IOException {
        StringBuilder buf = new StringBuilder(INITIAL_PDF_TO_XML_BYTES);
        buf.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
        buf.append("<pdf>\n");
        super.writeString(buf.toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void endDocument(PDDocument document) throws IOException {
        super.writeString("</pdf>");
    }

    /**
     * Write out the article separator (div tag) with proper text direction
     * information.
     *
     * @param isLTR true if direction of text is left to right
     * @throws IOException If there is an error writing to the stream.
     */
    @Override
    protected void startArticle(boolean isLTR) throws IOException {
        if (isLTR) {
            super.writeString("<article>");
        } else {
            super.writeString("<article dir=\"RTL\">");
        }
    }

    /**
     * Write out the article separator.
     *
     * @throws IOException If there is an error writing to the stream.
     */
    @Override
    protected void endArticle() throws IOException {
        super.endArticle();
        super.writeString("</article>");
    }

    /**
     * Write a string to the output stream, maintain font state, and escape some HTML characters.
     * The font state is only preserved per word.
     *
     * @param text          The text to write to the stream.
     * @param textPositions the corresponding text positions
     * @throws IOException If there is an error writing to the stream.
     */
    @Override
    protected void writeString(String text, List<TextPosition> textPositions) throws IOException {
        super.writeString(fontState.push(text, textPositions));
    }

    /**
     * Write a string to the output stream and escape some HTML characters.
     *
     * @param chars String to be written to the stream
     * @throws IOException If there is an error writing to the stream.
     */
    @Override
    protected void writeString(String chars) throws IOException {
        super.writeString(escape(chars));
    }

    /**
     * Writes the paragraph end "&lt;/p&gt;" to the output. Furthermore, it will also clear the font state.
     * <p>
     * {@inheritDoc}
     */
    @Override
    protected void writeParagraphEnd() throws IOException {
        // do not escape HTML
        super.writeString(fontState.clear());

        super.writeParagraphEnd();
    }

    /**
     * A helper class to maintain the current font state. It's public methods will emit opening and
     * closing tags as needed, and in the correct order.
     *
     * @author Axel Dörfler
     */
    private static class FontState {
        private final List<String> stateList = new ArrayList<String>();
        private final Set<String> stateSet = new HashSet<String>();

        /**
         * Pushes new {@link TextPosition TextPositions} into the font state. The state is only
         * preserved correctly for each letter if the number of letters in <code>text</code> matches
         * the number of {@link TextPosition} objects. Otherwise, it's done once for the complete
         * array (just by looking at its first entry).
         *
         * @return A string that contains the text including tag changes caused by its font state.
         */
        public String push(String text, List<TextPosition> textPositions) {
            StringBuilder buffer = new StringBuilder();

            if (text.length() == textPositions.size()) {
                // There is a 1:1 mapping, and we can use the TextPositions directly
                for (int i = 0; i < text.length(); i++) {
                    push(buffer, text.charAt(i), textPositions.get(i));
                }
            } else if (!text.isEmpty()) {
                if (textPositions.isEmpty()) {
                    return text;
                }
                push(buffer, text.charAt(0), textPositions.get(0));
                buffer.append(escape(text.substring(1)));
            }
            return buffer.toString();
        }

        /**
         * Closes all open states.
         *
         * @return A string that contains the closing tags of all currently open states.
         */
        public String clear() {
            StringBuilder buffer = new StringBuilder();
            closeUntil(buffer, null);
            stateList.clear();
            stateSet.clear();
            return buffer.toString();
        }

        protected String push(StringBuilder buffer, char character, TextPosition textPosition) {
            appendEscaped(buffer, character);

            return buffer.toString();
        }

        private int closeUntil(StringBuilder tagsBuilder, String endTag) {
            for (int i = stateList.size(); i-- > 0; ) {
                String tag = stateList.get(i);
                tagsBuilder.append(closeTag(tag));
                if (endTag != null && tag.equals(endTag)) {
                    return i;
                }
            }
            return -1;
        }

        private String closeTag(String tag) {
            return "</" + tag + ">";
        }
    }
}

