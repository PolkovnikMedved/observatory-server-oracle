package be.solodoukhin.domain;

import be.solodoukhin.domain.embeddable.Label;

import javax.persistence.*;

/**
 * Author: Solodoukhin Viktor
 * Date: 28.11.18
 * Description: TODO
 */
@Entity
@Table(name = "DOCUMENT")
public class Document {

    @Id
    @Column(name = "NO_DOCUMENT")
    private int number;

    @Embedded
    private Label label;

    @ManyToOne
    @JoinColumn(name = "NO_CATEGORIE")
    private DocumentCategory category;

    public Document() {}

    public int getNumber() {
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

    public DocumentCategory getCategory() {
        return category;
    }

    public void setCategory(DocumentCategory category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Document{" +
                "number=" + number +
                ", label=" + label +
                ", category=" + category +
                '}';
    }
}
