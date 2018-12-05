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

    private DocumentRepository documentRepository;

    @Autowired
    public DocumentFilterService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public Page<Document> getFilteredDocuments(Integer documentNumber, String documentName, String categoryName, String createdBy, String modifiedBy, Integer pageNumber)
    {
        if(documentName != null && documentName.trim().equalsIgnoreCase("")){
            documentName = null;
        }

        if(categoryName != null && categoryName.trim().equalsIgnoreCase("")){
            categoryName = null;
        }

        if(createdBy != null && createdBy.trim().equalsIgnoreCase("")){
            createdBy = null;
        }

        if(modifiedBy != null && modifiedBy.trim().equalsIgnoreCase("")){
            modifiedBy = null;
        }

        Pageable pageable = PageRequest.of(pageNumber, 15, Sort.Direction.ASC, "number");

        DocumentCategory documentCategoryExample = new DocumentCategory();
        documentCategoryExample.setLabel(new Label());
        documentCategoryExample.getLabel().setFrenchLabel(categoryName);
        //documentCategoryExample.getLabel().setDutchLabel(categoryName);

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
