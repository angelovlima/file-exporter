package com.api.fileexporter.dto;

import java.util.Date;

public record ExportDataDTO(
        Long id,
        String name,
        String description,
        Date date
) {
}
