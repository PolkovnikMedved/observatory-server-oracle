package be.solodoukhin.controller;

import be.solodoukhin.domain.dto.StructureElementDTO;
import be.solodoukhin.domain.persistent.StructureElement;
import be.solodoukhin.exception.ApiGenericException;
import be.solodoukhin.repository.StructureElementRepository;
import be.solodoukhin.service.converter.StructureElementConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

/**
 * Brief description here....
 *
 * @author viktor.solodoukhin@groups.be
 * @since 2018.12.19
 */
@Slf4j
@RestController
@RequestMapping("/structure-element")
public class StructureElementController {
    private final StructureElementConverter converter;
    private final StructureElementRepository repository;

    @Autowired
    public StructureElementController(StructureElementRepository repository, StructureElementConverter converter) {
        this.repository = repository;
        this.converter = converter;
    }

    @PutMapping("/update")
    public ResponseEntity<StructureElement> update(@RequestBody @Valid StructureElementDTO element) {
        log.info("Call to StructureElementController.update with element number = {}", element.getId());
        Optional<StructureElement> base = repository.findById(element.getId());

        if(!base.isPresent()) {
            log.warn("Could not find StructureElement with id = '{}'", element.getId());
            return ResponseEntity.badRequest().build();
        }

        try{
            this.converter.updateElementFromDTO(base.get(), element);
        } catch (ApiGenericException e) {
            log.error("Could not convert DTO to StructureElement", e);
            return ResponseEntity.badRequest().build();
        }

        base.get().getSignature().setModification("SOLODOUV");
        StructureElement saved;
        try{
            saved = this.repository.save(base.get());
        } catch (Exception e) {
            log.error("Could not save StructureElement with number = {}", element.getId());
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(saved);
    }
}
