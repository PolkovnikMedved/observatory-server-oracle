package be.solodoukhin.service;

import be.solodoukhin.domain.persistent.Structure;
import be.solodoukhin.domain.persistent.Version;
import org.springframework.stereotype.Service;

/**
 * Brief description here....
 *
 * @author viktor.solodoukhin@groups.be
 * @since 2018.12.06
 */
@Service
public class CopyService {

    public Version createCopyVersion(Version base, String newVersionName)
    {
        Version copy = new Version();
        copy.setName(newVersionName);
        copy.setDescription(base.getDescription());
        copy.setDfaName(base.getDfaName().orElse(null));
        copy.setStructure(base.getStructure());
        return copy;
    }

    public Structure createCopyStructure(Structure base, String newStructureName)
    {
        Structure structure = new Structure(base);
        structure.setName(newStructureName);
        structure.getElements().forEach(el -> el.setId(null));

        return structure;
    }
}
