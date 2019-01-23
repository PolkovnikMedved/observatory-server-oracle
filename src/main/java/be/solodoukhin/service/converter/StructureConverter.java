package be.solodoukhin.service.converter;

import be.solodoukhin.domain.dto.StructureDTO;
import be.solodoukhin.domain.persistent.Structure;
import org.springframework.stereotype.Service;

/**
 * Brief description here....
 *
 * @author viktor.solodoukhin@groups.be
 * @since 2019.01.23
 */
@Service
public class StructureConverter {

    public Structure toNewPersistableVersion(StructureDTO dto) {
        Structure structure = new Structure();
        structure.setName(dto.getName());
        structure.setTag(dto.getTag());
        structure.setDescription(dto.getDescription());
        return structure;
    }
}
