package com.api.fileexporter.service;

import com.api.fileexporter.dto.ExportableDTO;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExportFileService {

    public void exportPDF(HttpServletResponse response, List<? extends ExportableDTO> dtoList) throws IOException, DocumentException {
        if (dtoList == null || dtoList.isEmpty()) {
            throw new IllegalArgumentException("Data list cannot be null or empty");
        }

        response.setContentType("application/pdf");
        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        ExportableDTO dtoToGetHeaderList = dtoList.get(0);
        List<String> headers = dtoToGetHeaderList.getHeaders();
        PdfPTable table = new PdfPTable(headers.size());

        for (String header : headers) {
            PdfPCell headerCell = new PdfPCell(new Phrase(header));
            headerCell.setBackgroundColor(new BaseColor(39, 85, 146));
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell.setPadding(10);
            table.addCell(headerCell);
        }

        for (ExportableDTO dto : dtoList) {
            List<String> data = dto.getData();
            for (String attributeValue : data) {
                table.addCell(attributeValue != null ? attributeValue : "");
            }
        }

        document.add(table);
        document.close();
    }

    public void exportCSV(HttpServletResponse response, List<? extends ExportableDTO> dtoList) throws IOException {
        if (dtoList == null || dtoList.isEmpty()) {
            throw new IllegalArgumentException("Data list cannot be null or empty");
        }

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"export-data.csv\"");
        PrintWriter writer = response.getWriter();

        ExportableDTO dtoToGetHeaderList = dtoList.get(0);
        List<String> headers = dtoToGetHeaderList.getHeaders();
        writer.println(String.join(",", headers));

        for (ExportableDTO dto : dtoList) {
            List<String> data = dto.getData();
            writer.println(data.stream().map(attributeValue -> attributeValue != null ? attributeValue : "").collect(Collectors.joining(",")));
        }

        writer.flush();
        writer.close();
    }

}
