package be.solodoukhin.controller;

import be.solodoukhin.domain.Structure;
import be.solodoukhin.domain.embeddable.PersistenceSignature;
import be.solodoukhin.repository.StructureRepository;
import be.solodoukhin.service.StructureFilterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Brief description here....
 *
 * @author viktor.solodoukhin@groups.be
 * @since 2018.12.06
 */
@RestController
@RequestMapping("/structure")
public class StructuresController {

    private static final Logger LOGGER = LoggerFactory.getLogger(StructuresController.class);
    private final StructureRepository structureRepository;
    private final StructureFilterService structureFilterService;

    @Autowired
    public StructuresController(StructureRepository structureRepository, StructureFilterService structureFilterService) {
        this.structureRepository = structureRepository;
        this.structureFilterService = structureFilterService;
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
        LOGGER.info("Call to StructuresController.getAll with page = " + page);
        LOGGER.info("Parameters: name=" + name + ", tag=" + tag + ", description=" + description + ", createdBy=" + createdBy +", modifiedBy=" + modifiedBy);

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
        LOGGER.info("Call to StructuresController.getOne with name = " + name);
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
        LOGGER.info("Call to StructuresController.save with structure name  = " + s.getName());
        LOGGER.info("Call to StructuresController.save with structure tag   = " + s.getTag());
        LOGGER.info("Call to StructuresController.save with structure descr = " + s.getDescription());
        s.setSignature(new PersistenceSignature("SOLODOUV"));
        return this.structureRepository.save(s);
    }
}
