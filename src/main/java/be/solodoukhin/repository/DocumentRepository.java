package be.solodoukhin.repository;

import be.solodoukhin.domain.Document;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Author: Solodoukhin Viktor
 * Date: 28.11.18
 * Description: TODO
 */
@Repository
public interface DocumentRepository extends CrudRepository<Document, Integer> {}
