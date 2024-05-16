package com.api.fileexporter.dto;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public record ExampleDTO(
        Long id,
        String name,
        String description,
        Date date
) implements ExportableDTO {

    @Override
    public List<String> getHeaders() {
        return Arrays.asList("Name", "Description", "Date");
    }

    @Override
    public List<String> getData() {
        return Arrays.asList(
                name,
                description,
                date.toString()
        );
    }
}
