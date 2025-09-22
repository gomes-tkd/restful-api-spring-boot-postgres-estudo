package com.github.gomestkd.startup.file.exporter.implementation;

import com.github.gomestkd.startup.data.dto.PersonDTO;
import com.github.gomestkd.startup.file.exporter.contract.PersonExporter;
import com.github.gomestkd.startup.services.QRCodeService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class PdfExporter implements PersonExporter {
    private static final Logger logger = LoggerFactory.getLogger(PdfExporter.class);

    private final QRCodeService qrCodeService;

    public PdfExporter(QRCodeService qrCodeService) {
        this.qrCodeService = qrCodeService;
    }

    @Override
    public Resource exportPeople(Set<PersonDTO> people) throws IOException {
        logger.debug("Starting PDF export for a list of {} people.", people.size());

        final String templatePath = "templates/people_report.jrxml";

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(templatePath)) {
            if (inputStream == null) {
                logger.error("Template file not found at path: {}", templatePath);
                throw new RuntimeException("Template file not found: " + templatePath);
            }

            JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(people);
            Map<String, Object> parameters = new HashMap<>();

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()){
                JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
                byte[] pdfBytes = outputStream.toByteArray();
                logger.info("Successfully exported PDF for {} people. File size: {} bytes.", people.size(), pdfBytes.length);
                return new ByteArrayResource(pdfBytes);
            }
        } catch (Exception e) {
            logger.error("Failed to export people list to PDF.", e);
            if (e instanceof IOException) {
                throw (IOException) e;
            }
            throw new RuntimeException("An unexpected error occurred during PDF export.", e);
        }
    }

    @Override
    public Resource exportPerson(PersonDTO person) throws IOException {
        logger.debug("Starting PDF export for a single person with ID: {}", person.getId());
        final String mainTemplatePath = "/templates/person.jrxml";
        final String subReportPath = "/templates/books.jrxml";

        try (InputStream mainTemplateStream = getClass().getResourceAsStream(mainTemplatePath);
             InputStream subReportStream = getClass().getResourceAsStream(subReportPath)) {

            if (mainTemplateStream == null) {
                logger.error("Main template file not found at path: {}", mainTemplatePath);
                throw new RuntimeException("Template file not found: " + mainTemplatePath);
            }
            if (subReportStream == null) {
                logger.error("Sub-report template file not found at path: {}", subReportPath);
                throw new RuntimeException("Subreport file not found: " + subReportPath);
            }

            JasperReport subReport = JasperCompileManager.compileReport(subReportStream);
            JasperReport mainReport = JasperCompileManager.compileReport(mainTemplateStream);

            InputStream qrCodeStream = qrCodeService.createQRCode(person.getProfileUrl(), 200, 200);
            JRBeanCollectionDataSource subReportDataSource = new JRBeanCollectionDataSource(person.getBooks());

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("SUB_REPORT_DATA_SOURCE", subReportDataSource);
            parameters.put("BOOKS_SUB_REPORT", subReport);
            parameters.put("QR_CODE_IMAGE", qrCodeStream);

            JRBeanCollectionDataSource mainReportDataSource = new JRBeanCollectionDataSource(Collections.singletonList(person));
            JasperPrint jasperPrint = JasperFillManager.fillReport(mainReport, parameters, mainReportDataSource);

            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
                byte[] pdfBytes = outputStream.toByteArray();
                logger.info("Successfully exported PDF for person ID: {}. File size: {} bytes.", person.getId(), pdfBytes.length);
                return new ByteArrayResource(pdfBytes);
            }
        } catch (Exception e) {
            logger.error("Failed to export person with ID {} to PDF.", person.getId(), e);
            if (e instanceof IOException) {
                throw (IOException) e;
            }
            throw new RuntimeException("An unexpected error occurred during PDF export.", e);
        }
    }
}
