package be.solodoukhin.service;

import be.solodoukhin.domain.Document;
import be.solodoukhin.domain.DocumentCategory;
import be.solodoukhin.domain.embeddable.Label;
import be.solodoukhin.domain.embeddable.PersistenceSignature;
import be.solodoukhin.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

/**
 * Brief description here....
 *
 * @author viktor.solodoukhin@groups.be
 * @since 2018.12.04
 */
@Service
public class DocumentFilterService {

    private final DocumentRepository documentRepository;
    private final EmptyStringService emptyStringService;

    @Autowired
    public DocumentFilterService(DocumentRepository documentRepository, EmptyStringService emptyStringService) {
        this.documentRepository = documentRepository;
        this.emptyStringService = emptyStringService;
    }

    public Page<Document> getFilteredDocuments(Integer documentNumber, String documentName, String categoryName, String createdBy, String modifiedBy, Integer pageNumber)
    {
        documentName = this.emptyStringService.parseEmptyString(documentName);
        categoryName = this.emptyStringService.parseEmptyString(categoryName);
        createdBy = this.emptyStringService.parseEmptyString(createdBy);
        modifiedBy = this.emptyStringService.parseEmptyString(modifiedBy);

        Pageable pageable = PageRequest.of(pageNumber, 15, Sort.Direction.ASC, "number");

        DocumentCategory documentCategoryExample = new DocumentCategory();
        documentCategoryExample.setLabel(new Label());
        documentCategoryExample.getLabel().setFrenchLabel(categoryName);

        Document example = new Document();
        if(documentNumber != null){
            example.setNumber(documentNumber);
        }
        example.setCategory(documentCategoryExample);
        example.setSignature(new PersistenceSignature());
        example.setLabel(new Label());
        example.getLabel().setFrenchLabel(documentName);
        example.getSignature().setCreatedBy(createdBy);
        example.getSignature().setModifiedBy(modifiedBy);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("category.label.frenchLabel", ExampleMatcher.GenericPropertyMatcher.of(ExampleMatcher.StringMatcher.CONTAINING).ignoreCase())
                .withMatcher("label.frenchLabel", ExampleMatcher.GenericPropertyMatcher.of(ExampleMatcher.StringMatcher.CONTAINING).ignoreCase())
                .withMatcher("signature.modifiedBy",  ExampleMatcher.GenericPropertyMatcher.of(ExampleMatcher.StringMatcher.CONTAINING).ignoreCase())
                .withMatcher("signature.createdBy",  ExampleMatcher.GenericPropertyMatcher.of(ExampleMatcher.StringMatcher.CONTAINING).ignoreCase());
        return this.documentRepository.findAll(Example.of(example, matcher), pageable);
    }
}
