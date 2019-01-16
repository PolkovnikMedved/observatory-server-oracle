package be.solodoukhin.repository;

import be.solodoukhin.domain.Version;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Author: Solodoukhin Viktor
 * Date: 29.11.18
 * Description: Repository for Version entity containing Spring Data methods
 */
@Repository
public interface VersionRepository extends CrudRepository<Version, String> {}
