package be.solodoukhin.controller;

import be.solodoukhin.domain.Document;
import be.solodoukhin.domain.Version;
import be.solodoukhin.domain.api.ErrorResponse;
import be.solodoukhin.domain.embeddable.PersistenceSignature;
import be.solodoukhin.repository.DocumentRepository;
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
 * Description: Version REST methods
 */
@RestController
@RequestMapping("/version")
public class VersionController {
    private static final Logger LOGGER = LoggerFactory.getLogger(VersionController.class);
    private final VersionRepository versionRepository;
    private final DocumentRepository documentRepository;
    private final CopyService copyService;

    @Autowired
    public VersionController(VersionRepository versionRepository, DocumentRepository documentRepository, CopyService copyService) {
        this.versionRepository = versionRepository;
        this.documentRepository = documentRepository;
        this.copyService = copyService;
    }

    // Not used, we use document.update since we cascade all operations for version
    @PostMapping("/add")
    public Version save(@RequestBody Version version){
        LOGGER.info("Call to VersionController.save with version id = {}", version.getName());
        version.setSignature(new PersistenceSignature("SOLODOUV"));
        return this.versionRepository.save(version);
    }

    @GetMapping("/{id}")
    public Version getOne(@PathVariable String id)
    {
        LOGGER.info("Call to VersionController.getOne with id = {}", id);
        return this.versionRepository.findById(id).orElse(null);
    }

    @PutMapping("/update")
    public ResponseEntity<Version> update(@RequestBody Version version)
    {
        LOGGER.info("Call to VersionController.update with version id = {}", version.getName());

        Optional<Version> v = this.versionRepository.findById(version.getName());
        if(v.isPresent()){
            v.get().setDfaName(version.getDfaName().orElse(null));
            v.get().setDescription(version.getDescription());
            v.get().setStructure(version.getStructure());
            v.get().getSignature().setModification("SOLODOUV");
            return ResponseEntity.ok(this.versionRepository.save(v.get()));
        }
        else
        {
            return ResponseEntity.badRequest().body(version);
        }
    }

    @GetMapping("/copy")
    public ResponseEntity<?> copyVersion(@RequestParam("from") String from, @RequestParam("to") String to)
    {
        LOGGER.info("Call to VersionController.copyVersion from = '{}' to '{}'", from, to);
        // Get the document that contains the version
        Optional<Document> document = this.documentRepository.findByVersion(from);

        if(document.isPresent()) {
            // Get the version from document list
            Optional<Version> fromVersion = document.get().getVersions().stream().filter(version1 -> from.equals(version1.getName())).findAny();

            if(fromVersion.isPresent()){
                // Copy and save
                Version toVersion = this.copyService.createCopyVersion(fromVersion.get(), to);
                toVersion.setSignature(new PersistenceSignature("SOLODOUV"));
                toVersion.getSignature().setModification("SOLODOUV");
                document.get().addVersion(toVersion);

                return ResponseEntity.ok(this.documentRepository.save(document.get()));
            }
            else
            {
                LOGGER.warn("Could not find version {}", from);
                return ResponseEntity.badRequest().body(new ErrorResponse(400, "Could not find version"));
            }
        }
        else
        {
            LOGGER.warn("Could not find document for version {}", from);
            return ResponseEntity.badRequest().body(new ErrorResponse(400, "Could not find version document"));
        }
    }
}
