package be.solodoukhin.domain.xml;

import be.solodoukhin.domain.persistent.Structure;
import be.solodoukhin.domain.persistent.StructureElement;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.*;
import javax.xml.namespace.QName;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Brief description here....
 *
 * @author viktor.solodoukhin@groups.be
 * @since 2019.01.29
 */
@XmlRootElement
@XmlAccessorType(value = XmlAccessType.PROPERTY)
@XmlSeeAlso({ElementXml.class})
public class StructureXml implements Serializable {

    private String name;
    private String tag;
    private boolean empty;
    private List<ElementXml> elementXmls = new ArrayList<>();

    public StructureXml() {}

    public StructureXml(Structure structure, boolean empty) {
        this.tag = structure.getTag();
        this.name = structure.getName();
        this.empty = empty;
        for (StructureElement el : structure.getElements()) {
            this.elementXmls.add(new ElementXml(el, empty));
        }
    }

    @XmlAnyElement
    public List<JAXBElement<?>> getElements() {
        List<JAXBElement<?>> nodes = new ArrayList<>();
        for(ElementXml node: this.elementXmls) {
            if(node.getType() == null) {
                if(this.empty) {
                    nodes.add(new JAXBElement<>(new QName(node.getTag()), String.class, "0"));
                } else {
                    nodes.add(new JAXBElement<>(new QName(node.getTag()), String.class, node.getTag()));
                }
            } else {
                String structureTag;
                if(node.getTag() != null && !node.getTag().isEmpty()) {
                    structureTag = node.getTag();
                }
                else if(node.getType().getName() != null) {
                    structureTag = node.getType().getName();
                } else {
                    structureTag = "ERROR_TAG";
                }
                nodes.add(new JAXBElement<>(new QName(structureTag), StructureXml.class, node.getType()));
            }
        }
        return nodes;
    }

    @XmlTransient
    public String getTag() {
        return this.tag;
    }

    @XmlTransient
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "StructureXml{" +
                "tag='" + tag + '\'' +
                ", elementXmls=" + elementXmls +
                '}';
    }
}
