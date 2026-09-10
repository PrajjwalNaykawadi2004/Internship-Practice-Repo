package com.legalcontract.legal_contract_management_system.service;

import com.legalcontract.legal_contract_management_system.entity.Version;
import com.legalcontract.legal_contract_management_system.repository.VersionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VersionService
{

    private final VersionRepository versionRepository;

    public VersionService(VersionRepository versionRepository)
    {
        this.versionRepository = versionRepository;
    }

    // CREATE
    public Version createVersion(Version version)
    {
        return versionRepository.save(version);
    }

    // READ ALL
    public List<Version> getAllVersions()
    {
        return versionRepository.findAll();
    }

    // READ BY ID
    public Optional<Version> getVersionById(Long id)
    {
        return versionRepository.findById(id);
    }

    // UPDATE
    public Version updateVersion(Long id, Version details)
    {
        Version version = versionRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Version not found with id: " + id));

        version.setVersionNumber(details.getVersionNumber());
        version.setChangeSummary(details.getChangeSummary());
        version.setStatus(details.getStatus());

        return versionRepository.save(version);
    }

    // DELETE
    public void deleteVersion(Long id)
    {
        if (!versionRepository.existsById(id))
        {
            throw new RuntimeException("Version not found with id: " + id);
        }

        versionRepository.deleteById(id);
    }
}