package com.github.gomestkd.startup.file.exporter.factory;

import com.github.gomestkd.startup.exception.BadRequestException;
import com.github.gomestkd.startup.file.exporter.MediaTypes;
import com.github.gomestkd.startup.file.exporter.contract.PersonExporter;
import com.github.gomestkd.startup.file.exporter.implementation.CsvExporter;
import com.github.gomestkd.startup.file.exporter.implementation.PdfExporter;
import com.github.gomestkd.startup.file.exporter.implementation.XlsxExporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class FileExporterFactory {

    private final Logger logger = LoggerFactory.getLogger(FileExporterFactory.class);

    @Autowired
    private final ApplicationContext applicationContext;

    public FileExporterFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public PersonExporter createPersonExporter(String acceptHeader) {
        logger.debug("Attempting to find person exporter for media type: '{}'", acceptHeader);
        if (acceptHeader.equalsIgnoreCase(MediaTypes.APPLICATION_XLSX_VALUE)) {
            logger.info("XLSX exporter selected for media type: '{}'", acceptHeader);
            return applicationContext.getBean(XlsxExporter.class);
        } else if (acceptHeader.equalsIgnoreCase(MediaTypes.APPLICATION_CSV_VALUE)) {
            logger.info("CSV exporter selected for media type: '{}'", acceptHeader);
            return applicationContext.getBean(CsvExporter.class);
        } else if (acceptHeader.equalsIgnoreCase(MediaTypes.APPLICATION_PDF_VALUE)) {
            logger.info("PDF exporter selected for media type: '{}'", acceptHeader);
            return applicationContext.getBean(PdfExporter.class);
        } else {
            logger.warn("No suitable exporter found for media type: '{}'. Throwing BadRequestException.", acceptHeader);
            throw new BadRequestException("Invalid File Format!");
        }
    }
}