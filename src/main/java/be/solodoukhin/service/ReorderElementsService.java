package be.solodoukhin.service;

import be.solodoukhin.domain.dto.StructureDTO;
import be.solodoukhin.domain.persistent.Structure;
import be.solodoukhin.domain.persistent.StructureElement;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Brief description here....
 *
 * @author viktor.solodoukhin@groups.be
 * @since 2018.12.14
 * Description This service helps us to reorder Structure elements.
 */
@Service
public class ReorderElementsService {

    /**
     * We call this method to reorder sequence field of Structure object.
     * We use this method with a StructureDTO object received from the user
     *
     * Warning: The identifiers could not be null.
     *
     * @param structure Structure. If elements.size != 0 then each element.id can't be null
     * @param dto StructureDTO. If elements.size != null then each element.id can't be null
     */
    public void reorder(Structure structure, StructureDTO dto) {
        for(int i = 0; i < dto.getElements().size(); i++) {
            for(StructureElement element: structure.getElements()) {
                if(dto.getElements().get(i).getId().equals(element.getId())) {
                    element.setSequence(i);
                }
            }
        }
    }

    public void reorderWithNullIdentifiers(Structure structure) {
        Collections.sort(structure.getElements());
        for(int i = 0; i < structure.getElements().size(); i++) {
            structure.getElements().get(i).setSequence(i);
        }
    }
}
