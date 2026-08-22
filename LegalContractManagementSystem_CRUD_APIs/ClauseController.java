package com.legalcontract.legal_contract_management_system.controller;

import com.legalcontract.legal_contract_management_system.entity.Clause;
import com.legalcontract.legal_contract_management_system.service.ClauseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clauses")
public class ClauseController
{

    private final ClauseService clauseService;

    public ClauseController(ClauseService clauseService)
    {
        this.clauseService = clauseService;
    }

    @PostMapping
    public ResponseEntity<Clause> createClause(
            @RequestBody Clause clause)
    {
        return ResponseEntity.ok(clauseService.createClause(clause));
    }

    @GetMapping
    public ResponseEntity<List<Clause>> getAllClauses()
    {
        return ResponseEntity.ok(clauseService.getAllClauses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Clause> getClauseById(
            @PathVariable Long id)
    {

        return clauseService.getClauseById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Clause> updateClause(
            @PathVariable Long id,
            @RequestBody Clause clause)
    {

        return ResponseEntity.ok(
                clauseService.updateClause(id, clause));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClause(
            @PathVariable Long id)
    {

        clauseService.deleteClause(id);
        return ResponseEntity.noContent().build();
    }
}