# File Generator API

## Description
This Spring Boot project is a RESTful API for exporting files, designed for study purposes. Data can be exported in PDF, CSV, Word, and Excel formats. As the focus is solely on data export testing, the CRUD is only used to populate the database, with no major attention to exception handling or field validation.

## Features
- **PDF Export**;
- **CSV Export**;
- **Word Export**;
- **Excel Export**.

## Technologies Used
- **Spring Boot**: For building the RESTful API.
- **Spring Data JPA**: For data manipulation with the database.
- **Apache POI**: For generating Word and Excel files.
- **iText**: For generating PDF files.
- **H2 Database**: Used for testing and development.

## How to Use
To run the project locally:
1. Clone the repository;
2. Set up your preferred database in `application.properties` (H2 database will be used by default);
3. Run the project via Spring Boot;
4. Populate the table;
5. Access the available endpoints to export data in the desired formats.

## Endpoints
- `/example/export/pdf` - Exports data to PDF.
- `/example/export/csv` - Exports data to CSV.
- `/example/export/docx` - Exports data to Word.
- `/example/export/xlsx` - Exports data to Excel.
