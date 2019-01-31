package be.solodoukhin.domain.dto;

import be.solodoukhin.domain.persistent.Document;
import be.solodoukhin.domain.persistent.Version;
import be.solodoukhin.domain.persistent.embeddable.Label;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Brief description here....
 *
 * @author viktor.solodoukhin@groups.be
 * @since 2019.01.23
 */
@Data
@NoArgsConstructor
public class DocumentDTO {
    private Integer number;
    private Label label;
    private CategoryDTO category;
    private List<VersionDTO> versions;

    public DocumentDTO(Document document) {
        this.number = document.getNumber();
        this.label = document.getLabel();

        if(document.getCategory() != null) {
            this.category = new CategoryDTO(document.getCategory().getNumber(), document.getCategory().getLabel());
        }

        if(document.getVersions() != null) {
            for(Version version: document.getVersions()) {
                this.addVersion(new VersionDTO(version));
            }
        }
    }

    public void addVersion(VersionDTO versionDTO) {
        if(this.versions == null) {
            this.versions = new ArrayList<>();
        }
        this.versions.add(versionDTO);
    }
}
