package be.solodoukhin.repository;

import be.solodoukhin.domain.DocumentCategory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Author: Solodoukhin Viktor
 * Date: 28.11.18
 * Description: TODO
 */
@Repository
public interface DocumentCategoryRepository extends CrudRepository<DocumentCategory, Integer> {}
