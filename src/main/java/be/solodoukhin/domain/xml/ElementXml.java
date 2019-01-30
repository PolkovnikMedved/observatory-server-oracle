package be.solodoukhin.domain.xml;

import be.solodoukhin.domain.persistent.StructureElement;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import java.io.Serializable;

/**
 * Brief description here....
 *
 * @author viktor.solodoukhin@groups.be
 * @since 2019.01.29
 */
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.PROPERTY)
@XmlSeeAlso({StructureXml.class})
public class ElementXml implements Serializable {

    private String tag;
    private StructureXml type;

    public ElementXml() {}

    public ElementXml(StructureElement structureElement, boolean empty) {
        this.tag = structureElement.getTag().orElse("");
        if(structureElement.getTypeStructure() != null) {
            this.type = new StructureXml(structureElement.getTypeStructure(), empty);
        }
    }

    public String getTag() {
        return this.tag;
    }

    public StructureXml getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return "ElementXml{" +
                "tag='" + tag + '\'' +
                ", type=" + type +
                '}';
    }
}
