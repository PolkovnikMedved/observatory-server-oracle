package be.solodoukhin.controller;

import be.solodoukhin.domain.Version;
import be.solodoukhin.repository.VersionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/all")
    Iterable<Version> getAll() {
        LOGGER.info("Call to VersionController.getAll");
        return this.versionRepository.findAll();
    }
}
