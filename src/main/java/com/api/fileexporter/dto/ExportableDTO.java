package com.api.fileexporter.dto;

import java.util.List;

public interface ExportableDTO {
    List<String> getHeaders();
    List<String> getData();
}
