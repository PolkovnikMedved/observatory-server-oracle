package be.solodoukhin.domain.dto;

import be.solodoukhin.domain.persistent.embeddable.Label;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Brief description here....
 *
 * @author viktor.solodoukhin@groups.be
 * @since 2019.01.23
 */
@Data
@AllArgsConstructor
public class CategoryDTO {
    private Integer number;
    private Label label;
}
