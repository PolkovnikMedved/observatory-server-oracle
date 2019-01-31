package be.solodoukhin.domain.xml;

import be.solodoukhin.domain.persistent.Structure;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import java.io.Serializable;

/**
 * Brief description here....
 *
 * @author viktor.solodoukhin@groups.be
 * @since 2019.01.29
 */
@XmlRootElement(name = "DOCUMENT")
@XmlSeeAlso({StructureXml.class, XmlKeys.class})
public class StructureWrapper implements Serializable {

    @XmlElement(name = "CLEFS", required = true)
    private XmlKeys keys;

    @XmlElement(name = "CONTENU", required = true)
    private StructureXml structure;

    public StructureWrapper() {}

    public StructureWrapper(Structure structure, boolean empty) {
        this.keys = new XmlKeys(empty);
        this.structure = new StructureXml(structure, empty);
    }

    @Override
    public String toString() {
        return "StructureWrapper{" +
                "keys=" + keys +
                ", structure=" + structure +
                '}';
    }
}
