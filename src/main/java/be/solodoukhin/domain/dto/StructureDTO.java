package be.solodoukhin.domain.dto;

import be.solodoukhin.domain.persistent.Structure;
import be.solodoukhin.domain.persistent.StructureElement;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * Brief description here....
 *
 * @author viktor.solodoukhin@groups.be
 * @since 2019.01.23
 */
@Data
public class StructureDTO {
    @NotNull
    @Size(min = 2, max = 80)
    private String name;

    @Size(min = 2, max = 80)
    private String tag;

    @NotNull
    @Size(min = 2, max = 767)
    private String description;

    @Valid
    private List<StructureElementDTO> elements;

    public StructureDTO(Structure structure) {
        this.name = structure.getName();
        this.tag = structure.getTag().orElse(null);
        this.description = structure.getDescription();

        for(StructureElement element: structure.getElements()) {
            this.addElement(new StructureElementDTO(element));
        }
    }

    public void addElement(StructureElementDTO element) {
        if(this.elements == null) {
            this.elements = new ArrayList<>();
        }
        this.elements.add(element);
    }
}
