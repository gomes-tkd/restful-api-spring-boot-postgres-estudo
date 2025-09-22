package com.github.gomestkd.startup.file.importer.factory;

import com.github.gomestkd.startup.exception.BadRequestException;
import com.github.gomestkd.startup.file.importer.contract.FileImporter;
import com.github.gomestkd.startup.file.importer.implementation.CsvImporter;
import com.github.gomestkd.startup.file.importer.implementation.XlsxImporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class FileImporterFactory {

    private final Logger logger = LoggerFactory.getLogger(FileImporterFactory.class);

    @Autowired
    private final ApplicationContext applicationContext;

    public FileImporterFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public FileImporter getImporter(String fileName) {
        logger.debug("Attempting to find importer for file: '{}'", fileName);

        if (fileName.toLowerCase().endsWith(".xlsx")) {
            logger.info("XLSX importer selected for file: '{}'", fileName);
            return applicationContext.getBean(XlsxImporter.class);

        } else if (fileName.toLowerCase().endsWith(".csv")) {
            logger.info("CSV importer selected for file: '{}'", fileName);
            return applicationContext.getBean(CsvImporter.class);

        } else {
            logger.warn("No suitable importer found for file: '{}'. Throwing BadRequestException.", fileName);
            throw new BadRequestException("Invalid File Format!");
        }
    }
}