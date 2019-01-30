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
    private String type = "TYPE";

    @XmlElement(name = "ID_AFFILIE", required = true)
    private String affiliateIdentifier = "ID_AFFILIE";

    @XmlElement(name = "ID_CPL", required = true)
    private String complementaryIdentifier = "ID_CPL";

    @XmlElement(name = "LANGUE", required = true)
    private String language = "LANGUE";

    @XmlElement(name = "DT_PRODUCTION", required = true)
    private String productionDate = "DT_PRODUCTION";

    @XmlElement(name = "DT_EXPLOITATION", required = true)
    private String operatingDate = "DT_EXPLOITATION";

    @XmlElement(name = "ID_PLANNING", required = true)
    private String planningIdentifier = "ID_PLANNING";

    @XmlElement(name = "ID_DB", required = true)
    private String databaseIdentifier = "ID_DB";

    @XmlElement(name = "ID_APPLICATION", required = true)
    private String applicationIdentifier = "ID_APPLICATION";

    @XmlElement(name = "VARIANTE", required = true)
    private String variant = "VARIANTE";

    @XmlElement(name = "EXPEDITION", required = true)
    private String shipping = "EXPEDITION";

    @XmlElement(name = "ARCHIVE", required = true)
    private String archive = "ARCHIVE";
}
