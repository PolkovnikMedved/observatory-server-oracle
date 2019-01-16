package be.solodoukhin.repository;

import be.solodoukhin.domain.StructureElement;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Author: Solodoukhin Viktor
 * Date: 29.11.18
 * Description: Repository for StructureElements entity containing Spring Data methods
 */
@Repository
public interface StructureElementRepository extends CrudRepository<StructureElement, Long> {}
