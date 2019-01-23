package be.solodoukhin.repository;

import be.solodoukhin.domain.persistent.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Author: Solodoukhin Viktor
 * Date: 28.11.18
 * Description: Repository for Document entity containing Spring Data methods
 */
@Repository
public interface DocumentRepository extends JpaRepository<Document, Integer> {

    @Query("select d from Document d where " +
            "d.number = :documentNumber " +
            "or d.label.frenchLabel like %:documentName% " +
            "or d.label.dutchLabel like %:documentName% " +
            "or d.category.label.frenchLabel like %:documentCategory% " +
            "or d.category.label.dutchLabel like %:documentCategory% " +
            "or d.signature.createdBy like %:author% " +
            "or d.signature.modifiedBy like %:author%")
    Page<Document> getAllByFilter(
            @Param(value = "documentNumber") Integer documentNumber,
            @Param(value = "documentName") String documentName,
            @Param(value = "documentCategory") String documentCategory,
            @Param(value = "author") String author,
            Pageable pageable
    );

    @Query("select d from Document d inner join d.category")
    Page<Document> getAllWithCategory(Pageable pageable);

    @Query("select distinct d from Document d inner join d.versions v where v.name = :versionName")
    Optional<Document> findByVersion(@Param("versionName") String versionName);
}
