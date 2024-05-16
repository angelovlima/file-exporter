package com.api.fileexporter.controller;

import com.api.fileexporter.dto.ExampleDTO;
import com.api.fileexporter.service.ExampleService;
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
@RequestMapping("/example")
public class ExampleController {

    private final ExampleService exampleService;

    @Autowired
    public ExampleController(ExampleService exampleService)
    {
        this.exampleService = exampleService;
    }

    @PostMapping
    public ResponseEntity<ExampleDTO> save(@RequestBody ExampleDTO exampleDTO) {
        ExampleDTO exampleDTOSaved = exampleService.save(exampleDTO);
        return new ResponseEntity<>(exampleDTOSaved, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<ExampleDTO>> findAll(
            @PageableDefault(size = 10, page = 0, sort="name") Pageable pageable
    ) {
        Page<ExampleDTO> exampleDTOPage = exampleService.findAll(pageable);
        return ResponseEntity.ok(exampleDTOPage);
    }

    @GetMapping("/export/pdf")
    @ResponseStatus(HttpStatus.OK)
    public void exportPDF(HttpServletResponse response) throws IOException, DocumentException {
        exampleService.exportPDF(response);

    }

    @GetMapping("/export/csv")
    @ResponseStatus(HttpStatus.OK)
    public void exportCSV(HttpServletResponse response) throws IOException {
        exampleService.exportCSV(response);
    }

    @GetMapping("/export/docx")
    @ResponseStatus(HttpStatus.OK)
    public void exportWord(HttpServletResponse response) throws IOException {
        exampleService.exportWord(response);
    }

    @GetMapping("/export/xlsx")
    @ResponseStatus(HttpStatus.OK)
    public void exportExcel(HttpServletResponse response) throws IOException {
        exampleService.exportExcel(response);
    }
}
