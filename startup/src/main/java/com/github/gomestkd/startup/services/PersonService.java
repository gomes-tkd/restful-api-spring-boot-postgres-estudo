package com.github.gomestkd.startup.services;

import com.github.gomestkd.startup.controllers.PersonController;
import com.github.gomestkd.startup.data.dto.PersonDTO;
import com.github.gomestkd.startup.exception.BadRequestException;
import com.github.gomestkd.startup.exception.FileStorageException;
import com.github.gomestkd.startup.exception.RequiredObjectIsNullException;
import com.github.gomestkd.startup.exception.ResourceNotFoundException;
import com.github.gomestkd.startup.file.exporter.contract.PersonExporter;
import com.github.gomestkd.startup.file.exporter.factory.FileExporterFactory;
import com.github.gomestkd.startup.file.importer.contract.FileImporter;
import com.github.gomestkd.startup.file.importer.factory.FileImporterFactory;
import com.github.gomestkd.startup.model.Person;
import com.github.gomestkd.startup.repository.PersonRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.github.gomestkd.startup.mapper.ObjectMapper.parseObject;

@Service
public class PersonService {

    private final Logger logger = LoggerFactory.getLogger(PersonService.class.getName());
    private final PersonRepository repository;
    private final FileImporterFactory importerFactory;
    private final FileExporterFactory exporterFactory;
    private final PagedResourcesAssembler<PersonDTO> assembler;

    public PersonService(PersonRepository repository, FileImporterFactory importerFactory,
                         FileExporterFactory exporterFactory, PagedResourcesAssembler<PersonDTO> assembler) {
        this.repository = repository;
        this.importerFactory = importerFactory;
        this.exporterFactory = exporterFactory;
        this.assembler = assembler;
    }

    public PagedModel<EntityModel<PersonDTO>> findAll(Pageable pageable) {
        logger.info("Finding all People!");
        Page<Person> peoplePage = repository.findAll(pageable);
        return buildPagedModel(pageable, peoplePage);
    }

    public PagedModel<EntityModel<PersonDTO>> findByName(String firstName, Pageable pageable) {
        logger.info("Finding People by name!");
        Page<Person> peoplePage = repository.findPeopleByName(firstName, pageable);
        return buildPagedModel(pageable, peoplePage);
    }

    public Resource exportPage(Pageable pageable, String acceptHeader) {
        logger.info("Exporting a People page!");

        Set<PersonDTO> people = repository.findAll(pageable)
                .map(person -> parseObject(person, PersonDTO.class))
                .stream().collect(Collectors.toSet());

        try {
            PersonExporter personExporter = this.exporterFactory.getPersonExporter(acceptHeader);
            return personExporter.exportPeople(people);
        } catch (Exception e) {
            throw new RuntimeException("Error during file export!", e);
        }
    }

    public Resource exportPerson(Long id) {
        logger.info("Exporting data of one Person !");

        PersonDTO person = repository.findById(id)
                .map(entity -> parseObject(entity, PersonDTO.class))
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        try {
            PersonExporter personExporter = this.exporterFactory.getPersonExporter("application/pdf");
            return personExporter.exportPerson(person);
        } catch (Exception e) {
            throw new RuntimeException("Error during file export!", e);
        }
    }

    public PersonDTO findById(Long id) {
        logger.info("Finding one Person!");

        Person entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        PersonDTO dto =  parseObject(entity, PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public PersonDTO create(PersonDTO person) {
        if (person == null) throw new RequiredObjectIsNullException();

        logger.info("Creating one Person!");
        Person entity = parseObject(person, Person.class);

        PersonDTO dto = parseObject(repository.save(entity), PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public Set<PersonDTO> massCreation(MultipartFile file) {
        logger.info("Importing People from file!");

        if (file.isEmpty()) throw new BadRequestException("Please set a Valid File!");

        try(InputStream inputStream = file.getInputStream()){
            String filename = Optional.ofNullable(file.getOriginalFilename())
                    .orElseThrow(() -> new BadRequestException("File name cannot be null"));
            FileImporter fileImporter = this.importerFactory.getImporter(filename);

            Set<Person> entities = fileImporter.importFile(inputStream).stream()
                    .map(dto -> repository.save(parseObject(dto, Person.class)))
                    .collect(Collectors.toSet());

            return entities.stream()
                    .map(entity -> {
                        PersonDTO dto = parseObject(entity, PersonDTO.class);
                        addHateoasLinks(dto);
                        return dto;
                    })
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            throw new FileStorageException("Error processing the file!");
        }
    }

    public PersonDTO update(PersonDTO person) {
        if (person == null) throw new RequiredObjectIsNullException();

        logger.info("Updating one Person!");
        Person entity = repository.findById(person.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());
        entity.setEnabled(person.getEnabled());
        entity.setPhotoUrl(person.getPhotoUrl());
        entity.setWikipediaProfileUrl(person.getProfileUrl());

        PersonDTO dto = parseObject(repository.save(entity), PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    @Transactional
    public PersonDTO disablePerson(Long id) {
        logger.info("Disabling one Person!");

        repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        repository.disabledPerson(id);

        Person entity = repository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Disabled User not found!"));

        PersonDTO dto = parseObject(entity, PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public void delete(Long id) {
        logger.info("Deleting one Person!");

        Person entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        repository.delete(entity);
    }

    private PagedModel<EntityModel<PersonDTO>> buildPagedModel(Pageable pageable, Page<Person> people) {
        Page<PersonDTO> peopleWithLinks = people.map(person -> {
            PersonDTO dto = parseObject(person, PersonDTO.class);
            addHateoasLinks(dto);
            return dto;
        });

        Link findAllLink = WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(PersonController.class)
                                .findAll(
                                        pageable.getPageNumber(),
                                        pageable.getPageSize(),
                                        String.valueOf(pageable.getSort())))
                .withSelfRel();
        return assembler.toModel(peopleWithLinks, findAllLink);
    }

    private void addHateoasLinks(PersonDTO dto) {
        //...
    }
}