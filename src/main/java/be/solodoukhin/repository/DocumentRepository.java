package be.solodoukhin.repository;

import be.solodoukhin.domain.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Author: Solodoukhin Viktor
 * Date: 28.11.18
 * Description: TODO
 */
@Repository
public interface DocumentRepository extends JpaRepository<Document, Integer> {}
