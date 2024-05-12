package com.api.fileexporter.service;

import com.api.fileexporter.dto.ExportDataDTO;
import com.api.fileexporter.entities.ExportData;
import com.api.fileexporter.repository.ExportDataRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Service
public class ExportDataService {

    private ExportDataRepository exportDataRepository;

    @Autowired
    public ExportDataService(ExportDataRepository exportDataRepository) {
        this.exportDataRepository = exportDataRepository;
    }

    public ExportDataDTO save(ExportDataDTO exportDataDTO) {
        ExportData exportData = toEntity(exportDataDTO);
        exportData = exportDataRepository.save(exportData);
        return toDTO(exportData);
    }

    public Page<ExportDataDTO> findAll(Pageable pageable) {
        Page<ExportData> exportData = exportDataRepository.findAll(pageable);
        return exportData.map(this::toDTO);
    }

    protected List<ExportData> findAllEntities() {
        return exportDataRepository.findAll();
    }

    public ExportData toEntity(ExportDataDTO exportDataDTO) {
        return new ExportData(
            exportDataDTO.id(),
            exportDataDTO.name(),
            exportDataDTO.description(),
            exportDataDTO.date()
        );
    }

    public ExportDataDTO toDTO(ExportData exportData) {
        return new ExportDataDTO(
          exportData.getId(),
          exportData.getName(),
          exportData.getDescription(),
          exportData.getDate()
        );
    }

    public void exportDataToPDF(HttpServletResponse response) throws IOException, DocumentException {
        List<ExportData> dataList = exportDataRepository.findAll();

        response.setContentType("application/pdf");
        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        PdfPTable table = new PdfPTable(3); // Três colunas: Nome, Descrição, Data

        // Adicionando cabeçalhos
        table.addCell("Nome");
        table.addCell("Descrição");
        table.addCell("Data");

        // Configurando as células para serem cabeçalhos
        for (int i = 0; i < 3; i++) {
            PdfPCell cell = table.getRow(0).getCells()[i];
            cell.setBackgroundColor(new BaseColor(39, 85, 146));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(10);
        }

        // Adicionando dados das entidades na tabela
        for (ExportData data : dataList) {
            table.addCell(data.getName());
            table.addCell(data.getDescription());
            table.addCell(data.getDate().toString()); // Converter Date para String
        }

        document.add(table);
        document.close();
    }

    public void exportDataToCSV(HttpServletResponse response) throws IOException {
        List<ExportData> dataList = exportDataRepository.findAll();

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"export-data.csv\"");

        PrintWriter writer = response.getWriter();
        writer.println("ID,Name,Description,Date");

        for (ExportData data : dataList) {
            writer.println(String.format("%d,%s,%s,%s",
                    data.getId(),
                    data.getName(),
                    data.getDescription(),
                    data.getDate().toString()));
        }

        writer.flush();
        writer.close();
    }

    public void exportDataToWord(HttpServletResponse response) throws IOException {
        List<ExportData> dataList = exportDataRepository.findAll();

        response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        response.setHeader("Content-Disposition", "attachment; filename=\"export-data.docx\"");

        XWPFDocument document = new XWPFDocument();
        XWPFTable table = document.createTable();

        // Cabeçalho da Tabela
        XWPFTableRow header = table.getRow(0);
        header.getCell(0).setText("ID");
        header.addNewTableCell().setText("Name");
        header.addNewTableCell().setText("Description");
        header.addNewTableCell().setText("Date");

        // Adicionando dados
        for (ExportData data : dataList) {
            XWPFTableRow row = table.createRow();
            row.getCell(0).setText(data.getId().toString());
            row.getCell(1).setText(data.getName());
            row.getCell(2).setText(data.getDescription());
            row.getCell(3).setText(data.getDate().toString());
        }

        document.write(response.getOutputStream());
        document.close();
    }

    public void exportDataToExcel(HttpServletResponse response) throws IOException {
        List<ExportData> dataList = exportDataRepository.findAll();;

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=\"export-data.xlsx\"");

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data");

        // Cabeçalho
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Name");
        headerRow.createCell(2).setCellValue("Description");
        headerRow.createCell(3).setCellValue("Date");

        // Preenchendo as linhas
        int rowNum = 1;
        for (ExportData data : dataList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(data.getId());
            row.createCell(1).setCellValue(data.getName());
            row.createCell(2).setCellValue(data.getDescription());
            row.createCell(3).setCellValue(data.getDate().toString());
        }

        // Ajustando automaticamente o tamanho das colunas
        for (int i = 0; i < 4; i++) {
            sheet.autoSizeColumn(i);
        }

        workbook.write(response.getOutputStream());
        workbook.close();
    }
}
