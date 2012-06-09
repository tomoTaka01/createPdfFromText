/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package createpdffromtext;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.*;
import java.util.TreeMap;

/**
 *
 * @author tomo
 */
public class CreatePdfFromText {

    private enum DOC_TYPE {

        NORMAL,
        ANSWER
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, DocumentException, IOException {
        String dir = System.getProperty("user.home");
        dir += "/NetBeansProjects/createPdfFromText/";
//        writePdf("sample.pdf", dir, DOC_TYPE.NORMAL); // TODO 
        writePdf("sample.pdf", dir, DOC_TYPE.ANSWER);
        System.out.println(System.getProperty("user.home"));
    }

    private static void writePdf(String pdfName, String dir, DOC_TYPE docType) throws FileNotFoundException, DocumentException, IOException {
        Document doc = new Document(PageSize.A4);
        PdfWriter.getInstance(doc, new FileOutputStream(pdfName));
        doc.open();
        doc.addTitle("Networkign for Introverts");
        doc.addCreationDate();
        Font titleFont = new Font();
        titleFont.setSize(12);
        titleFont.setStyle(Font.UNDERLINE);
        Font subFont = new Font();
        subFont.setSize(12);
        subFont.setStyle(Font.BOLD);
        Font wordFont = new Font();
        wordFont.setSize(9);
        wordFont.setStyle(Font.BOLD);
        Font font = new Font();
        font.setSize(9);

        BufferedReader rTitle = new BufferedReader(new FileReader(dir + "title.txt"));
        String titleLine = rTitle.readLine();
        String[] titles = titleLine.split("::");
        String title = titles[0];
        int cnt = Integer.parseInt(titles[1]);
        for (int i = 1; i <= cnt; i++) {
            doc.add(new Paragraph(title + i, titleFont));
            doc.add(new Paragraph("Summary", subFont));
            FileReader inSum = new FileReader(dir + "Summary" + i + ".txt");
            BufferedReader rSum = new BufferedReader(inSum);
            String line = rSum.readLine();
            if (docType == DOC_TYPE.NORMAL) {
                doc.add(new Paragraph(line));
            } else {
                doc.add(new Paragraph(createAns(line)));
            }
            doc.add(new Paragraph("Words and Phrases", subFont));
            Font dummyFont = new Font();
            dummyFont.setSize(6);
            Phrase spaceLine = new Phrase("", dummyFont);
            doc.add(spaceLine);
            FileReader inWords = new FileReader(dir + "words" + i + ".txt");
            BufferedReader rWords = new BufferedReader(inWords);
            line = rWords.readLine();
            TreeMap<String, String> map = new TreeMap<>();
            while (line != null) {
                String[] vals = line.split("::");
                if (vals.length < 2) {
                    break;
                }
                map.put(vals[0], vals[1]);
                line = rWords.readLine();
            }
            for (String key : map.keySet()) {
                String val = map.get(key);
                PdfPTable table = new PdfPTable(2);
                table.setWidths(new int[]{20, 80});
                if (docType == DOC_TYPE.ANSWER) {
                    PdfPCell cell1 = new PdfPCell(new Phrase("", wordFont));
                    table.addCell(cell1);
                    PdfPCell cell2 = new PdfPCell(new Phrase(createAns(val.toString()), font));
                    table.addCell(cell2);
                } else {
                    PdfPCell cell1 = new PdfPCell(new Phrase(key.toString(), wordFont));
                    table.addCell(cell1);
                    PdfPCell cell2 = new PdfPCell(new Phrase(val.toString(), font));
                    table.addCell(cell2);
                }
                doc.add(table);
            }
        }
        doc.close();

    }

    private static String createAns(String val) {
        StringBuilder sb = new StringBuilder();
        boolean isSpacing = false;
        char[] tmp = val.toCharArray();
        for (char c : tmp) {
            if (c == ')') {
                isSpacing = false;
            }
            if (isSpacing) {
                sb.append(" ");
            } else {
                sb.append(c);
            }
            if (c == '(') {
                isSpacing = true;
            }
        }
        return sb.toString();
    }
}
