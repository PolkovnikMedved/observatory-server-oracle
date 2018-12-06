package be.solodoukhin.service;

import be.solodoukhin.domain.Structure;
import be.solodoukhin.domain.embeddable.PersistenceSignature;
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

    @Autowired
    public StructureFilterService(StructureRepository structureRepository) {
        this.structureRepository = structureRepository;
    }

    public Page<Structure> getFilteredStructures(String name, String tag, String description, String createdBy, String modifiedBy, Integer page)
    {
        if(name != null && name.trim().equalsIgnoreCase("")){
            name = null;
        }

        if(tag != null && tag.trim().equalsIgnoreCase("")){
            tag = null;
        }

        if(description != null && description.trim().equalsIgnoreCase("")){
            description = null;
        }

        if(createdBy != null && createdBy.trim().equalsIgnoreCase("")){
            createdBy = null;
        }

        if(modifiedBy != null && modifiedBy.trim().equalsIgnoreCase("")){
            modifiedBy = null;
        }

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
