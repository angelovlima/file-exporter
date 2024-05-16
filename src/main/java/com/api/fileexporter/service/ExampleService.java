package com.api.fileexporter.service;

import com.api.fileexporter.dto.ExampleDTO;
import com.api.fileexporter.entities.Example;
import com.api.fileexporter.repository.ExampleRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
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
import java.util.List;

@Service
public class ExampleService {

    private final ExampleRepository exampleRepository;
    private final ExportFileService exportFileService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    public ExampleService(
            ExampleRepository exampleRepository,
            ExportFileService exportFileService) {
        this.exampleRepository = exampleRepository;
        this.exportFileService = exportFileService;
    }

    public ExampleDTO save(ExampleDTO exampleDTO) {
        Example example = toEntity(exampleDTO);
        example = exampleRepository.save(example);
        return toDTO(example);
    }

    public Page<ExampleDTO> findAll(Pageable pageable) {
        Page<Example> example = exampleRepository.findAll(pageable);
        return example.map(this::toDTO);
    }

    protected List<Example> findAllEntities() {
        return exampleRepository.findAll();
    }

    public Example toEntity(ExampleDTO exampleDTO) {
        return new Example(
                exampleDTO.id(),
                exampleDTO.name(),
                exampleDTO.description(),
                exampleDTO.date()
        );
    }

    public ExampleDTO toDTO(Example example) {
        return new ExampleDTO(
                example.getId(),
                example.getName(),
                example.getDescription(),
                example.getDate()
        );
    }

    public void exportPDF(HttpServletResponse response) throws IOException, DocumentException {
        List<Example> exampleList = exampleRepository.findAll();
        List<ExampleDTO> dataList = exampleList.stream().map(this::toDTO).toList();
        exportFileService.exportPDF(response, dataList);
    }

    public void exportCSV(HttpServletResponse response) throws IOException {
        List<Example> exampleList = exampleRepository.findAll();
        List<ExampleDTO> dataList = exampleList.stream().map(this::toDTO).toList();
        exportFileService.exportCSV(response, dataList);
    }

    @Deprecated
    public void exportWord(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        response.setHeader("Content-Disposition", "attachment; filename=\"export-data.docx\"");

        try (XWPFDocument document = new XWPFDocument()) {
            XWPFTable table = document.createTable();
            // Adiciona cabeçalhos
            XWPFTableRow header = table.getRow(0);
            header.getCell(0).setText("ID");
            header.addNewTableCell().setText("Name");
            header.addNewTableCell().setText("Description");
            header.addNewTableCell().setText("Date");

            // Adiciona dados
            List<Example> dataList = exampleRepository.findAll();
            for (Example data : dataList) {
                XWPFTableRow row = table.createRow();
                row.getCell(0).setText(data.getId().toString());
                row.getCell(1).setText(data.getName());
                row.getCell(2).setText(data.getDescription());
                row.getCell(3).setText(data.getDate().toString());
            }

            document.write(response.getOutputStream());
        } catch (IOException e) {
            logger.error("Erro ao exportar dados para Word", e);
            throw new RuntimeException("Erro ao exportar dados para Word", e);
        }
    }

    @Deprecated
    public void exportExcel(HttpServletResponse response) throws IOException {
        List<Example> dataList = exampleRepository.findAll();;

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
        for (Example data : dataList) {
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
