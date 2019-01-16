package be.solodoukhin.service;

import be.solodoukhin.domain.Structure;
import org.springframework.stereotype.Service;

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
     * When calling this method, we will reindex the "sequence" field of
     * each structure element of the given structure. The sequence field will
     * receive the position of the element in the structure.elements list.
     *
     * @param structure Lambda structure
     */
    public void reorder(Structure structure)
    {
        for(int i = 0; i < structure.getElements().size(); i++) {
            structure.getElements().get(i).setSequence(i);
        }
    }
}
