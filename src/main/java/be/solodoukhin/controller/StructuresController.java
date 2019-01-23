package be.solodoukhin.controller;

import be.solodoukhin.domain.persistent.Structure;
import be.solodoukhin.domain.persistent.StructureElement;
import be.solodoukhin.domain.api.ErrorResponse;
import be.solodoukhin.domain.persistent.embeddable.PersistenceSignature;
import be.solodoukhin.repository.StructureRepository;
import be.solodoukhin.service.CopyService;
import be.solodoukhin.service.ReorderElementsService;
import be.solodoukhin.service.StructureFilterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Brief description here....
 *
 * @author viktor.solodoukhin@groups.be
 * @since 2018.12.06
 * Description: Structure REST methods
 */
@RestController
@RequestMapping("/structure")
public class StructuresController {

    private static final Logger LOGGER = LoggerFactory.getLogger(StructuresController.class);
    private final StructureRepository structureRepository;
    private final StructureFilterService structureFilterService;
    private final CopyService copyService;
    private final ReorderElementsService reorderElementsService;

    @Autowired
    public StructuresController(StructureRepository structureRepository, StructureFilterService structureFilterService, CopyService copyService, ReorderElementsService reorderElementsService) {
        this.structureRepository = structureRepository;
        this.structureFilterService = structureFilterService;
        this.copyService = copyService;
        this.reorderElementsService = reorderElementsService;
    }

    @GetMapping("/all-names")
    public List<String> getAllNames()
    {
        LOGGER.info("Call to StructuresController.getAllNames");
        return this.structureRepository.getAllStructureNames();
    }

    @GetMapping("/is-used")
    public Boolean isUsed(@RequestParam("name") String name) {
        LOGGER.info("Call to StructuresController.isUsed with name = {}", name);
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
        LOGGER.info("Call to StructuresController.getAll with page = {}", page);
        LOGGER.info("Parameters: name={}, tag={}, description={}, createdBy={}, modifiedBy={}", name, tag, description, createdBy, modifiedBy);

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
        LOGGER.info("Call to StructuresController.getOne with name={}", name);
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
    public Structure add(@RequestBody Structure s)
    {
        LOGGER.info("Call to StructuresController.save with structure name  ={}", s.getName());
        LOGGER.info("Call to StructuresController.save with structure tag   ={}", s.getTag());
        LOGGER.info("Call to StructuresController.save with structure descr ={}", s.getDescription());
        s.setSignature(new PersistenceSignature("SOLODOUV"));
        return this.structureRepository.save(s);
    }

    @PutMapping("/update-order")
    public ResponseEntity<?> changeOrder(@RequestBody Structure s)
    {
        LOGGER.info("Call to StructureController.changeOrder with name ={}", s.getName());
        this.reorderElementsService.reorder(s);
        s.getElements().forEach(el -> el.getSignature().setModification("SOLODOUV"));
        Structure savedStructure;
        try{
            savedStructure = this.structureRepository.save(s);
            LOGGER.info("Structure has been saved.");
            LOGGER.info("Saved structure = {}", savedStructure);
        } catch (Exception e) {
            LOGGER.error("Could save reordered structure : " + s, e);
            return ResponseEntity.badRequest().body(new ErrorResponse(400, e.getMessage()));
        }

        return ResponseEntity.ok(savedStructure);
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody Structure s) //TODO element is not removed....
    {
        LOGGER.info("Call to StructureController.update with name = {}", s.getName());
        LOGGER.info("We have {} elements.", s.getElements().size());
        Optional<Structure> found = this.structureRepository.findById(s.getName());
        if(found.isPresent()){
            LOGGER.info("Found has {} elements.", found.get().getElements().size());
            found.get().setTag(s.getTag().orElse(null));
            found.get().setDescription(s.getDescription());
            found.get().getSignature().setModifiedBy("SOLODOUV");

            LOGGER.info("On va faire {} boucles.", s.getElements().size());
            int cpt = 0;
            for(StructureElement receivedElement: s.getElements()) {
                LOGGER.info("Boucle ....");
                if(receivedElement.getId() != null && receivedElement.getId() != 0L) { // Existing element
                    for(StructureElement existing: found.get().getElements()) {
                        if(existing.getId().equals(receivedElement.getId())) { // found in the original object
                            LOGGER.info("On a trouvé notre bonheur: {}", existing.getId());
                            existing.setSequence(cpt);
                            existing.getSignature().setModification("SOLODOUV");
                            break;
                        }
                    }
                }
                else { // new element
                    LOGGER.info("C'est un tout nouvel élément: {}", receivedElement);
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

            LOGGER.info("Structure to save : {}", found.get());

            Structure saved = null;
            try{
                LOGGER.info("Before save found has {} elements", found.get().getElements().size());
                saved = this.structureRepository.save(found.get());
            } catch (Exception e) {
                LOGGER.error("An error occurred", e);
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
    public ResponseEntity<?> copyStructure(@RequestParam("from") String from, @RequestParam("to") String to)
    {
        LOGGER.info("Call to StructuresController.copy from = '{}', to = '{}'", from, to);
        Optional<Structure> fromStructure = this.structureRepository.findById(from);
        if(fromStructure.isPresent() && to != null && !to.trim().equalsIgnoreCase("")) {
            Structure newStructure = this.copyService.createCopyStructure(fromStructure.get(), to);
            newStructure.setSignature(new PersistenceSignature("SOLODOUV"));
            LOGGER.info("Copy structure = {}", newStructure);
            Structure saved;
            try {
                saved = this.structureRepository.save(newStructure);
            }
            catch (Exception e){
                LOGGER.error("An error occurred", e);
                return ResponseEntity.badRequest().body(new ErrorResponse(400, e.getMessage()));
            }

            return ResponseEntity.ok(saved);
        }
        else
        {
            return ResponseEntity.badRequest().body(new ErrorResponse(400, "Could not find structure " + from + " or new structure name is invalid."));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam("name") String name)
    {
        LOGGER.info("Call to StructuresController.delete with name = {}", name);
        try{
            this.structureRepository.deleteById(name);
        } catch (Exception e) {
            LOGGER.error("An error occurred", e);
            return ResponseEntity.badRequest().body(new ErrorResponse(400, e.getMessage()));
        }
        return ResponseEntity.ok().build();
    }
}
