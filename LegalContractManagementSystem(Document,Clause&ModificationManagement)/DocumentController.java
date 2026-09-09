package com.legalcontract.legal_contract_management_system.controller;

import com.legalcontract.legal_contract_management_system.entity.Document;
import com.legalcontract.legal_contract_management_system.service.DocumentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.nio.file.Path;
import java.nio.file.Paths;

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

    @PostMapping("/upload")
    public ResponseEntity<Document> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("contractId") Long contractId,
            @RequestParam("username") String username) {

        try {
            return ResponseEntity.ok(
                    documentService.uploadDocument(
                            file,
                            contractId,
                            username
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Document>> getAllDocuments()
    {
        return ResponseEntity.ok(documentService.getAllDocuments());
    }

    @GetMapping("/contract/{contractId}")
    public ResponseEntity<List<Document>> getDocumentsByContract(
            @PathVariable Long contractId,
            @RequestParam String username)
    {
        return ResponseEntity.ok(
                documentService.getDocumentsByContract(
                        contractId,
                        username
                )
        );
    }

    @GetMapping("/view/{id}")
    public ResponseEntity<Resource> viewDocument(
            @PathVariable Long id,
            @RequestParam String username) {

        Document document =
                documentService.getDocumentForUser(id, username);

        Path path =
                Paths.get(document.getFilePath());

        Resource resource =
                new FileSystemResource(path);

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" +
                                document.getFileName() +
                                "\""
                )
                .contentType(
                        MediaType.parseMediaType(
                                document.getFileType()
                        )
                )
                .body(resource);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadDocument(
            @PathVariable Long id,
            @RequestParam String username) {

        Document document =
                documentService.getDocumentForUser(id, username);

        Path path =
                Paths.get(document.getFilePath());

        Resource resource =
                new FileSystemResource(path);

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        MediaType mediaType = MediaType.APPLICATION_OCTET_STREAM;

        if (document.getFileType() != null &&
                !document.getFileType().isBlank()) {

            try {
                mediaType =
                        MediaType.parseMediaType(
                                document.getFileType()
                        );
            } catch (Exception ignored) {
                mediaType = MediaType.APPLICATION_OCTET_STREAM;
            }
        }

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" +
                                document.getFileName() +
                                "\""
                )
                .contentType(mediaType)
                .body(resource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(
            @PathVariable Long id,
            @RequestParam String username)
    {
        documentService.deleteDocument(id, username);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Document> getDocumentById(
            @PathVariable Long id)
    {

        Document document = documentService.getDocumentById(id);

        return ResponseEntity.ok(document);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Document> updateDocument(
            @PathVariable Long id,
            @RequestBody Document document)
    {

        return ResponseEntity.ok(
                documentService.updateDocument(id, document));
    }
}