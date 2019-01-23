package be.solodoukhin.controller;

import be.solodoukhin.domain.api.ErrorResponse;
import be.solodoukhin.domain.dto.StructureDTO;
import be.solodoukhin.domain.persistent.Structure;
import be.solodoukhin.domain.persistent.StructureElement;
import be.solodoukhin.domain.persistent.embeddable.PersistenceSignature;
import be.solodoukhin.repository.StructureRepository;
import be.solodoukhin.service.CopyService;
import be.solodoukhin.service.ReorderElementsService;
import be.solodoukhin.service.StructureFilterService;
import be.solodoukhin.service.converter.StructureConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
@RequestMapping("/structure")
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
    public ResponseEntity<Structure> create(@RequestBody @Valid StructureDTO s) {
        log.info("Call to StructuresController.create with structure name        = {}", s.getName());
        log.info("Call to StructuresController.create with structure tag         = {}", s.getTag());
        log.info("Call to StructuresController.create with structure description = {}", s.getDescription());
        Structure structure = converter.toNewPersistableVersion(s);
        structure.setSignature(new PersistenceSignature("SOLODOUV"));
        try {
            structure = this.structureRepository.save(structure);
        } catch (Exception e) {
            log.error("Could not save Structure", e);
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(structure);
    }

    @PutMapping("/update-order")
    public ResponseEntity<Structure> changeOrder(@RequestBody @Valid StructureDTO structureDTO) {
        log.info("Call to StructuresController.changeOrder with name = {}", structureDTO.getName());
        Optional<Structure> structure = this.structureRepository.findById(structureDTO.getName());
        if(!structure.isPresent()) {
            log.warn("Could not find structure with name = '{}'", structureDTO.getName());
            return ResponseEntity.badRequest().build();
        }

        this.reorderElementsService.reorder(structure.get(), structureDTO);
        structure.get().getElements().forEach(el -> el.getSignature().setModification("SOLODOUV"));
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
    public ResponseEntity<?> update(@RequestBody Structure s) //TODO element is not removed....
    {
        log.info("Call to StructureController.update with name = {}", s.getName());
        log.info("We have {} elements.", s.getElements().size());
        Optional<Structure> found = this.structureRepository.findById(s.getName());
        if(found.isPresent()){
            log.info("Found has {} elements.", found.get().getElements().size());
            found.get().setTag(s.getTag().orElse(null));
            found.get().setDescription(s.getDescription());
            found.get().getSignature().setModifiedBy("SOLODOUV");

            log.info("On va faire {} boucles.", s.getElements().size());
            int cpt = 0;
            for(StructureElement receivedElement: s.getElements()) {
                log.info("Boucle ....");
                if(receivedElement.getId() != null && receivedElement.getId() != 0L) { // Existing element
                    for(StructureElement existing: found.get().getElements()) {
                        if(existing.getId().equals(receivedElement.getId())) { // found in the original object
                            log.info("On a trouvé notre bonheur: {}", existing.getId());
                            existing.setSequence(cpt);
                            existing.getSignature().setModification("SOLODOUV");
                            break;
                        }
                    }
                }
                else { // new element
                    log.info("C'est un tout nouvel élément: {}", receivedElement);
                    if(receivedElement.getTypeStructure() == null || receivedElement.getTypeStructure().getName() == null || receivedElement.getTypeStructure().getName().trim().equalsIgnoreCase("")){
                        receivedElement.setTypeStructure(null);
                    }
                    receivedElement.setSignature(new PersistenceSignature("SOLODOUV"));
                    receivedElement.setSequence(cpt);
                    receivedElement.getSignature().setModification("SOLODOUV");
                    found.get().getElements().add(receivedElement);
                }

                cpt++;
            }

            log.info("Structure to save : {}", found.get());

            Structure saved = null;
            try{
                log.info("Before save found has {} elements", found.get().getElements().size());
                saved = this.structureRepository.save(found.get());
            } catch (Exception e) {
                log.error("An error occurred", e);
                return ResponseEntity.badRequest().body(new ErrorResponse(400, e.getMessage()));
            }

            return ResponseEntity.ok(saved);
        }
        else
        {
            return ResponseEntity.badRequest().body(s);
        }
    }

    @GetMapping("/copy")
    public ResponseEntity<Structure> copyStructureNew(@RequestParam("from") String from, @RequestParam("to") String to) {
        log.info("Call to StructuresController.copyStructure from = '{}', to = '{}'", from, to);
        Optional<Structure> fromStructure = this.structureRepository.findById(from);

        if(!fromStructure.isPresent() || to == null || to.trim().equalsIgnoreCase("")) {
            log.warn("Could not create copy from structure with name = '{}', to = '{}'", from, to);
            return ResponseEntity.badRequest().build();
        }

        Structure copy = this.copyService.createCopyStructure(fromStructure.get(), to);
        copy.setSignature(new PersistenceSignature("SOLODOUV"));
        copy.getElements().forEach(el -> {
            el.setSignature(new PersistenceSignature("SOLODOUV"));
            el.getSignature().setModification("SOLODOUV"); // Hibernate will update the children to set the foreign key
        });

        try{
            copy = this.structureRepository.save(copy);
        } catch (Exception e) {
            log.error("Could not save copy structure", e);
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(copy);
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
