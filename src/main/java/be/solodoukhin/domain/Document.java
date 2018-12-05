package be.solodoukhin.domain;

import be.solodoukhin.domain.embeddable.Label;
import be.solodoukhin.domain.embeddable.PersistenceSignature;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Author: Solodoukhin Viktor
 * Date: 28.11.18
 * Description: TODO
 */
@Entity
@Table(name = "DOCUMENT")
public class Document implements Serializable {

    @Id
    @Column(name = "NO_DOCUMENT")
    private Integer number;

    @Embedded
    private Label label;

    @OneToMany
    @JoinColumn(name = "NO_DOCUMENT", referencedColumnName = "NO_DOCUMENT")
    private List<Version> versions;

    @ManyToOne
    @JoinColumn(name = "NO_CATEGORIE")
    private DocumentCategory category;

    @Embedded
    private PersistenceSignature signature;

    public Document() {}

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
        return versions;
    }

    public void setVersions(List<Version> versions) {
        this.versions = versions;
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
                ", category=" + category +
                '}';
    }
}
