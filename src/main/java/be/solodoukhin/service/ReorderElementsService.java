package be.solodoukhin.service;

import be.solodoukhin.domain.Structure;
import org.springframework.stereotype.Service;

/**
 * Brief description here....
 *
 * @author viktor.solodoukhin@groups.be
 * @since 2018.12.14
 */
@Service
public class ReorderElementsService {

    public void reorder(Structure structure)
    {
        for(int i = 0; i < structure.getElements().size(); i++) {
            structure.getElements().get(i).setSequence(i);
        }
    }
}
