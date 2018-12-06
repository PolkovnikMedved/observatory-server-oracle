package be.solodoukhin.controller;

import be.solodoukhin.domain.Version;
import be.solodoukhin.domain.embeddable.PersistenceSignature;
import be.solodoukhin.repository.VersionRepository;
import be.solodoukhin.service.CopyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Author: Solodoukhin Viktor
 * Date: 01.12.18
 * Description: TODO
 */
@RestController
@RequestMapping("/version")
public class VersionController {
    private static final Logger LOGGER = LoggerFactory.getLogger(VersionController.class);
    private final VersionRepository versionRepository;
    private final CopyService copyService;

    @Autowired
    public VersionController(VersionRepository versionRepository, CopyService copyService) {
        this.versionRepository = versionRepository;
        this.copyService = copyService;
    }

    @PostMapping("/add")
    public Version save(@RequestBody Version version){
        LOGGER.info("Call to VersionController.save with version id = " + version.getName());
        version.setSignature(new PersistenceSignature("SOLODOUV"));
        return this.versionRepository.save(version);
    }

    @GetMapping("/{id}")
    public Version getOne(@PathVariable String id)
    {
        LOGGER.info("Call to VersionController.getOne with id = " + id);
        return this.versionRepository.findById(id).orElse(null);
    }

    @PutMapping("/update")
    public ResponseEntity<Version> update(@RequestBody Version version)
    {
        LOGGER.info("Call to VersionController.update with version id = " + version.getName());
        Optional<Version> v = this.versionRepository.findById(version.getName());

        if(v.isPresent()){
            v.get().setDfaName(version.getDfaName());
            v.get().setDescription(version.getDescription());
            v.get().getSignature().setModification("SOLODOUV");
            return ResponseEntity.ok(this.versionRepository.save(v.get()));
        }
        else
        {
            return ResponseEntity.badRequest().body(version);
        }
    }

    @GetMapping("/copy")
    public ResponseEntity<Version> copyVersion(@RequestParam("from") String from, @RequestParam("to") String to)
    {
        Optional<Version> fromVersion = this.versionRepository.findById(from);
        if(fromVersion.isPresent() && to != null && !to.equalsIgnoreCase("")){
            Version newVersion = this.copyService.createCopyVersion(fromVersion.get(), to);
            newVersion.setSignature(new PersistenceSignature("SOLODOUV"));
            return ResponseEntity.ok(this.versionRepository.save(newVersion));
        }
        else
        {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
