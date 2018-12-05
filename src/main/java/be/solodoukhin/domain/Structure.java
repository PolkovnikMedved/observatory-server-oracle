package be.solodoukhin.domain;

import be.solodoukhin.domain.embeddable.PersistenceSignature;
import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * Author: Solodoukhin Viktor
 * Date: 28.11.18
 * Description: TODO
 */
@Entity
@Table(name = "OBS_STRUCTURE")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "name")
public class Structure {

    @Id
    @Column(name = "NOM_STRUCTURE")
    private String name;

    @Column(name = "TAG")
    private String tag;

    @Column(name = "DESCRIPTION")
    private String description;

    @JsonIgnore
    @OneToMany(mappedBy = "structure")
    private Set<Version> versions;

    @OneToMany(mappedBy = "parentStructure")
    private List<StructureElement> children;

    @OneToMany(mappedBy = "typeStructure")
    private List<StructureElement> slaves;

    @Embedded
    private PersistenceSignature signature;

    public Structure() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Version> getVersions() {
        return versions;
    }

    public void setVersions(Set<Version> versions) {
        this.versions = versions;
    }

    public List<StructureElement> getChildren() {
        return children;
    }

    public void setChildren(List<StructureElement> children) {
        this.children = children;
    }

    public List<StructureElement> getSlaves() {
        return slaves;
    }

    public void setSlaves(List<StructureElement> slaves) {
        this.slaves = slaves;
    }

    public PersistenceSignature getSignature() {
        return signature;
    }

    public void setSignature(PersistenceSignature signature) {
        this.signature = signature;
    }

    @Override
    public String toString() {
        return "Structure{" +
                "name='" + name + '\'' +
                ", tag='" + tag + '\'' +
                ", description='" + description + '\'' +
                ", versions=" + versions +
                ", children=" + children +
                ", signature=" + signature +
                '}';
    }
}
