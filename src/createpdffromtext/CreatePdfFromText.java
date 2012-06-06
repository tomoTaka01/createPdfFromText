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
import java.util.Iterator;
import java.util.Properties;

/**
 *
 * @author tomo
 */
public class CreatePdfFromText {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, DocumentException, IOException {
        String dir = System.getProperty("user.home");
        dir += "/NetBeansProjects/createPdfFromText/";
        writePdf("sample.pdf", dir);
        System.out.println(System.getProperty("user.home"));
    }

    private static void writePdf(String pdfName, String dir) throws FileNotFoundException, DocumentException, IOException {
        Document doc = new Document(PageSize.A4);
        PdfWriter.getInstance(doc, new FileOutputStream(pdfName));        
        doc.open();
        doc.addTitle("Networkign for Introverts");
        doc.addCreationDate();
        Font titleFont = new Font();
        titleFont.setSize(14);
        titleFont.setStyle(Font.UNDERLINE);
        Font subFont = new Font();
        subFont.setSize(12);
        subFont.setStyle(Font.BOLD);
        Font wordFont = new Font();
        wordFont.setSize(10);
        wordFont.setStyle(Font.BOLD);
        Font font = new Font();
        font.setSize(10);
//        font.setStyle(Font.BOLD);
        
        for (int i=1; i<=3; i++){
            doc.add(new Paragraph("Networking for Introverts" + i, titleFont));
            doc.add(new Paragraph("Summary" , subFont));
            FileReader inSum = new FileReader(dir + "Summary" + i + ".txt");
            BufferedReader rSum = new BufferedReader(inSum);
            String line = rSum.readLine();
            doc.add(new Paragraph(line));
            doc.add(new Paragraph("Words and Phrases", subFont));
            FileReader inWords = new FileReader(dir + "words" + i + ".txt");
            BufferedReader rWords = new BufferedReader(inWords);
            line = rWords.readLine();
            while (line != null){
                String[] vals = line.split(":");
                if (vals.length < 2){
                    break;
                }
                PdfPTable table = new PdfPTable(2);
                table.setWidths(new int[]{20,80});
                PdfPCell cell1 = new PdfPCell(new Phrase(vals[0].toString(), wordFont));
                PdfPCell cell2 = new PdfPCell(new Phrase(vals[1].toString(), font));
                table.addCell(cell1);                
                table.addCell(cell2);
                doc.add(table);
                line = rWords.readLine();
            }
        }
//        doc.add(new Paragraph("hello world"));
        doc.close();
        
    }
}
