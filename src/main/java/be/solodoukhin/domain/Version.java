package be.solodoukhin.domain;

import be.solodoukhin.domain.embeddable.PersistenceSignature;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Optional;

/**
 * Author: Solodoukhin Viktor
 * Date: 28.11.18
 * Description: Entity for obs_version table
 */
@Entity
@Table(name = "OBS_VERSION2")
public class Version implements Serializable {
    @Id
    @Column(name = "NO_VERSION")
    private String name;

    @Column(name = "NO_DFA")
    private String dfaName;

    @Column(name = "DESCRIPTION")
    private String description;

    @ManyToOne(cascade = { CascadeType.ALL })
    @JoinColumn(name = "NOM_STRUCTURE")
    private Structure structure;

    @Embedded
    private PersistenceSignature signature;

    /**
     * Empty constructor needed for Hibernate
     */
    public Version() { /* Just for Hibernate */}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Optional<String> getDfaName() {
        return Optional.ofNullable(dfaName);
    }

    public void setDfaName(String dfaName) {
        this.dfaName = dfaName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Structure getStructure() {
        return structure;
    }

    public void setStructure(Structure structure) {
        this.structure = structure;
    }

    public PersistenceSignature getSignature() {
        return signature;
    }

    public void setSignature(PersistenceSignature signature) {
        this.signature = signature;
    }

    @Override
    public String toString() {
        return "Version{" +
                "name='" + name + '\'' +
                ", dfaName='" + dfaName + '\'' +
                ", structure=" + structure +
                ", signature=" + signature +
                '}';
    }
}
