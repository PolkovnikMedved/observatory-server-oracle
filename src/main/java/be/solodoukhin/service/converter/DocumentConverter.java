package be.solodoukhin.service.converter;

import be.solodoukhin.domain.dto.DocumentDTO;
import be.solodoukhin.domain.dto.VersionDTO;
import be.solodoukhin.domain.persistent.Document;
import be.solodoukhin.domain.persistent.Version;
import be.solodoukhin.domain.persistent.embeddable.PersistenceSignature;
import be.solodoukhin.repository.StructureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;

/**
 * Brief description here....
 *
 * @author viktor.solodoukhin@groups.be
 * @since 2019.01.23
 */
@Service
public class DocumentConverter {

    private final StructureRepository structureRepository;

    @Autowired
    public DocumentConverter(StructureRepository structureRepository) {
        this.structureRepository = structureRepository;
    }

    public DocumentDTO toDTO(Document document) {
        return new DocumentDTO(document);
    }

    public void updateDocumentFromDTO(Document document, DocumentDTO dto, String modificationAuthor) {
        // Update existent
        for(VersionDTO versionDTO: dto.getVersions()) {
            for(Version version: document.getVersions()) {
                if(versionDTO.getName().equals(version.getName())) {

                    if(version.getDfaName().isPresent() && !versionDTO.getDfaName().equalsIgnoreCase(version.getDfaName().get())) {
                        // Update DFA
                        version.setDfaName(versionDTO.getDfaName());
                        version.getSignature().setModification(modificationAuthor);
                    }

                    if(!version.getDescription().equals(versionDTO.getDescription())) {
                        // Update Description
                        version.setDescription(versionDTO.getDescription());
                        version.getSignature().setModification(modificationAuthor);
                    }

                }
            }
        }

        addVersions(document, dto, modificationAuthor);

        removeVersions(document, dto);
    }

    /*
        Here, we CHOOSE to persist version where the structure has not been found.
        Else, it's difficult to provide concrete error message to the user
     */
    private void addVersions(Document document, DocumentDTO dto, String modificationAuthor) {
        for(VersionDTO versionDTO: dto.getVersions()) {
            boolean found = false;
            for(Version version: document.getVersions()) {
                if(versionDTO.getName().equals(version.getName())) {
                    found = true;
                }
            }

            if(!found) {
                Version versionToCreate = new Version();
                versionToCreate.setName(versionDTO.getName());
                versionToCreate.setDescription(versionDTO.getDescription());
                versionToCreate.setDfaName(versionDTO.getDfaName());

                if(versionDTO.getStructure() != null && versionDTO.getStructure().getName() != null && this.structureRepository.existsById(versionDTO.getStructure().getName())) {
                    versionToCreate.setStructure(structureRepository.getOne(versionDTO.getStructure().getName()));
                }
                versionToCreate.setSignature(new PersistenceSignature(modificationAuthor));
                versionToCreate.getSignature().setModification(modificationAuthor);

                document.addVersion(versionToCreate);
            }
        }
    }

    private void removeVersions(Document document, DocumentDTO dto) {
        Iterator<Version> versionIterator = document.getVersions().iterator();
        while(versionIterator.hasNext()) {
            Version version = versionIterator.next();

            boolean found = false;
            for(VersionDTO versionDTO: dto.getVersions()) {
                if(versionDTO.getName().equals(version.getName())) {
                    found = true;
                }
            }

            if(!found) {
                versionIterator.remove();
            }
        }
    }
}
