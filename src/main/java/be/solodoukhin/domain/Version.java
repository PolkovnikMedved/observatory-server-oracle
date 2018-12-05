package be.solodoukhin.domain;

import be.solodoukhin.domain.embeddable.PersistenceSignature;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Author: Solodoukhin Viktor
 * Date: 28.11.18
 * Description: TODO
 */
@Entity
@Table(name = "OBS_VERSION2")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "name")
public class Version implements Serializable {
    @Id
    @Column(name = "NO_VERSION")
    private String name;

    @Column(name = "NO_DFA")
    private String dfaName;

    @Column(name = "DESCRIPTION")
    private String description;

    @ManyToOne
    @JoinColumn(name = "NO_DOCUMENT")
    private Document document;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
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
