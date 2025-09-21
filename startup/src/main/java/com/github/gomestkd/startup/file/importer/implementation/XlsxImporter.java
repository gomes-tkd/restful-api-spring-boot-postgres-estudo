package com.github.gomestkd.startup.file.importer.implementation;

import com.github.gomestkd.startup.data.dto.PersonDTO;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.github.gomestkd.startup.file.importer.contract.FileImporter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class XlsxImporter implements FileImporter {
    private static final Logger logger = LoggerFactory.getLogger(XlsxImporter.class);

    @Override
    public Set<PersonDTO> importFile(InputStream inputStream) throws IOException {
        logger.debug("Starting XLSX file import process. Returning a Set.");

        try (XSSFWorkbook workbook = new XSSFWorkbook(inputStream)) {
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            if (rowIterator.hasNext()) {
                rowIterator.next();
                logger.debug("Header row skipped.");
            }

            return parseRowsToPersonDtoSet(rowIterator);

        } catch (IOException e) {
            logger.error("Failed to read or parse the XLSX file.", e);
            throw e;
        }
    }

    private Set<PersonDTO> parseRowsToPersonDtoSet(Iterator<Row> rowIterator) {
        Set<PersonDTO> uniquePeople = new HashSet<>();
        DataFormatter dataFormatter = new DataFormatter();

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if (isRowValid(row)) {
                uniquePeople.add(parseRowToPersonDto(row, dataFormatter));
            }
        }

        logger.info("Successfully parsed {} unique records from the XLSX file.", uniquePeople.size());

        return uniquePeople;
    }

    private PersonDTO parseRowToPersonDto(Row row, DataFormatter dataFormatter) {
        PersonDTO person = new PersonDTO();
        person.setFirstName(getSafeStringCellValue(row.getCell(0), dataFormatter));
        person.setLastName(getSafeStringCellValue(row.getCell(1), dataFormatter));
        person.setAddress(getSafeStringCellValue(row.getCell(2), dataFormatter));
        person.setGender(getSafeStringCellValue(row.getCell(3), dataFormatter));
        person.setEnabled(true);
        return person;
    }

    private boolean isRowValid(Row row) {
        Cell firstCell = row.getCell(0);
        return firstCell != null && !firstCell.toString().trim().isEmpty();
    }

    private String getSafeStringCellValue(Cell cell, DataFormatter dataFormatter) {
        if (cell == null) {
            return "";
        }
        return dataFormatter.formatCellValue(cell).trim();
    }
}
