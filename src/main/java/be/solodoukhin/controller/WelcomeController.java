package be.solodoukhin.controller;

import be.solodoukhin.domain.tool.ProjectIdentifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author: Solodoukhin Viktor
 * Date: 28.11.18
 * Description: TODO
 */
@RestController
public class WelcomeController {

    private ProjectIdentifier projectIdentifier;

    /*@Value("${project.version}")
    private String projectVersion;

    @Value("${project.name}")
    private String projectName;*/

    public WelcomeController(@Value("${project.version}") String projectVersion, @Value("${project.name}") String projectName) {
        this.projectIdentifier = new ProjectIdentifier(projectName, projectVersion);
    }

    @GetMapping("/")
    public ProjectIdentifier welcome()
    {
        return this.projectIdentifier;
    }
}
