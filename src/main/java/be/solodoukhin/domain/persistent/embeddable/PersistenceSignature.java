package be.solodoukhin.domain.persistent.embeddable;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Author: Solodoukhin Viktor
 * Date: 28.11.18
 * Description: this class is used to keep trace of the user and the date on creating/modifying data
 */
@Embeddable
public class PersistenceSignature implements Serializable {

    @JsonProperty(value="createdBy", required = true)
    @Column(name = "AUTEUR_CREATION")
    private String createdBy;

    @JsonProperty(value="createdAt", required = true)
    @Column(name = "DT_CREATION")
    private LocalDate createdAt;

    @JsonProperty(value="modifiedBy", required = true)
    @Column(name = "AUTEUR_MODIFICATION")
    private String modifiedBy;

    @JsonProperty(value="modifiedAt", required = true)
    @Column(name = "DT_MODIFICATION")
    private LocalDate modifiedAt;

    public PersistenceSignature() {}

    public PersistenceSignature(String createdBy) {
        this.createdBy = createdBy;
        this.createdAt = LocalDate.now();
    }

    public void setModification(String modifiedBy)
    {
        this.modifiedBy = modifiedBy;
        this.modifiedAt = LocalDate.now();
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    LocalDate getCreatedAt() {
        return createdAt;
    }

    void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    LocalDate getModifiedAt() {
        return modifiedAt;
    }

    void setModifiedAt(LocalDate modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    @Override
    public String toString() {
        return "PersistenceSignature{" +
                "createdBy='" + createdBy + '\'' +
                ", createdAt=" + createdAt +
                ", modifiedBy='" + modifiedBy + '\'' +
                ", modifiedAt=" + modifiedAt +
                '}';
    }
}
