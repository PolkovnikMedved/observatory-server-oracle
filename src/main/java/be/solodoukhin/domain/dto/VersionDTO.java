package be.solodoukhin.domain.dto;

import be.solodoukhin.domain.persistent.Version;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Brief description here....
 *
 * @author viktor.solodoukhin@groups.be
 * @since 2019.01.23
 */
@Data
@NoArgsConstructor
public class VersionDTO implements Comparable<VersionDTO>{
    @NotNull
    @Size(min = 2, max = 20)
    private String name;

    @Size(max = 20)
    private String dfaName;

    @Size(min = 2, max = 767)
    private String description;

    private StructureDTO structure;

    public VersionDTO(Version version) {
        this.name = version.getName();
        this.dfaName = version.getDfaName().orElse(null);
        this.description = version.getDescription();
        if(version.getStructure() != null) {
            this.structure = new StructureDTO(version.getStructure());
        }
    }

    @Override
    public int compareTo(VersionDTO o) {
        return this.name.compareTo(o.name);
    }
}
