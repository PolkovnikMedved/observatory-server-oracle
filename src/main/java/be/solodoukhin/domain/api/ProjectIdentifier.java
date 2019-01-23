package be.solodoukhin.domain.api;

import java.io.Serializable;

/**
 * Author: Solodoukhin Viktor
 * Date: 29.11.18
 * Description: class used in welcome controller ( url "/" sends the project identifier)
 */
public class ProjectIdentifier implements Serializable {
    private final String projectName;
    private final String projectVersion;

    public ProjectIdentifier(String projectName, String projectVersion) {
        this.projectName = projectName;
        this.projectVersion = projectVersion;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getProjectVersion() {
        return projectVersion;
    }
}
