package be.solodoukhin.domain;

import be.solodoukhin.domain.embeddable.Label;

import javax.persistence.*;

/**
 * Author: Solodoukhin Viktor
 * Date: 28.11.18
 * Description: TODO
 */
@Entity
@Table(name = "CATEGORIE_DOCUMENTS")
public class DocumentCategory {

    @Id
    @Column(name = "NO_CATEGORIE")
    private Integer number;

    @Embedded
    private Label label;

    public DocumentCategory() {}

    public DocumentCategory(int number, Label label) {
        this.number = number;
        this.label = label;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "DocumentCategory{" +
                "number=" + number +
                ", label=" + label +
                '}';
    }
}
