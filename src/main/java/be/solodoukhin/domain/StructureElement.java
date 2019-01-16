package be.solodoukhin.domain;

import be.solodoukhin.domain.converter.CharToBooleanConverter;
import be.solodoukhin.domain.embeddable.PersistenceSignature;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Optional;

/**
 * Author: Solodoukhin Viktor
 * Date: 28.11.18
 * Description: Entity for obs_structure_element table. After last deployment obs_structure_element2 should became obs_structure_element
 */
@Entity
@Table(name = "OBS_STRUCTURE_ELEMENT2")
@SequenceGenerator(name = "structure_element_seq", sequenceName = "structure_element_seq", allocationSize = 1)
public class StructureElement implements Serializable {

    @Id
    @Column(name = "NO_ELEMENT")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "structure_element_seq")
    private Long id;

    @Column(name = "TAG")
    private String tag;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "SUITE")
    private int sequence;

    @Column(name = "OPTIONNEL")
    @Convert(converter = CharToBooleanConverter.class)
    private boolean optional;

    @Column(name = "REPETITIF")
    @Convert(converter = CharToBooleanConverter.class)
    private boolean repetitive;

    @ManyToOne
    @JoinColumn(name = "NOM_STRUCTURE_TYPE")
    private Structure typeStructure;

    @Embedded
    private PersistenceSignature signature;

    public StructureElement() {}

    public StructureElement(StructureElement that) {
        this.id = that.getId();
        this.tag = that.getTag().orElse(null);
        this.description = that.getDescription();
        this.sequence = that.getSequence();
        this.optional = that.isOptional();
        this.repetitive = that.isRepetitive();
        if(that.getTypeStructure() != null) {
            this.typeStructure = new Structure(that.getTypeStructure());
        }
        this.signature = that.getSignature();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Optional<String> getTag() {
        return Optional.ofNullable(tag);
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
