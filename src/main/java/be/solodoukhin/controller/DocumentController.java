package be.solodoukhin.controller;

import be.solodoukhin.domain.Document;
import be.solodoukhin.domain.Version;
import be.solodoukhin.domain.api.ErrorResponse;
import be.solodoukhin.domain.embeddable.PersistenceSignature;
import be.solodoukhin.repository.DocumentRepository;
import be.solodoukhin.service.DocumentFilterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RestController
@RequestMapping("/document")
public class DocumentController {

    private DocumentFilterService documentFilterService;
    private DocumentRepository documentRepository;
    private final static Logger LOGGER = LoggerFactory.getLogger(DocumentController.class);

    @Autowired
    public DocumentController(DocumentFilterService documentFilterService, DocumentRepository documentRepository) {
        this.documentFilterService = documentFilterService;
        this.documentRepository = documentRepository;
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
        LOGGER.info("Call to DocumentController.getFilteredPage with page = {}", pageNumber);
        LOGGER.info("Parameters: documentNumber={}, documentName={}, documentCategory={}, createdBy={}, modifiedBy={}", documentNumber, documentName, documentCategory, createdBy, modifiedBy);
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
    public Document getOne(@PathVariable("id") Integer id)
    {
        LOGGER.info("Call to DocumentController.getOne with id = {}", id);
        return this.documentRepository.getOne(id);
    }

    // Warning update document CAN NOT update DOCUMENT TABLE. It's used to do cascade operations on version.
    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody Document document) {
        LOGGER.info("Call to DocumentController.update with id = {}", document.getNumber());

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
                    LOGGER.info("Update DFA for version {}", originalVersion.getName());
                    originalVersion.setDfaName(receivedVersion.get().getDfaName().get());
                    originalVersion.getSignature().setModification("SOLODOUV");
                } else if(originalVersion.getDfaName().isPresent() && (!receivedVersion.get().getDfaName().isPresent() || receivedVersion.get().getDfaName().get().trim().equalsIgnoreCase("") )) {
                    LOGGER.info("Update DFA for version {}", originalVersion.getName());
                    originalVersion.setDfaName(null);
                    originalVersion.getSignature().setModification("SOLODOUV");
                }

                if(!receivedVersion.get().getDescription().equalsIgnoreCase(originalVersion.getDescription())) {
                    LOGGER.info("Update description for version: {}", originalVersion.getName());
                    originalVersion.setDescription(receivedVersion.get().getDescription());
                    originalVersion.getSignature().setModification("SOLODOUV");
                }

            } else {
                it.remove(); // the version doesn't exist in the received object => we remove it from the original one
            }
        }
        // add new versions
        if(document.getVersions().size() > originalDocument.get().getVersions().size()) {
            LOGGER.info("Document versions {} > original versions {}", document.getVersions().size(), originalDocument.get().getVersions().size());
            List<Version> newVersions = new ArrayList<>();
            for(Version received: document.getVersions()) {
                boolean found = false;
                for(Version original : originalDocument.get().getVersions()) {
                    if(original.getName().equalsIgnoreCase(received.getName())) {
                        LOGGER.info("Found : {}", original.getName());
                        found = true;
                        break;
                    }
                }
                if(!found) {
                    LOGGER.info("Not found : {}", received.getName());
                    received.setSignature(new PersistenceSignature("SOLODOUV"));
                    received.getSignature().setModification("SOLODOUV");
                    newVersions.add(received);
                }
            }
            LOGGER.info("New versions size = {}", newVersions.size());
            originalDocument.get().getVersions().addAll(newVersions);
            LOGGER.info("New versions size = {}", originalDocument.get().getVersions().size());
        }

        Document savedDocument;

        try{
            savedDocument = this.documentRepository.save(originalDocument.get());
        } catch (Exception e){
            LOGGER.error("An error occurred", e);
            return ResponseEntity.badRequest().body(new ErrorResponse(400, e.getMessage()));
        }

        return ResponseEntity.ok(savedDocument);
    }
}
