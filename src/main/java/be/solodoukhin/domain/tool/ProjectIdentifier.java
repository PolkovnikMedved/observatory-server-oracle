package be.solodoukhin.domain.tool;

/**
 * Author: Solodoukhin Viktor
 * Date: 29.11.18
 * Description: class used in welcome controller ( url / sends the project identifier)
 */
public class ProjectIdentifier {
    private final String projectName;
    private final String projectVersion;

    public ProjectIdentifier(String projectName, String projectVersion) {
        this.projectName = projectName;
        this.projectVersion = projectVersion;
    }

    String getProjectName() {
        return projectName;
    }

    String getProjectVersion() {
        return projectVersion;
    }
}
