# File Generator API

## Descrição
Este projeto Spring Boot oferece uma API RESTful para exportar dados de uma entidade chamada `ExportData`. Os dados podem ser exportados em formatos PDF, CSV, Word e Excel. Como o foco é unicamente testes de exportação de dados da entidade, o CRUD serve unicamente para popular o banco, não se tem grande atenção com tratativas de exceção e validação dos campos.

## Funcionalidades
- **Exportação para PDF**;
- **Exportação para CSV**;
- **Exportação para Word**;
- **Exportação para Excel**.

## Tecnologias Utilizadas
- **Spring Boot**: Para a construção da API RESTful.
- **Spring Data JPA**: Para manipulação de dados com o banco de dados.
- **Apache POI**: Para a geração de arquivos Word e Excel.
- **iText**: Para a geração de arquivos PDF.
- **H2 Database**: Utilizado para testes e desenvolvimento.

## Como Usar
Para rodar o projeto localmente:
1. Clone o repositório;
2. Configure o banco de dados de sua preferência em `application.properties` (Por padrão será utilizado o banco H2);
3. Execute o projeto através do Spring Boot;
4. Popule a tabela;
5. Acesse os endpoints disponíveis para exportar dados nos formatos desejados.

## Endpoints
- `/export-data/export/pdf` - Exporta dados para PDF.
- `/export-data/export/csv` - Exporta dados para CSV.
- `/export-data/export/docx` - Exporta dados para Word.
- `/export-data/export/xlsx` - Exporta dados para Excel.