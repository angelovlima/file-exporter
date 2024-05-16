package com.api.fileexporter.repository;

import com.api.fileexporter.entities.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExampleRepository extends JpaRepository<Example, Long> {

}
