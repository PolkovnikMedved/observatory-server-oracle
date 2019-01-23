package be.solodoukhin.service;

import be.solodoukhin.domain.persistent.Structure;
import be.solodoukhin.domain.persistent.embeddable.PersistenceSignature;
import be.solodoukhin.repository.StructureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

/**
 * Brief description here....
 *
 * @author viktor.solodoukhin@groups.be
 * @since 2018.12.06
 */
@Service
public class StructureFilterService {

    private final StructureRepository structureRepository;
    private final EmptyStringService emptyStringService;

    @Autowired
    public StructureFilterService(StructureRepository structureRepository, EmptyStringService emptyStringService) {
        this.structureRepository = structureRepository;
        this.emptyStringService = emptyStringService;
    }

    public Page<Structure> getFilteredStructures(String name, String tag, String description, String createdBy, String modifiedBy, Integer page)
    {
        name = this.emptyStringService.parseEmptyString(name);
        tag = this.emptyStringService.parseEmptyString(tag);
        description = this.emptyStringService.parseEmptyString(description);
        createdBy = this.emptyStringService.parseEmptyString(createdBy);
        modifiedBy = this.emptyStringService.parseEmptyString(modifiedBy);

        // Let us get pages of 12 elements sorted by structure name
        Pageable pageable = PageRequest.of(page, 15, Sort.Direction.ASC, "name");

        // Let us create structure example object
        Structure structure = new Structure();
        structure.setName(name);
        structure.setTag(tag);
        structure.setDescription(description);
        structure.setSignature(new PersistenceSignature());
        structure.getSignature().setCreatedBy(createdBy);
        structure.getSignature().setModifiedBy(modifiedBy);

        // we will search with a `like '%:value%'` for name, tag, description, createdBy and modifiedBy
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatcher.of(ExampleMatcher.StringMatcher.CONTAINING).ignoreCase())
                .withMatcher("tag", ExampleMatcher.GenericPropertyMatcher.of(ExampleMatcher.StringMatcher.CONTAINING).ignoreCase())
                .withMatcher("description", ExampleMatcher.GenericPropertyMatcher.of(ExampleMatcher.StringMatcher.CONTAINING).ignoreCase())
                .withMatcher("signature.modifiedBy",  ExampleMatcher.GenericPropertyMatcher.of(ExampleMatcher.StringMatcher.CONTAINING).ignoreCase())
                .withMatcher("signature.createdBy",  ExampleMatcher.GenericPropertyMatcher.of(ExampleMatcher.StringMatcher.CONTAINING).ignoreCase());

        return this.structureRepository.findAll(Example.of(structure, matcher), pageable);
    }
}
