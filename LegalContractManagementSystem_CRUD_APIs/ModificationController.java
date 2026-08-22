package com.legalcontract.legal_contract_management_system.controller;

import com.legalcontract.legal_contract_management_system.entity.Modification;
import com.legalcontract.legal_contract_management_system.service.ModificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/modifications")
public class ModificationController
{

    private final ModificationService modificationService;

    public ModificationController(ModificationService modificationService)
    {
        this.modificationService = modificationService;
    }

    @PostMapping
    public ResponseEntity<Modification> createModification(
            @RequestBody Modification modification)
    {

        return ResponseEntity.ok(
                modificationService.createModification(modification));
    }

    @GetMapping
    public ResponseEntity<List<Modification>> getAllModifications()
    {
        return ResponseEntity.ok(
                modificationService.getAllModifications());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Modification> getModificationById(
            @PathVariable Long id)
    {

        return modificationService.getModificationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Modification> updateModification(
            @PathVariable Long id,
            @RequestBody Modification modification)
    {

        return ResponseEntity.ok(
                modificationService.updateModification(id, modification));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteModification(
            @PathVariable Long id)
    {

        modificationService.deleteModification(id);
        return ResponseEntity.noContent().build();
    }
}