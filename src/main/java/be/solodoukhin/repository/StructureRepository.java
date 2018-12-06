package be.solodoukhin.repository;

import be.solodoukhin.domain.Structure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Author: Solodoukhin Viktor
 * Date: 29.11.18
 * Description: TODO
 */
@Repository
public interface StructureRepository extends JpaRepository<Structure, String> {}
