package be.solodoukhin.domain;

import be.solodoukhin.domain.embeddable.PersistenceSignature;
import com.fasterxml.jackson.annotation.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;

/**
 * Author: Solodoukhin Viktor
 * Date: 28.11.18
 * Description: TODO
 */
@Entity
@Table(name = "OBS_STRUCTURE_ELEMENT2")
@SequenceGenerator(name = "structure_element_seq", sequenceName = "structure_element_seq", allocationSize = 1)
public class StructureElement {

    @Id
    @Column(name = "NO_ELEMENT")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "structure_element_seq")
    private long id;

    @Column(name = "TAG")
    private String tag;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "SUITE")
    private int sequence;

    @Column(name = "OPTIONNEL")
    @Type(type = "true_false")
    private boolean optional;

    @Column(name = "REPETITIF")
    @Type(type = "true_false")
    private boolean repetitive;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "NOM_STRUCTURE_PARENT")
    private Structure parentStructure;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "NOM_STRUCTURE_TYPE")
    private Structure typeStructure;

    @Embedded
    private PersistenceSignature signature;

    public StructureElement() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public boolean isOptional() {
        return optional;
    }

    public void setOptional(boolean optional) {
        this.optional = optional;
    }

    public boolean isRepetitive() {
        return repetitive;
    }

    public void setRepetitive(boolean repetitive) {
        this.repetitive = repetitive;
    }

    public Structure getParentStructure() {
        return parentStructure;
    }

    public void setParentStructure(Structure parentStructure) {
        this.parentStructure = parentStructure;
    }

    public Structure getTypeStructure() {
        return typeStructure;
    }

    public void setTypeStructure(Structure typeStructure) {
        this.typeStructure = typeStructure;
    }

    public PersistenceSignature getSignature() {
        return signature;
    }

    public void setSignature(PersistenceSignature signature) {
        this.signature = signature;
    }

    @Override
    public String toString() {
        return "StructureElement{" +
                "id=" + id +
                ", tag='" + tag + '\'' +
                ", description='" + description + '\'' +
                ", sequence=" + sequence +
                ", optional=" + optional +
                ", repetitive=" + repetitive +
                ", typeStructure=" + typeStructure +
                ", signature=" + signature +
                '}';
    }
}
