package com.github.gomestkd.startup.file.exporter.implementation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Set;

import com.github.gomestkd.startup.data.dto.PersonDTO;
import com.github.gomestkd.startup.file.exporter.contract.PersonExporter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class XlsxExporter implements PersonExporter {

    private static final Logger logger = LoggerFactory.getLogger(XlsxExporter.class);

    @Override
    public Resource exportPeople(Set<PersonDTO> people) throws IOException {
        logger.debug("Starting XLSX export for {} person(s).", people.size());

        // O try-with-resources garante que o Workbook seja fechado corretamente.
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("People");

            // Criação do cabeçalho
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "First Name", "Last Name", "Address", "Gender", "Enabled"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(createHeaderCellStyle(workbook));
            }

            // Preenchimento dos dados
            int rowIndex = 1;
            for (PersonDTO person : people) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(person.getId());
                row.createCell(1).setCellValue(person.getFirstName());
                row.createCell(2).setCellValue(person.getLastName());
                row.createCell(3).setCellValue(person.getAddress());
                row.createCell(4).setCellValue(person.getGender());
                row.createCell(5).setCellValue(
                        person.getEnabled() != null && person.getEnabled() ? "Yes" : "No");
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Escrita dos dados em memória
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                workbook.write(outputStream);
                byte[] xlsxBytes = outputStream.toByteArray();
                logger.info("XLSX export completed successfully. File size: {} bytes.", xlsxBytes.length);
                return new ByteArrayResource(xlsxBytes);
            }
        } catch (IOException e) {
            logger.error("Failed to write data to XLSX workbook.", e);
            // Lança a exceção novamente após o log, para que a camada superior saiba do erro.
            throw e;
        }
    }

    private CellStyle createHeaderCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    @Override
    public Resource exportPerson(PersonDTO person) {
        logger.warn("Method 'exportPerson' was called, but it is not implemented.");
        return null;
    }
}