package be.solodoukhin.repository;

import be.solodoukhin.domain.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Author: Solodoukhin Viktor
 * Date: 28.11.18
 * Description: TODO
 */
@Repository
public interface DocumentRepository extends JpaRepository<Document, Integer> {

    @Query("select d from Document d where " +
            "d.number = :documentNumber " +
            "or d.label.frenchLabel like %:documentName% " +
            "or d.label.dutchLabel like %:documentName% " +
            "or d.category.label.frenchLabel like %:documentCategory% " +
            "or d.category.label.dutchLabel like %:documentCategory%")
    Page<Document> getAllByFilter(
            @Param(value = "documentNumber") Integer documentNumber,
            //@Param(value = "dfaNumber") String dfaNumber,
            @Param(value = "documentName") String documentName,
            @Param(value = "documentCategory") String documentCategory,
            //@Param(value = "structureName") String structureName,
            //@Param(value = "author") String author,
            Pageable pageable
    );
}
