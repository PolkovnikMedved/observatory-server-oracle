package be.solodoukhin.domain;

import be.solodoukhin.domain.embeddable.PersistenceSignature;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Solodoukhin Viktor
 * Date: 28.11.18
 * Description: TODO
 */
@Entity
@Table(name = "OBS_STRUCTURE2")
public class Structure {

    @Id
    @Column(name = "NOM_STRUCTURE")
    private String name;

    @Column(name = "TAG")
    private String tag;

    @Column(name = "DESCRIPTION")
    private String description;

    @OrderBy("sequence ASC")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "NOM_STRUCTURE_PARENT")
    private List<StructureElement> elements;

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

    public List<StructureElement> getElements() {
        if(this.elements == null){
            this.elements = new ArrayList<>();
        }
        return elements;
    }

    public void setElements(List<StructureElement> elements) {
        this.elements = elements;
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
                ", elements=" + elements +
                ", signature=" + signature +
                '}';
    }
}
