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

import java.util.Optional;

/**
 * Author: Solodoukhin Viktor
 * Date: 01.12.18
 * Description: TODO
 */
@RestController
@RequestMapping("/document")
public class DocumentController {

    private final static Logger LOGGER = LoggerFactory.getLogger(DocumentController.class);
    private DocumentFilterService documentFilterService;
    private DocumentRepository documentRepository;

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
        LOGGER.info("Parameters: documentNumber=" + documentNumber + ", documentName=" + documentName + ", documentCategory=" + documentCategory + ", createdBy=" + createdBy + ", modifiedBy=" + modifiedBy);
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
        LOGGER.info("Call to DocumentController.getOne with id = " + id);
        return this.documentRepository.getOne(id);
    }

    //TODO remove create version + refactoring + remove verson if not exists
    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody Document document) {
        LOGGER.info("Call to DocumentController.update with id = " + document.getNumber());

        Optional<Document> originalDocument = this.documentRepository.findById(document.getNumber());
        if(!originalDocument.isPresent()) {
            return ResponseEntity.badRequest().body(new ErrorResponse(400, "Could not find document with id = " + document.getNumber()));
        }

        for (Version v : document.getVersions()) {
            if(v.getName() != null) {
                boolean found = false;
                for(Version originalVersion: originalDocument.get().getVersions()) {
                    if(v.getName().equalsIgnoreCase(originalVersion.getName())) {// Version already exists. Update ?
                        found = true;

                        if(!v.getDfaName().equalsIgnoreCase(originalVersion.getDfaName())) {
                            LOGGER.info("Update dfa for version : " + originalVersion.getName());
                            originalVersion.setDfaName(v.getDfaName());
                            originalVersion.getSignature().setModification("SOLODOUV");
                        }

                        if(!v.getDescription().equalsIgnoreCase(originalVersion.getDescription())) {
                            LOGGER.info("Update description for version: " + originalVersion.getName());
                            originalVersion.setDescription(v.getDescription());
                            originalVersion.getSignature().setModification("SOLODOUV");
                        }

                        break;
                    }
                }
                if(!found) { // This is a new version
                    v.setSignature(new PersistenceSignature("SOLODOUV"));
                    v.getSignature().setModification("SOLODOUV");
                    originalDocument.get().addVersion(v);
                }
            }
            else{
                return ResponseEntity.badRequest().body(new ErrorResponse(400, "One version is invalid. Version name is null."));
            }
        }

        try{
            this.documentRepository.save(originalDocument.get());
        } catch (Exception e){
            LOGGER.error("An error occurred", e);
            return ResponseEntity.badRequest().body(new ErrorResponse(400, e.getMessage()));
        }

        return ResponseEntity.ok().build();
    }
}
