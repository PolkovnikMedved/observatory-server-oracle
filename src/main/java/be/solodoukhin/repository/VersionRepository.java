package be.solodoukhin.repository;

import be.solodoukhin.domain.Version;
import org.springframework.data.repository.CrudRepository;

/**
 * Author: Solodoukhin Viktor
 * Date: 29.11.18
 * Description: TODO
 */
public interface VersionRepository extends CrudRepository<Version, String> {}
