package com.api.fileexporter.service;

import com.api.fileexporter.dto.ExportableDTO;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class ExportFileService {

    private static final Map<String, String> ICON_PATHS = Map.of(
            "CHECK", "/static/images/check-icon.png",
            "CAUTION", "/static/images/caution-icon.png",
            "WARNING", "/static/images/warning-icon.png"
    );

    public void exportPDF(HttpServletResponse response, List<? extends ExportableDTO> dtoList) throws IOException, DocumentException {
        if (dtoList == null || dtoList.isEmpty()) {
            throw new IllegalArgumentException("Data list cannot be null or empty");
        }

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"export-data.pdf\"");

        // Define o documento como paisagem e adiciona margens
        Document document = new Document(PageSize.A4.rotate(), 36, 36, 36, 36);
        PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());

        // Adiciona o evento de página para cabeçalho e numeração
        PageNumberEvent headerFooterEvent = new PageNumberEvent();
        writer.setPageEvent(headerFooterEvent);

        document.open();

        // Adiciona o banner no PDF
        addBannerToPDF(document, writer, "/static/images/banner.png");

        // Adiciona um espaço entre o banner e a tabela
        addSpaceAfterBanner(document, 55); // Aumenta o espaço para 100

        ExportableDTO sampleDTO = dtoList.get(0); // Use the first DTO to get headers
        List<String> headers = sampleDTO.getHeaders();
        PdfPTable table = new PdfPTable(headers.size());
        table.setWidthPercentage(100);

        // Add headers to table
        for (String header : headers) {
            Phrase headerPhrase = new Phrase(header, FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD, Color.WHITE));
            PdfPCell headerCell = new PdfPCell(headerPhrase);
            headerCell.setBackgroundColor(new Color(39, 85, 146));
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell.setPadding(10);
            table.addCell(headerCell);
        }
        table.setHeaderRows(1); // Define que a primeira linha será repetida em cada nova página

        // Add data to table
        for (ExportableDTO dto : dtoList) {
            List<String> data = dto.getData();
            for (String datum : data) {
                PdfPCell cell;
                if (ICON_PATHS.containsKey(datum)) {
                    cell = createIconCell(ICON_PATHS.get(datum));
                } else {
                    cell = new PdfPCell(new Phrase(datum != null ? datum : ""));
                    cell.setPadding(10); // Configura o padding da célula
                }
                table.addCell(cell);
            }
        }

        document.add(table);
        document.close();
    }

    private void addBannerToPDF(Document document, PdfWriter writer, String imagePath) throws DocumentException, IOException {
        Image banner = Image.getInstance(getClass().getResource(imagePath));
        banner.scaleAbsolute(PageSize.A4.rotate().getWidth(), 100); // Ajusta a altura conforme necessário
        banner.setAbsolutePosition(0, PageSize.A4.rotate().getHeight() - banner.getScaledHeight());
        writer.getDirectContent().addImage(banner);
        document.add(banner);
    }

    private void addSpaceAfterBanner(Document document, float space) throws DocumentException {
        // Adiciona um espaço específico após o banner
        Paragraph spaceParagraph = new Paragraph();
        spaceParagraph.setSpacingBefore(space);
        spaceParagraph.setSpacingAfter(space); // Aumenta o espaço após o banner
        document.add(spaceParagraph);
    }

    private PdfPCell createIconCell(String imagePath) throws IOException, BadElementException {
        Image img = Image.getInstance(getClass().getResource(imagePath));
        img.scaleToFit(20, 20); // Ajuste o tamanho conforme necessário
        PdfPCell cell = new PdfPCell(img);
        cell.setPadding(10); // Configura o padding da célula
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return cell;
    }
}
