package be.solodoukhin.controller;

import be.solodoukhin.domain.Version;
import be.solodoukhin.domain.embeddable.PersistenceSignature;
import be.solodoukhin.repository.VersionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    public VersionController(VersionRepository versionRepository) {
        this.versionRepository = versionRepository;
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
    public Version update(@RequestBody Version version)
    {
        LOGGER.info("Call to VersionController.update with version id = " + version.getName());
        version.getSignature().setModification("SOLODOUV");
        return this.versionRepository.save(version);
    }
}
