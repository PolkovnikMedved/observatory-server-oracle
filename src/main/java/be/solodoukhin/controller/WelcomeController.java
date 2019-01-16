package be.solodoukhin.controller;

import be.solodoukhin.domain.tool.ProjectIdentifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author: Solodoukhin Viktor
 * Date: 28.11.18
 * Description: this controller is used to see if the service is running and which version of the service is running
 */
@RestController
public class WelcomeController {

    private ProjectIdentifier projectIdentifier;

    public WelcomeController(@Value("${project.version}") String projectVersion, @Value("${project.name}") String projectName) {
        this.projectIdentifier = new ProjectIdentifier(projectName, projectVersion);
    }

    @GetMapping("/")
    public ProjectIdentifier welcome()
    {
        return this.projectIdentifier;
    }
}
