package be.solodoukhin.service;

import be.solodoukhin.domain.Structure;
import be.solodoukhin.domain.Version;
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
        copy.setDfaName(base.getDfaName());
/*        copy.setDocument(base.getDocument());
        copy.setStructure(base.getStructure());*/
        return copy;
    }

    public Structure createCopyStructure(Structure base, String newStructureName)
    {
        Structure structure = new Structure();
        structure.setName(newStructureName);
        structure.setDescription(base.getDescription());
        structure.setTag(base.getTag());
/*        structure.setVersions(base.getVersions());*/
        structure.setChildren(base.getChildren());
        structure.setSlaves(base.getSlaves());
        return structure;
    }
}
