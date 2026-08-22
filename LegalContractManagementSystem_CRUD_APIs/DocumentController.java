package com.legalcontract.legal_contract_management_system.controller;

import com.legalcontract.legal_contract_management_system.entity.Document;
import com.legalcontract.legal_contract_management_system.service.DocumentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class DocumentController
{

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService)
    {
        this.documentService = documentService;
    }

    @PostMapping
    public ResponseEntity<Document> createDocument(
            @RequestBody Document document)
    {
        return ResponseEntity.ok(documentService.createDocument(document));
    }

    @GetMapping
    public ResponseEntity<List<Document>> getAllDocuments()
    {
        return ResponseEntity.ok(documentService.getAllDocuments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Document> getDocumentById(
            @PathVariable Long id)
    {

        return documentService.getDocumentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Document> updateDocument(
            @PathVariable Long id,
            @RequestBody Document document)
    {

        return ResponseEntity.ok(
                documentService.updateDocument(id, document));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(
            @PathVariable Long id)
    {

        documentService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }
}