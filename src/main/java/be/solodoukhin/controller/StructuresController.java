package be.solodoukhin.controller;

import be.solodoukhin.domain.dto.StructureDTO;
import be.solodoukhin.domain.persistent.Structure;
import be.solodoukhin.domain.persistent.embeddable.PersistenceSignature;
import be.solodoukhin.domain.xml.StructureWrapper;
import be.solodoukhin.repository.StructureRepository;
import be.solodoukhin.service.CopyService;
import be.solodoukhin.service.ReorderElementsService;
import be.solodoukhin.service.StructureFilterService;
import be.solodoukhin.service.converter.StructureConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

/**
 * Brief description here....
 *
 * @author viktor.solodoukhin@groups.be
 * @since 2018.12.06
 * Description: Structure REST methods
 */
@Slf4j
@RestController
@RequestMapping(RoutingMapping.PROTECTED_URL_STRUCTURE)
public class StructuresController {

    private final StructureConverter converter;
    private final StructureRepository structureRepository;
    private final StructureFilterService structureFilterService;
    private final CopyService copyService;
    private final ReorderElementsService reorderElementsService;

    @Autowired
    public StructuresController(
            StructureRepository structureRepository,
            StructureFilterService structureFilterService,
            CopyService copyService,
            ReorderElementsService reorderElementsService,
            StructureConverter converter
    ) {
        this.structureRepository = structureRepository;
        this.structureFilterService = structureFilterService;
        this.copyService = copyService;
        this.reorderElementsService = reorderElementsService;
        this.converter = converter;
    }

    @GetMapping("/all-names")
    public List<String> getAllNames()
    {
        log.info("Call to StructuresController.getAllNames");
        return this.structureRepository.getAllStructureNames();
    }

    @GetMapping("/is-used")
    public Boolean isUsed(@RequestParam("name") String name) {
        log.info("Call to StructuresController.isUsed with name = {}", name);
        return this.structureRepository.isUsedAsType(name);
    }

    @GetMapping("/all")
    public Page<Structure> getAll(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "tag", required = false) String tag,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "createdBy", required = false) String createdBy,
            @RequestParam(value = "modifiedBy", required = false) String modifiedBy,
            @RequestParam(value = "page", required = false) Integer page
            )
    {
        log.info("Call to StructuresController.getAll with page = {}", page);
        log.info("Parameters: name={}, tag={}, description={}, createdBy={}, modifiedBy={}", name, tag, description, createdBy, modifiedBy);

        if (page == null){
            page = 0;
        }

        if(page >= 1){
            // Front-end problem forces us to mad solutions
            page--;
        }

        return this.structureFilterService.getFilteredStructures(name, tag, description, createdBy, modifiedBy, page);
    }

    @GetMapping("/{name}")
    public ResponseEntity<Structure> getOne(@PathVariable("name") String name)
    {
        log.info("Call to StructuresController.getOne with name={}", name);
        Optional<Structure> found = this.structureRepository.findById(name);
        if(found.isPresent()){
            return ResponseEntity.ok(found.get());
        }
        else
        {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Structure> create(@RequestBody @Valid StructureDTO s, Principal principal) {
        log.info("Call to StructuresController.create with structure name        = {}", s.getName());
        log.info("Call to StructuresController.create with structure tag         = {}", s.getTag());
        log.info("Call to StructuresController.create with structure description = {}", s.getDescription());
        Structure structure = converter.toNewPersistableVersion(s);
        structure.setSignature(new PersistenceSignature(principal.getName()));
        try {
            structure = this.structureRepository.save(structure);
        } catch (Exception e) {
            log.error("Could not save Structure", e);
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(structure);
    }

    @PutMapping("/update-order")
    public ResponseEntity<Structure> changeOrder(@RequestBody @Valid StructureDTO structureDTO, Principal principal) {
        log.info("Call to StructuresController.changeOrder with name = {}", structureDTO.getName());
        Optional<Structure> structure = this.structureRepository.findById(structureDTO.getName());
        if(!structure.isPresent()) {
            log.warn("Could not find structure with name = '{}'", structureDTO.getName());
            return ResponseEntity.badRequest().build();
        }

        this.reorderElementsService.reorder(structure.get(), structureDTO);
        structure.get().getElements().forEach(el -> el.getSignature().setModification(principal.getName()));
        Structure savedStructure;
        try{
            savedStructure = this.structureRepository.save(structure.get());
        } catch ( Exception e ) {
            log.error("Could not update structure", e);
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(savedStructure);
    }

    @PutMapping("/update")
    public ResponseEntity<Structure> updateStructureAndElements(@RequestBody @Valid StructureDTO structureDTO, Principal principal) {
        log.info("Call to StructureController.updateStructureAndElements with name = '{}'", structureDTO.getName());
        log.info("We have '{}' elements in the structure.", structureDTO.getElements().size());
        Optional<Structure> originalStructure = this.structureRepository.findById(structureDTO.getName());
        if(!originalStructure.isPresent()) {
            log.error("Could not find structure from DTO with name = '{}'", structureDTO.getName());
            return ResponseEntity.badRequest().build();
        }

        this.converter.updateStructureFromDTO(originalStructure.get(), structureDTO, principal.getName());

        Structure saved;
        try {
            saved = this.structureRepository.save(originalStructure.get());
        } catch (Exception e) {
            log.error("Could not save updated structure with name = '{}'", structureDTO);
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(saved);
    }

    @GetMapping("/copy")
    public ResponseEntity<Structure> copyStructureNew(@RequestParam("from") String from, @RequestParam("to") String to, Principal principal) {
        log.info("Call to StructuresController.copyStructure from = '{}', to = '{}'", from, to);
        Optional<Structure> fromStructure = this.structureRepository.findById(from);

        if(!fromStructure.isPresent() || to == null || to.trim().equalsIgnoreCase("")) {
            log.warn("Could not create copy from structure with name = '{}', to = '{}'", from, to);
            return ResponseEntity.badRequest().build();
        }

        Structure copy = this.copyService.createCopyStructure(fromStructure.get(), to);
        copy.setSignature(new PersistenceSignature(principal.getName()));
        copy.getElements().forEach(el -> {
            el.setSignature(new PersistenceSignature(principal.getName()));
            el.getSignature().setModification(principal.getName()); // Hibernate will update the children to set the foreign key
        });

        try{
            copy = this.structureRepository.save(copy);
        } catch (Exception e) {
            log.error("Could not save copy structure", e);
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(copy);
    }

    @GetMapping(value = "/xml", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<StructureWrapper> getStructureXml(@RequestParam("name") String structureName, @RequestParam("empty") boolean empty) {
        log.info("Call to StructuresController.getStructureXml");
        Optional<Structure> structure = this.structureRepository.findById(structureName);

        if(!structure.isPresent()){
            log.error("Could not find structure with name = '{}'", structureName);
            return ResponseEntity.badRequest().build();
        }

        StructureWrapper wrapper = new StructureWrapper(structure.get(), empty);
        log.info("Wrapper = " + wrapper);

        return ResponseEntity.ok(wrapper);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Structure> delete(@RequestParam("name") String name)
    {
        log.info("Call to StructuresController.delete with name = {}", name);
        try{
            this.structureRepository.deleteById(name);
        } catch (Exception e) {
            log.error("An error occurred", e);
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }
}
