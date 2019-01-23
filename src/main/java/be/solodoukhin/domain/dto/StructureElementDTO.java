package be.solodoukhin.domain.dto;

import be.solodoukhin.domain.persistent.StructureElement;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Brief description here....
 *
 * @author viktor.solodoukhin@groups.be
 * @since 2019.01.23
 */
@Data
public class StructureElementDTO implements Comparable<StructureElementDTO>{
    @Digits(integer = 19, fraction = 0)
    private Long id;

    @Size(min = 2, max = 80)
    private String tag;

    @NotNull
    @Size(min = 2, max = 767)
    private String description;

    @Digits(integer = 5, fraction = 0)
    private Integer sequence;

    private boolean optional;

    private boolean repetitive;

    @Valid
    private StructureDTO typeStructure;

    public StructureElementDTO(StructureElement structureElement) {
        this.id = structureElement.getId();
        this.tag = structureElement.getTag().orElse(null);
        this.description = structureElement.getDescription();
        this.sequence = structureElement.getSequence();
        this.optional = structureElement.isOptional();
        this.repetitive = structureElement.isRepetitive();
        if(structureElement.getTypeStructure() != null) {
            this.typeStructure = new StructureDTO( structureElement.getTypeStructure() );
        }
    }

    @Override
    public int compareTo(StructureElementDTO o) {
        return Integer.compare(this.sequence, o.sequence);
    }
}
