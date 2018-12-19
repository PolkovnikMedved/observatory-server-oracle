package be.solodoukhin.controller;

import be.solodoukhin.domain.StructureElement;
import be.solodoukhin.repository.StructureElementRepository;
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

    @Autowired
    public StructureElementController(StructureElementRepository repository) {
        this.repository = repository;
    }

    @PutMapping("/update")
    public ResponseEntity<StructureElement> update(@RequestBody StructureElement el) {
        LOGGER.info("Call to StructureElementController.update with element number = " + el.getId());
        Optional<StructureElement> element = repository.findById(el.getId());
        if(element.isPresent()) {

            element.get().setTag(el.getTag());
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
