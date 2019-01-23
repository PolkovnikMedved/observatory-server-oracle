package be.solodoukhin.controller;

import be.solodoukhin.domain.persistent.StructureElement;
import be.solodoukhin.domain.api.ErrorResponse;
import be.solodoukhin.repository.StructureElementRepository;
import be.solodoukhin.repository.StructureRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * Brief description here....
 *
 * @author viktor.solodoukhin@groups.be
 * @since 2018.12.19
 */

@RestController
@RequestMapping("/structure-element")
public class StructureElementController {
    private static final Logger LOGGER = LoggerFactory.getLogger(StructureElementController.class);
    private StructureElementRepository repository;
    private StructureRepository structureRepository;

    @Autowired
    public StructureElementController(StructureElementRepository repository, StructureRepository structureRepository) {
        this.repository = repository;
        this.structureRepository = structureRepository;
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody StructureElement el) {
        LOGGER.info("Call to StructureElementController.update with element number = {}", el.getId());
        Optional<StructureElement> element = repository.findById(el.getId());
        if(element.isPresent()) {

            if(el.getTypeStructure() != null) {
                if (this.structureRepository.existsById(el.getTypeStructure().getName()) && (!el.getTypeStructure().getName().equals(element.get().getTypeStructure().getName()))) {
                    element.get().setTypeStructure(this.structureRepository.getOne(el.getTypeStructure().getName()));
                } else {
                    return ResponseEntity.badRequest().body(new ErrorResponse(400, "Could not find new element type"));
                }
            }

            element.get().setTag(el.getTag().orElse(null));
            element.get().setDescription(el.getDescription());
            element.get().setOptional(el.isOptional());
            element.get().setRepetitive(el.isRepetitive());

            return ResponseEntity.ok(repository.save(element.get()));
        }
        else
        {
            return ResponseEntity.badRequest().body(el);
        }
    }
}
