package be.solodoukhin.controller;

import be.solodoukhin.domain.dto.DocumentDTO;
import be.solodoukhin.domain.persistent.Document;
import be.solodoukhin.repository.DocumentRepository;
import be.solodoukhin.service.DocumentFilterService;
import be.solodoukhin.service.converter.DocumentConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public ResponseEntity<Document> updateVersions(@RequestBody @Valid DocumentDTO dto) {
        log.info("Call to DocumentController.updateVersions with id = '{}'", dto.getNumber());
        Optional<Document> originalDocument = this.documentRepository.findById(dto.getNumber());
        if(!originalDocument.isPresent()) {
            log.warn("Could not find Document from dto with id = '{}'", dto.getNumber());
            return ResponseEntity.badRequest().build();
        }

        converter.updateDocumentFromDTO(originalDocument.get(), dto, "SOLODOUV");
        originalDocument.get().getSignature().setModification("SOLODOUV");

        Document saved;
        try{
            saved = this.documentRepository.save(originalDocument.get());
        } catch (Exception e) {
            log.error("Could not persist document after updating his versions");
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(saved);
    }
}
