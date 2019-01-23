package be.solodoukhin.controller;

import be.solodoukhin.domain.api.ErrorResponse;
import be.solodoukhin.domain.dto.DocumentDTO;
import be.solodoukhin.domain.persistent.Document;
import be.solodoukhin.domain.persistent.Version;
import be.solodoukhin.domain.persistent.embeddable.PersistenceSignature;
import be.solodoukhin.repository.DocumentRepository;
import be.solodoukhin.service.DocumentFilterService;
import be.solodoukhin.service.converter.DocumentConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * Author: Solodoukhin Viktor
 * Date: 01.12.18
 * Description: Document REST methods
 */
@Slf4j
@RestController
@RequestMapping("/document")
public class DocumentController {

    private final DocumentFilterService documentFilterService;
    private final DocumentRepository documentRepository;
    private final DocumentConverter converter;

    @Autowired
    public DocumentController(DocumentFilterService documentFilterService, DocumentRepository documentRepository, DocumentConverter converter) {
        this.documentFilterService = documentFilterService;
        this.documentRepository = documentRepository;
        this.converter = converter;
    }

    @GetMapping("/all")
    public Iterable<Document> getFilteredPage(
            @RequestParam(value = "documentNumber", required = false) Integer documentNumber,
            @RequestParam(value = "documentName", required = false) String documentName,
            @RequestParam(value = "documentCategory", required = false) String documentCategory,
            @RequestParam(value = "createdBy", required = false) String createdBy,
            @RequestParam(value = "modifiedBy", required = false) String modifiedBy,
            @RequestParam(value = "page", required = false) Integer pageNumber
    )
    {
        log.info("Call to DocumentController.getFilteredPage with page = {}", pageNumber);
        log.info("Parameters: documentNumber={}, documentName={}, documentCategory={}, createdBy={}, modifiedBy={}", documentNumber, documentName, documentCategory, createdBy, modifiedBy);
        if (pageNumber == null){
            pageNumber = 0;
        }

        if(pageNumber >= 1){
            // Front-end problem forces us to mad solutions
            pageNumber--;
        }

        return this.documentFilterService.getFilteredDocuments(documentNumber, documentName, documentCategory, createdBy, modifiedBy, pageNumber);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentDTO> getOneDTO(@PathVariable("id") Integer id) {
        log.info("Call to getOneDTO with id = '{}'", id);
        Optional<Document> document = this.documentRepository.findById(id);

        if(!document.isPresent()) {
            log.warn("Could not find document with id = '{}'", id);
            return ResponseEntity.badRequest().build();
        }

        DocumentDTO response;
        try {
            response = this.converter.toDTO(document.get());
        } catch (Exception e) {
            log.error("An error occurred converting Document to DTO", e);
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(response);
    }

    // Warning update document CAN NOT update DOCUMENT TABLE. It's used to do cascade operations on version.
    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody Document document) {
        log.info("Call to DocumentController.update with id = {}", document.getNumber());

        Optional<Document> originalDocument = this.documentRepository.findById(document.getNumber());
        if(!originalDocument.isPresent()) {
            return ResponseEntity.badRequest().body(new ErrorResponse(400, "Could not find document with id = " + document.getNumber()));
        }

        // Update versions and remove
        Iterator<Version> it = originalDocument.get().getVersions().iterator();
        while(it.hasNext()) {
            Version originalVersion = it.next();
            Optional<Version> receivedVersion = document.getVersions().stream().filter(version -> version.getName().equalsIgnoreCase(originalVersion.getName())).findAny();

            if(receivedVersion.isPresent()) { // found version => see if we have to update the orignal one

                if(originalVersion.getDfaName().isPresent() && receivedVersion.get().getDfaName().isPresent()) {
                    log.info("Update DFA for version {}", originalVersion.getName());
                    originalVersion.setDfaName(receivedVersion.get().getDfaName().get());
                    originalVersion.getSignature().setModification("SOLODOUV");
                } else if(originalVersion.getDfaName().isPresent() && (!receivedVersion.get().getDfaName().isPresent() || receivedVersion.get().getDfaName().get().trim().equalsIgnoreCase("") )) {
                    log.info("Update DFA for version {}", originalVersion.getName());
                    originalVersion.setDfaName(null);
                    originalVersion.getSignature().setModification("SOLODOUV");
                }

                if(!receivedVersion.get().getDescription().equalsIgnoreCase(originalVersion.getDescription())) {
                    log.info("Update description for version: {}", originalVersion.getName());
                    originalVersion.setDescription(receivedVersion.get().getDescription());
                    originalVersion.getSignature().setModification("SOLODOUV");
                }

            } else {
                it.remove(); // the version doesn't exist in the received object => we remove it from the original one
            }
        }
        // add new versions
        if(document.getVersions().size() > originalDocument.get().getVersions().size()) {
            log.info("Document versions {} > original versions {}", document.getVersions().size(), originalDocument.get().getVersions().size());
            List<Version> newVersions = new ArrayList<>();
            for(Version received: document.getVersions()) {
                boolean found = false;
                for(Version original : originalDocument.get().getVersions()) {
                    if(original.getName().equalsIgnoreCase(received.getName())) {
                        log.info("Found : {}", original.getName());
                        found = true;
                        break;
                    }
                }
                if(!found) {
                    log.info("Not found : {}", received.getName());
                    received.setSignature(new PersistenceSignature("SOLODOUV"));
                    received.getSignature().setModification("SOLODOUV");
                    newVersions.add(received);
                }
            }
            log.info("New versions size = {}", newVersions.size());
            originalDocument.get().getVersions().addAll(newVersions);
            log.info("New versions size = {}", originalDocument.get().getVersions().size());
        }

        Document savedDocument;

        try{
            savedDocument = this.documentRepository.save(originalDocument.get());
        } catch (Exception e){
            log.error("An error occurred", e);
            return ResponseEntity.badRequest().body(new ErrorResponse(400, e.getMessage()));
        }

        return ResponseEntity.ok(savedDocument);
    }
}
