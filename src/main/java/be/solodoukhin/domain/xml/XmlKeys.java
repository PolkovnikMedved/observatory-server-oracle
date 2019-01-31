package be.solodoukhin.domain.xml;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * Brief description here....
 *
 * @author viktor.solodoukhin@groups.be
 * @since 2019.01.29
 */
@Data
@XmlType(name = "CLEFS", propOrder = {
        "type",
        "affiliateIdentifier",
        "complementaryIdentifier",
        "language",
        "productionDate",
        "operatingDate",
        "planningIdentifier",
        "databaseIdentifier",
        "applicationIdentifier",
        "variant",
        "shipping",
        "archive"})
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlKeys implements Serializable {
    @XmlElement(name = "TYPE", required = true)
    private String type;

    @XmlElement(name = "ID_AFFILIE", required = true)
    private String affiliateIdentifier;

    @XmlElement(name = "ID_CPL", required = true)
    private String complementaryIdentifier;

    @XmlElement(name = "LANGUE", required = true)
    private String language;

    @XmlElement(name = "DT_PRODUCTION", required = true)
    private String productionDate;

    @XmlElement(name = "DT_EXPLOITATION", required = true)
    private String operatingDate;

    @XmlElement(name = "ID_PLANNING", required = true)
    private String planningIdentifier;

    @XmlElement(name = "ID_DB", required = true)
    private String databaseIdentifier;

    @XmlElement(name = "ID_APPLICATION", required = true)
    private String applicationIdentifier;

    @XmlElement(name = "VARIANTE", required = true)
    private String variant;

    @XmlElement(name = "EXPEDITION", required = true)
    private String shipping;

    @XmlElement(name = "ARCHIVE", required = true)
    private String archive;

    public XmlKeys() {}

    public XmlKeys(boolean empty) {
        if(empty) {
            this.type = "0";
            this.affiliateIdentifier = "0";
            this.complementaryIdentifier = "0";
            this.language = "0";
            this.productionDate = "0";
            this.operatingDate = "0";
            this.planningIdentifier = "0";
            this.databaseIdentifier = "0";
            this.applicationIdentifier = "0";
            this.variant = "0";
            this.shipping = "0";
            this.archive = "0";
        } else {
            this.type = "TYPE";
            this.affiliateIdentifier = "ID_AFFILIE";
            this.complementaryIdentifier = "ID_CPL";
            this.language = "LANGUE";
            this.productionDate = "DT_PRODUCTION";
            this.operatingDate = "DT_EXPLOITATION";
            this.planningIdentifier = "ID_PLANNING";
            this.databaseIdentifier = "ID_DB";
            this.applicationIdentifier = "ID_APPLICATION";
            this.variant = "VARIANTE";
            this.shipping = "EXPEDITION";
            this.archive = "ARCHIVE";
        }
    }
}
