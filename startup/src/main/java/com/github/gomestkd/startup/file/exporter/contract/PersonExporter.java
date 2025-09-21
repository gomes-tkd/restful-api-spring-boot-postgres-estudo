package com.github.gomestkd.startup.file.exporter.contract;

import com.github.gomestkd.startup.data.dto.PersonDTO;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.rmi.server.ExportException;
import java.util.Set;

public interface PersonExporter {
    Resource exportPeople(Set<PersonDTO> people) throws IOException, ExportException;
    Resource exportPerson(PersonDTO person) throws IOException, ExportException;
}
