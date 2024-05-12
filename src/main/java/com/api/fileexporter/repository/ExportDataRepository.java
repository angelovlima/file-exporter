package com.api.fileexporter.repository;

import com.api.fileexporter.entities.ExportData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExportDataRepository extends JpaRepository<ExportData, Long> {

}
