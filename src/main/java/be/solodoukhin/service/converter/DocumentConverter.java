package be.solodoukhin.service.converter;

import be.solodoukhin.domain.dto.DocumentDTO;
import be.solodoukhin.domain.persistent.Document;
import org.springframework.stereotype.Service;

/**
 * Brief description here....
 *
 * @author viktor.solodoukhin@groups.be
 * @since 2019.01.23
 */
@Service
public class DocumentConverter {

    public DocumentDTO toDTO(Document document) {
        return new DocumentDTO(document);
    }
}
