package be.solodoukhin.repository;

import be.solodoukhin.domain.persistent.Structure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Author: Solodoukhin Viktor
 * Date: 29.11.18
 * Description: Repository for Structure entity containing Spring Data methods
 */
@Repository
public interface StructureRepository extends JpaRepository<Structure, String> {
    @Query("SELECT s.name FROM Structure s ORDER BY s.name")
    List<String> getAllStructureNames();

    @Query("SELECT CASE WHEN (COUNT(s) > 0) THEN TRUE ELSE FALSE END FROM Structure s JOIN s.elements e WHERE e.typeStructure.name = :name")
    boolean isUsedAsType(@Param("name") String name);
}
