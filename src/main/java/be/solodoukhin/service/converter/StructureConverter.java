package be.solodoukhin.service.converter;

import be.solodoukhin.domain.dto.StructureDTO;
import be.solodoukhin.domain.dto.StructureElementDTO;
import be.solodoukhin.domain.persistent.Structure;
import be.solodoukhin.domain.persistent.StructureElement;
import be.solodoukhin.domain.persistent.embeddable.PersistenceSignature;
import be.solodoukhin.repository.StructureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;

/**
 * Brief description here....
 *
 * @author viktor.solodoukhin@groups.be
 * @since 2019.01.23
 */
@Slf4j
@Service
public class StructureConverter {

    private final StructureRepository structureRepository;

    @Autowired
    public StructureConverter(StructureRepository structureRepository) {
        this.structureRepository = structureRepository;
    }

    public Structure toNewPersistableVersion(StructureDTO dto) {
        Structure structure = new Structure();
        structure.setName(dto.getName());
        structure.setTag(dto.getTag());
        structure.setDescription(dto.getDescription());
        return structure;
    }

    public void updateStructureFromDTO(Structure structure, StructureDTO dto, String modificationAuthor) {

        structure.setTag(dto.getTag());
        structure.setDescription(dto.getDescription());
        log.info("Structure updated.");

        // Update existent
        for(StructureElementDTO elementDTO: dto.getElements()) {
            for(StructureElement element: structure.getElements()) {
                if(elementDTO.getId() != null && elementDTO.getId().equals(element.getId())) {
                    log.info("Update element with id = {}" + elementDTO.getId());
                    element.setTag(elementDTO.getTag());
                    element.setDescription(elementDTO.getDescription());
                    element.setSequence(elementDTO.getSequence());
                    element.setRepetitive(elementDTO.isRepetitive());
                    element.setOptional(elementDTO.isOptional());

                    if(elementDTO.getTypeStructure() != null && elementDTO.getTypeStructure().getName() != null && this.structureRepository.existsById(elementDTO.getTypeStructure().getName())) {
                        element.setTypeStructure(this.structureRepository.getOne(elementDTO.getTypeStructure().getName()));
                    }
                    element.getSignature().setModification(modificationAuthor);
                }
            }
        }

        this.removeElements(structure, dto);

        this.addStructureElements(structure, dto, modificationAuthor);
    }

    private void addStructureElements(Structure structure, StructureDTO dto, String modificationAuthor) {
        for(StructureElementDTO elementDTO: dto.getElements()) {
            if(elementDTO.getId() == null) {
                log.info("Add element with tag = " + elementDTO.getTag());
                StructureElement structureElement = new StructureElement();
                structureElement.setTag(elementDTO.getTag());
                structureElement.setDescription(elementDTO.getDescription());
                structureElement.setSequence(elementDTO.getSequence());
                structureElement.setOptional(elementDTO.isOptional());
                structureElement.setRepetitive(elementDTO.isRepetitive());

                if(elementDTO.getTypeStructure() != null && elementDTO.getTypeStructure().getName() != null && structureRepository.existsById(elementDTO.getTypeStructure().getName())) {
                    structureElement.setTypeStructure(structureRepository.getOne(elementDTO.getTypeStructure().getName()));
                }
                structureElement.setSignature(new PersistenceSignature(modificationAuthor));
                structureElement.getSignature().setModification(modificationAuthor);
                structure.addElement(structureElement);
            }
        }
    }

    private void removeElements(Structure structure, StructureDTO dto) {
        Iterator<StructureElement> elementIterator = structure.getElements().iterator();
        while(elementIterator.hasNext()) {
            StructureElement element = elementIterator.next();

            boolean found = false;
            for(StructureElementDTO elementDTO: dto.getElements()) {
                if(elementDTO.getId() != null && elementDTO.getId().equals(element.getId())) {
                    found = true;
                }
            }

            if(!found) {
                log.info("Not found element with id = '{}'", element.getId());
                elementIterator.remove();
            }
        }
    }
}
