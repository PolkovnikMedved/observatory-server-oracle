package be.solodoukhin.controller;

import be.solodoukhin.domain.Document;
import be.solodoukhin.repository.DocumentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private DocumentRepository documentRepository;

    @Autowired
    public DocumentController(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    @GetMapping("/all")
    public Iterable<Document> getPage(@RequestParam(value = "page", required = false) Integer pageNumber)
    {
        LOGGER.info("Call to DocumentController.getPage with page = {}", pageNumber);
        if (pageNumber == null){
            pageNumber = 0;
        }

        if(pageNumber >= 1){
            // Front-end problem forces us to mad solutions
            pageNumber--;
        }

        Pageable pageable = PageRequest.of(pageNumber, 15, Sort.Direction.ASC, "number");

        return this.documentRepository.getAllWithCategory(pageable);
    }

    @GetMapping("/allFiltered")
    public Iterable<Document> getFilteredPage(
            @RequestParam(value = "documentNumber", required = false) Integer documentNumber,
            @RequestParam(value = "documentName", required = false) String documentName,
            @RequestParam(value = "documentCategory", required = false) String documentCategory,
            @RequestParam(value = "author", required = false) String author,
            @RequestParam(value = "page", required = false) Integer pageNumber
    )
    {
        LOGGER.info("Call to DocumentController.getFilteredPage with page = {}", pageNumber);
        if (pageNumber == null){
            pageNumber = 0;
        }

        LOGGER.info(documentNumber + "/" + documentName + "/" + documentCategory);

        Pageable pageable = PageRequest.of(pageNumber, 15, Sort.Direction.ASC, "number");

        return this.documentRepository.getAllByFilter(documentNumber, documentName, documentCategory, author, pageable);
    }
}
