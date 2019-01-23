package be.solodoukhin.service.converter;

import be.solodoukhin.domain.dto.VersionDTO;
import be.solodoukhin.domain.persistent.Version;
import be.solodoukhin.exception.ApiGenericException;
import be.solodoukhin.repository.StructureRepository;
import be.solodoukhin.repository.VersionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Brief description here....
 *
 * @author viktor.solodoukhin@groups.be
 * @since 2019.01.23
 */
@Service
public class VersionConverter {

    private final VersionRepository versionRepository;
    private final StructureRepository structureRepository;

    @Autowired
    public VersionConverter(VersionRepository versionRepository, StructureRepository structureRepository) {
        this.versionRepository = versionRepository;
        this.structureRepository = structureRepository;
    }

    public VersionDTO toDTO(Version version) {
        return new VersionDTO(version);
    }

    public Version toNewPersistableVersion(VersionDTO dto) throws ApiGenericException {
        Version version = new Version();
        version.setName(dto.getName());
        version.setDfaName(dto.getDfaName());
        version.setDescription(dto.getDescription());

        if(dto.getStructure() != null && dto.getStructure().getName() != null) {
            if(structureRepository.existsById(dto.getStructure().getName())) {
                version.setStructure(this.structureRepository.getOne(dto.getStructure().getName()));
            } else {
                throw new ApiGenericException(String.format("Could not find Structure from name = '%s'", dto.getStructure().getName()));
            }
        }

        return version;
    }

    public Optional<Version> toUpdatedPersistable(VersionDTO dto) throws ApiGenericException {
        Optional<Version> version = this.versionRepository.findById(dto.getName());

        if(!version.isPresent()) {
            return Optional.empty();
        }

        version.get().setDfaName(dto.getDfaName());
        version.get().setDescription(dto.getDescription());

        if(dto.getStructure() != null && dto.getStructure().getName() != null) {
            if(structureRepository.existsById(dto.getStructure().getName())) {
                version.get().setStructure(structureRepository.getOne(dto.getStructure().getName()));
            } else {
                throw new ApiGenericException(String.format("Could not find Structure from name = '%s'", dto.getStructure().getName()));
            }
        }

        return version;
    }
}
