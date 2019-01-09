package be.solodoukhin.service;

import be.solodoukhin.domain.Structure;
import be.solodoukhin.domain.Version;
import be.solodoukhin.domain.embeddable.PersistenceSignature;
import org.springframework.stereotype.Service;

/**
 * Brief description here....
 *
 * @author viktor.solodoukhin@groups.be
 * @since 2018.12.06
 */
@Service
public class CopyService {

    //TODO returned copy has no signature but returned structure has signature....
    public Version createCopyVersion(Version base, String newVersionName)
    {
        Version copy = new Version();
        copy.setName(newVersionName);
        copy.setDescription(base.getDescription());
        copy.setDfaName(base.getDfaName());
        copy.setStructure(base.getStructure());
        return copy;
    }

    public Structure createCopyStructure(Structure base, String newStructureName)
    {
        Structure structure = new Structure(base);
        structure.setName(newStructureName);
        structure.setSignature(new PersistenceSignature("SOLODOUV"));
        structure.getElements().forEach(el -> {
            el.setId(null);
            el.setSignature(new PersistenceSignature("SOLODOUV"));
            el.getSignature().setModification("SOLODOUV");
        });

        return structure;
    }
}
