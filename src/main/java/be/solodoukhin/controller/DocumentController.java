package be.solodoukhin.controller;

import be.solodoukhin.domain.Document;
import be.solodoukhin.service.DocumentFilterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @Autowired
    public DocumentController(DocumentFilterService documentFilterService) {
        this.documentFilterService = documentFilterService;
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
}
