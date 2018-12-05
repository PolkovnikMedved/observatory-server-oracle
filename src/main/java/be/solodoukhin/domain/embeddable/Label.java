package be.solodoukhin.domain.embeddable;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Author: Solodoukhin Viktor
 * Date: 28.11.18
 * Description: TODO
 */
@Embeddable
public class Label implements Serializable {

    @JsonProperty(value="frenchLabel", required = true)
    @Column(name = "LIBELLE_F")
    private String frenchLabel;

    @JsonProperty(value="dutchLabel", required = true)
    @Column(name = "LIBELLE_N")
    private String dutchLabel;

    @JsonProperty(value="englishLabel")
    @Column(name = "LIBELLE_X")
    private String englishLabel;

    @JsonProperty(value="germanLabel")
    @Column(name = "LIBELLE_D")
    private String germanLabel;

    public Label() {}

    public Label(String frenchLabel, String dutchLabel, String englishLabel, String germanLabel) {
        this.frenchLabel = frenchLabel;
        this.dutchLabel = dutchLabel;
        this.englishLabel = englishLabel;
        this.germanLabel = germanLabel;
    }

    public String getFrenchLabel() {
        return frenchLabel;
    }

    public void setFrenchLabel(String frenchLabel) {
        this.frenchLabel = frenchLabel;
    }

    public String getDutchLabel() {
        return dutchLabel;
    }

    public void setDutchLabel(String dutchLabel) {
        this.dutchLabel = dutchLabel;
    }

    public String getEnglishLabel() {
        return englishLabel;
    }

    public void setEnglishLabel(String englishLabel) {
        this.englishLabel = englishLabel;
    }

    public String getGermanLabel() {
        return germanLabel;
    }

    public void setGermanLabel(String germanLabel) {
        this.germanLabel = germanLabel;
    }

    @Override
    public String toString() {
        return "Label{" +
                "frenchLabel='" + frenchLabel + '\'' +
                ", dutchLabel='" + dutchLabel + '\'' +
                ", englishLabel='" + englishLabel + '\'' +
                ", germanLabel='" + germanLabel + '\'' +
                '}';
    }
}
