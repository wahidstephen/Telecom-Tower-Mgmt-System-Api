package com.tmanagement.view;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.tmanagement.model.Complaint;

public class PdfView extends AbstractPdfView {
    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // change the file name
        response.setHeader("Content-Disposition", "attachment; filename=\"admin-report.pdf\"");

        List<Complaint> complaints = (List<Complaint>) model.get("complaint");
        document.add(new Paragraph("Generated on " + LocalDateTime.now()));
        document.add(new Paragraph("Complaint Report of Tower ID: " + complaints.get(0).getTower().getTowerId()));
        document.add(new Paragraph("Tower Circle: " + complaints.get(0).tower.getCircle()));
        document.add(new Paragraph("Tower Status: " + ((complaints.get(0).tower.isStatus())?"Active":"Under Maintainence")));
        PdfPTable table = new PdfPTable(6/*complaints.stream().findAny().get().getColumnCount()*/);
        table.setWidthPercentage(100.0f);
        table.setSpacingBefore(10);

        // define font for table header row
        Font font = FontFactory.getFont(FontFactory.TIMES);
        font.setColor(BaseColor.WHITE);

        // define table header cell
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(BaseColor.DARK_GRAY);
        cell.setPadding(5);

        // write table header
        cell.setPhrase(new Phrase("Complaint ID", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Filed By", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Complaint Type", font));
        table.addCell(cell);
        
        cell.setPhrase(new Phrase("Remarks", font));
        table.addCell(cell);
        
        cell.setPhrase(new Phrase("Issue Date", font));
        table.addCell(cell);
        
        cell.setPhrase(new Phrase("Approval Date", font));
        table.addCell(cell);

        for(Complaint complaint : complaints){
            table.addCell(((Integer)complaint.getComplaintId()).toString());
            table.addCell(complaint.getCsa().getName());
            table.addCell(complaint.getType().toString());
            table.addCell(complaint.getDescription());
            table.addCell(complaint.getDateOfIssue().toString());
            table.addCell(complaint.getDateOfApproval().toString());
        }

        document.add(table);
    }
}