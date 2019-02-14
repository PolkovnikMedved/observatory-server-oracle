package be.solodoukhin.controller;

import be.solodoukhin.domain.dto.VersionDTO;
import be.solodoukhin.domain.persistent.Document;
import be.solodoukhin.domain.persistent.Version;
import be.solodoukhin.domain.persistent.embeddable.PersistenceSignature;
import be.solodoukhin.exception.ApiGenericException;
import be.solodoukhin.repository.DocumentRepository;
import be.solodoukhin.repository.VersionRepository;
import be.solodoukhin.service.CopyService;
import be.solodoukhin.service.converter.VersionConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

/**
 * Author: Solodoukhin Viktor
 * Date: 01.12.18
 * Description: Version REST methods
 */
@Slf4j
@RestController
@RequestMapping(RoutingMapping.PROTECTED_URL_VERSION)
public class VersionController {
    private final VersionConverter converter;
    private final VersionRepository versionRepository;
    private final DocumentRepository documentRepository;
    private final CopyService copyService;

    @Autowired
    public VersionController(VersionRepository versionRepository, DocumentRepository documentRepository, CopyService copyService, VersionConverter converter) {
        this.versionRepository = versionRepository;
        this.documentRepository = documentRepository;
        this.copyService = copyService;
        this.converter = converter;
    }

    @GetMapping("/{name}")
    public ResponseEntity<Version> getOne(@PathVariable String name)
    {
        log.info("Call to VersionController.getOne with name = '{}'", name);
        Optional<Version> version = this.versionRepository.findById(name);
        if(!version.isPresent()) {
            log.warn("Could not find version with name = '{}'", name);
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(version.get());
    }

    @PutMapping("/update")
    public ResponseEntity<Version> update(@RequestBody @Valid VersionDTO v)
    {
        log.info("Call to VersionController.update with version id = {}", v.getName());

        Optional<Version> version;
        try{
            version = this.converter.toUpdatedPersistable(v);
        } catch (ApiGenericException e) {
            log.warn("Could not convert DTO to Version", e);
            return ResponseEntity.badRequest().build();
        }

        if(!version.isPresent()) {
            log.warn("Could not find Version from DTO with name = '{}'", v.getName());
            return ResponseEntity.badRequest().build();
        }

        try{
            return ResponseEntity.ok(this.versionRepository.save(version.get()));
        } catch (Exception e) {
            log.error("Could not save the Version", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/copy")
    public ResponseEntity<Document> copyVersion(@RequestParam("from") String from, @RequestParam("to") String to, Principal principal)
    {
        log.info("Call to VersionController.copyVersion from = '{}' to '{}'", from, to);
        // Get the document that contains the version (version has no link to the document)
        Optional<Document> document = this.documentRepository.findByVersion(from);

        if(document.isPresent()) {
            // Get the version from document list
            Optional<Version> fromVersion = document.get().getVersions().stream().filter(version1 -> from.equals(version1.getName())).findAny();

            if(fromVersion.isPresent()){
                // Copy and save
                Version toVersion = this.copyService.createCopyVersion(fromVersion.get(), to);
                toVersion.setSignature(new PersistenceSignature(principal.getName()));
                toVersion.getSignature().setModification(principal.getName());
                document.get().addVersion(toVersion);

                Document saved;
                try{
                    saved = this.documentRepository.save(document.get());
                } catch (Exception e) {
                    log.error("Could not save document when trying to copy version {} to {}", from, to);
                    return ResponseEntity.badRequest().build();
                }

                return ResponseEntity.ok(saved);
            }
            else
            {
                log.warn("Could not find version {}", from);
                return ResponseEntity.badRequest().build();
            }
        }
        else
        {
            log.warn("Could not find document for version {}", from);
            return ResponseEntity.badRequest().build();
        }
    }
}
