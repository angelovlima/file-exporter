package com.api.fileexporter.controller;

import com.api.fileexporter.dto.ExportDataDTO;
import com.api.fileexporter.service.ExportDataService;
import com.itextpdf.text.DocumentException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/export-data")
public class ExportDataController {

    private final ExportDataService exportDataService;

    @Autowired
    public ExportDataController(
            ExportDataService exportDataService)
    {
        this.exportDataService = exportDataService;
    }

    @PostMapping
    public ResponseEntity<ExportDataDTO> save(@RequestBody ExportDataDTO exportDataDTO) {
        ExportDataDTO exportDataDTOSaved = exportDataService.save(exportDataDTO);
        return new ResponseEntity<>(exportDataDTOSaved, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<ExportDataDTO>> findAll(
            @PageableDefault(size = 10, page = 0, sort="name") Pageable pageable
    ) {
        Page<ExportDataDTO> exportDataDTOPage = exportDataService.findAll(pageable);
        return ResponseEntity.ok(exportDataDTOPage);
    }

    @GetMapping("/export/pdf")
    @ResponseStatus(HttpStatus.OK)
    public void exportData(HttpServletResponse response) throws IOException, DocumentException {
        exportDataService.exportDataToPDF(response);
    }

    @GetMapping("/export/csv")
    @ResponseStatus(HttpStatus.OK)
    public void exportDataToCSV(HttpServletResponse response) throws IOException {
        exportDataService.exportDataToCSV(response);
    }

    @GetMapping("/export/docx")
    @ResponseStatus(HttpStatus.OK)
    public void exportDataToWord(HttpServletResponse response) throws IOException {
        exportDataService.exportDataToWord(response);
    }

    @GetMapping("/export/xlsx")
    @ResponseStatus(HttpStatus.OK)
    public void exportDataToExcel(HttpServletResponse response) throws IOException {
        exportDataService.exportDataToExcel(response);
    }
}
