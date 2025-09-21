package com.github.gomestkd.startup.file.importer.contract;

import com.github.gomestkd.startup.data.dto.PersonDTO;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

public interface FileImporter {
    Set<PersonDTO> importFile(InputStream inputStream) throws IOException;
}
