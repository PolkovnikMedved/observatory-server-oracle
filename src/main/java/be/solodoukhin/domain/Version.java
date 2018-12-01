package be.solodoukhin.domain;

import be.solodoukhin.domain.embeddable.PersistenceSignature;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;

/**
 * Author: Solodoukhin Viktor
 * Date: 28.11.18
 * Description: TODO
 */
@Entity
@Table(name = "OBS_VERSION")
public class Version {
    @Id
    @Column(name = "NO_VERSION")
    private String name;

    @Column(name = "NO_DFA")
    private String dfaName;

    @Column(name = "NO_DOCUMENT")
    private int document;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "NOM_STRUCTURE")
    private Structure structure;

    @Embedded
    private PersistenceSignature signature;

    public Version() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDfaName() {
        return dfaName;
    }

    public void setDfaName(String dfaName) {
        this.dfaName = dfaName;
    }

    public int getDocument() {
        return document;
    }

    public void setDocument(int document) {
        this.document = document;
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
                ", document=" + document +
                ", structure=" + structure +
                ", signature=" + signature +
                '}';
    }
}
