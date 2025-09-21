package com.github.gomestkd.startup.file.importer.implementation;

import com.github.gomestkd.startup.data.dto.PersonDTO;
import com.github.gomestkd.startup.file.importer.contract.FileImporter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

public class CsvImporter implements FileImporter {
    private static final Logger logger = LoggerFactory.getLogger(CsvImporter.class);

    @Override
    public Set<PersonDTO> importFile(InputStream inputStream) throws IOException {
        logger.debug("Starting CSV file import process.");

        try (InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            CSVFormat format = CSVFormat.Builder.create()
                    .setHeader() // Define que a primeira linha é o cabeçalho
                    .setSkipHeaderRecord(true) // Pula a linha do cabeçalho ao ler os registros
                    .setIgnoreEmptyLines(true)
                    .setTrim(true)
                    .build();

            Iterable<CSVRecord> records = format.parse(reader);
            return parseRecordsToPersonDTOs(records);

        } catch (IllegalArgumentException e) {
            logger.error("CSV import failed due to a missing or incorrect header column.", e);
            throw new IOException("CSV header is invalid or missing a required column.", e);
        } catch (IOException e) {
            logger.error("CSV import failed due to a file reading error.", e);
            throw e;
        }
    }

    private Set<PersonDTO> parseRecordsToPersonDTOs(Iterable<CSVRecord> records) {
        Set<PersonDTO> people = new HashSet<>();

        for (CSVRecord record : records) {
            PersonDTO person = new PersonDTO();
            person.setFirstName(record.get("first_name"));
            person.setLastName(record.get("last_name"));
            person.setAddress(record.get("address"));
            person.setGender(record.get("gender"));
            person.setEnabled(true);
            people.add(person); // Adiciona ao Set (duplicatas serão ignoradas)
        }

        logger.info("Successfully parsed {} unique records from the CSV file.", people.size());

        return people;
    }
}
