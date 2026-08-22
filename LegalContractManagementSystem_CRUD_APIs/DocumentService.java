package com.legalcontract.legal_contract_management_system.service;

import com.legalcontract.legal_contract_management_system.entity.Document;
import com.legalcontract.legal_contract_management_system.repository.DocumentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DocumentService
{

    private final DocumentRepository documentRepository;

    public DocumentService(DocumentRepository documentRepository)
    {
        this.documentRepository = documentRepository;
    }

    // CREATE
    public Document createDocument(Document document)
    {
        return documentRepository.save(document);
    }

    // READ ALL
    public List<Document> getAllDocuments()
    {
        return documentRepository.findAll();
    }

    // READ BY ID
    public Optional<Document> getDocumentById(Long id)
    {
        return documentRepository.findById(id);
    }

    // UPDATE
    public Document updateDocument(Long id, Document details)
    {
        Document document = documentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Document not found with id: " + id));

        document.setFileName(details.getFileName());
        document.setFilePath(details.getFilePath());
        document.setFileType(details.getFileType());

        return documentRepository.save(document);
    }

    // DELETE
    public void deleteDocument(Long id) {
        if (!documentRepository.existsById(id))
        {
            throw new RuntimeException("Document not found with id: " + id);
        }

        documentRepository.deleteById(id);
    }
}