package be.solodoukhin.service.converter;

import be.solodoukhin.domain.dto.StructureElementDTO;
import be.solodoukhin.domain.persistent.StructureElement;
import be.solodoukhin.exception.ApiGenericException;
import be.solodoukhin.repository.StructureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Brief description here....
 *
 * @author viktor.solodoukhin@groups.be
 * @since 2019.01.23
 */
@Service
public class StructureElementConverter {

    private final StructureRepository structureRepository;

    @Autowired
    public StructureElementConverter(StructureRepository structureRepository) {
        this.structureRepository = structureRepository;
    }

    public void updateElementFromDTO(StructureElement element, StructureElementDTO dto) throws ApiGenericException {
        element.setTag(dto.getTag());
        element.setDescription(dto.getDescription());
        element.setSequence(dto.getSequence());
        element.setOptional(dto.isOptional());
        element.setRepetitive(dto.isRepetitive());

        if(dto.getTypeStructure() != null && dto.getTypeStructure().getName() != null) {
            if (this.structureRepository.existsById(dto.getTypeStructure().getName())) {
                element.setTypeStructure(structureRepository.getOne(dto.getTypeStructure().getName()));
            } else {
                throw new ApiGenericException(String.format("Could not find element type with name = '%s'", dto.getTypeStructure().getName()));
            }
        }
    }
}
