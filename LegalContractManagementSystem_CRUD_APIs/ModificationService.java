package com.legalcontract.legal_contract_management_system.service;

import com.legalcontract.legal_contract_management_system.entity.Modification;
import com.legalcontract.legal_contract_management_system.repository.ModificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ModificationService
{

    private final ModificationRepository modificationRepository;

    public ModificationService(ModificationRepository modificationRepository)
    {
        this.modificationRepository = modificationRepository;
    }

    // CREATE
    public Modification createModification(Modification modification)
    {
        return modificationRepository.save(modification);
    }

    // READ ALL
    public List<Modification> getAllModifications()
    {
        return modificationRepository.findAll();
    }

    // READ BY ID
    public Optional<Modification> getModificationById(Long id)
    {
        return modificationRepository.findById(id);
    }

    // UPDATE
    public Modification updateModification(Long id, Modification details)
    {
        Modification modification = modificationRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Modification not found with id: " + id));

        modification.setOldContent(details.getOldContent());
        modification.setNewContent(details.getNewContent());
        modification.setReason(details.getReason());

        return modificationRepository.save(modification);
    }

    // DELETE
    public void deleteModification(Long id)
    {
        if (!modificationRepository.existsById(id))
        {
            throw new RuntimeException("Modification not found with id: " + id);
        }

        modificationRepository.deleteById(id);
    }
}