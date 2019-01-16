package be.solodoukhin.repository;

import be.solodoukhin.domain.DocumentCategory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Author: Solodoukhin Viktor
 * Date: 28.11.18
 * Description: Repository for DocumentCategory entity containing Spring Data methods
 */
@Repository
public interface DocumentCategoryRepository extends CrudRepository<DocumentCategory, Integer> {}
