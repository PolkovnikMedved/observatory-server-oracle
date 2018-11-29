package be.solodoukhin.domain.embeddable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDate;

/**
 * Author: Solodoukhin Viktor
 * Date: 28.11.18
 * Description: TODO
 */
@Embeddable
public class PersistenceSignature {

    @Column(name = "AUTEUR_CREATION")
    private String createdBy;

    @Column(name = "DT_CREATION")
    private LocalDate createdAt;

    @Column(name = "AUTEUR_MODIFICATION")
    private String modifiedBy;

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
