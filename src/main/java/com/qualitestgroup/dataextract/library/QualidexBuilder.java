package main.java.com.qualitestgroup.dataextract.library;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import name.fraser.neil.plaintext.diff_match_patch;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

@Getter
@Setter
@Builder
public class QualidexBuilder {
    private Workbook workbook;
    private Sheet sheet;
    private Cell value;
    private String pdfToText;
    private String pdfPath;
    private int matchCounter =0;
    private String headerCoords;
    private String footerCoords;
    private String pdfOperation;
    @Builder.Default
    private String earlierDiffState = "";
    @Builder.Default
    private String textInEarlierDiff = "";
    private diff_match_patch.Diff pdfDiff;
    @Builder.Default
    private StringBuilder regexForSplit = new StringBuilder("(");
}
