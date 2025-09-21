package com.github.gomestkd.startup.file.exporter.implementation;

import com.github.gomestkd.startup.data.dto.PersonDTO;
import com.github.gomestkd.startup.file.exporter.contract.PersonExporter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Set;

public class CsvExporter implements PersonExporter {
    private static final Logger logger = LoggerFactory.getLogger(CsvExporter.class);

    @Override
    public Resource exportPeople(Set<PersonDTO> people) throws IOException {
        logger.debug("Starting CSV export for {} person(s).", people.size());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);

        CSVFormat csvFormat = CSVFormat.Builder.create()
                .setHeader("ID", "First Name", "Last Name", "Address", "Gender", "Enabled")
                .setSkipHeaderRecord(false)
                .build();

        try (CSVPrinter printer = new CSVPrinter(writer, csvFormat)) {
            for (PersonDTO person : people) {
                printer.printRecord(
                        person.getId(),
                        person.getFirstName(),
                        person.getLastName(),
                        person.getAddress(),
                        person.getGender(),
                        person.getEnabled()
                );
            }
        } catch (IOException e) {
            logger.error("Failed to write data to CSV stream.", e);
            throw e;
        }

        byte[] data = outputStream.toByteArray();

        logger.info("CSV export completed successfully. File size: {} bytes.", data.length);

        return new ByteArrayResource(data);
    }

    @Override
    public Resource exportPerson(PersonDTO person) throws IOException {
        logger.warn("Method 'exportPerson' was called, but it is not implemented.");
        return null;
    }
}
