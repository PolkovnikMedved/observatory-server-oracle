package be.solodoukhin.domain.persistent;

import be.solodoukhin.domain.persistent.embeddable.Label;
import be.solodoukhin.domain.persistent.embeddable.PersistenceSignature;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Solodoukhin Viktor
 * Date: 28.11.18
 * Description: Entity for table document. Should be read-only !
 */
@Entity
@Table(name = "DOCUMENT")
public class Document implements Serializable {

    @Id
    @Column(name = "NO_DOCUMENT")
    private Integer number;

    @Embedded
    private Label label;

    @OrderBy("name ASC")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "NO_DOCUMENT")
    private List<Version> versions;

    @Column(name = "FL_OBLIGATOIRE")
    private Integer mandatoryFlag;

    @Column(name = "FL_UN_SEUL_EX")
    private Integer uniqueCopy = 0;

    @Column(name = "FL_UN_EXEMPLAIRE_AU_MOINS")
    private Integer atLeastOneCopy = 0;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "NO_CATEGORIE")
    private DocumentCategory category;

    @Embedded
    private PersistenceSignature signature;

    public Document() { /* This constructor is used by Hibernate */}

    public Integer getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public List<Version> getVersions() {
        if(this.versions == null){
            this.versions = new ArrayList<>();
        }
        return versions;
    }

    public void addVersion(Version version) {
        if(this.versions == null){
            this.versions = new ArrayList<>();
        }
        this.versions.add(version);
    }

    public void setVersions(List<Version> versions) {
        this.versions = versions;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getMandatoryFlag() {
        return mandatoryFlag;
    }

    public void setMandatoryFlag(int mandatoryFlag) {
        this.mandatoryFlag = mandatoryFlag;
    }

    public Integer getUniqueCopy() {
        return uniqueCopy;
    }

    public void setUniqueCopy(int uniqueCopy) {
        this.uniqueCopy = uniqueCopy;
    }

    public Integer getAtLeastOneCopy() {
        return atLeastOneCopy;
    }

    public void setAtLeastOneCopy(int atLeastOneCopy) {
        this.atLeastOneCopy = atLeastOneCopy;
    }

    public DocumentCategory getCategory() {
        return category;
    }

    public void setCategory(DocumentCategory category) {
        this.category = category;
    }

    public PersistenceSignature getSignature() {
        return signature;
    }

    public void setSignature(PersistenceSignature signature) {
        this.signature = signature;
    }

    @Override
    public String toString() {
        return "Document{" +
                "number=" + number +
                ", label=" + label +
                ", versions=" + versions +
                ", mandatoryFlag=" + mandatoryFlag +
                ", uniqueCopy=" + uniqueCopy +
                ", atLeastOneCopy=" + atLeastOneCopy +
                ", category=" + category +
                ", signature=" + signature +
                '}';
    }
}
