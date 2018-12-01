package be.solodoukhin.controller;

import be.solodoukhin.domain.Document;
import be.solodoukhin.repository.DocumentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

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
    public Iterable<Document> getPage(@RequestAttribute(value = "page", required = false) Integer pageNumber)
    {
        LOGGER.info("Call to DocumentController.getPage with page = {}", pageNumber);
        if (pageNumber == null){
            pageNumber = 0;
        }

        Pageable pageable = PageRequest.of(pageNumber, 15, Sort.Direction.ASC, "number");

        return this.documentRepository.findAll(pageable);
    }
}
