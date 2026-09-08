package com.legalcontract.legal_contract_management_system.service;

import com.legalcontract.legal_contract_management_system.entity.Document;
import com.legalcontract.legal_contract_management_system.repository.DocumentRepository;
import org.springframework.stereotype.Service;
import com.legalcontract.legal_contract_management_system.entity.Contract;
import com.legalcontract.legal_contract_management_system.entity.User;
import com.legalcontract.legal_contract_management_system.repository.ContractRepository;
import com.legalcontract.legal_contract_management_system.repository.UserRepository;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.List;
import java.util.Optional;

@Service
public class DocumentService
{

    private final DocumentRepository documentRepository;

    private final ContractRepository contractRepository;

    private final UserRepository userRepository;

    public DocumentService(
            DocumentRepository documentRepository,
            ContractRepository contractRepository,
            UserRepository userRepository)
    {
        this.documentRepository = documentRepository;
        this.contractRepository = contractRepository;
        this.userRepository = userRepository;
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

    // READ DOCUMENTS BY CONTRACT
    public List<Document> getDocumentsByContract(
            Long contractId,
            String username)
    {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found: " + username));

        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Contract not found with id: " + contractId));

        if (user.getRole() != null &&
                user.getRole().getName().equalsIgnoreCase("ContractManager"))
        {
            return documentRepository.findByContractId(contractId);
        }

        if (contract.getCreatedBy() == null ||
                !contract.getCreatedBy().getId().equals(user.getId()))
        {
            throw new RuntimeException(
                    "You are not authorized to view these documents");
        }

        return documentRepository.findByContractId(contractId);
    }

    // GET DOCUMENT BY ID
    public Document getDocumentById(Long id)
    {
        return documentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Document not found with id: " + id
                        )
                );
    }

    public Document getDocumentForUser(Long id, String username)
    {
        Document document =
                documentRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Document not found with id: " + id
                                ));

        User user =
                userRepository.findByUsername(username)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found: " + username
                                ));

        boolean isAdmin =
                user.getRole() != null &&
                        user.getRole().getName().equalsIgnoreCase("ContractManager");

        Contract contract =
                document.getContract();

        boolean isContractOwner =
                contract != null &&
                        contract.getCreatedBy() != null &&
                        contract.getCreatedBy().getId().equals(user.getId());

        if (!isAdmin && !isContractOwner)
        {
            throw new RuntimeException(
                    "You are not authorized to view this document"
            );
        }

        return document;
    }

    public void deleteDocument(Long id, String username)
    {
        Document document =
                documentRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Document not found with id: " + id
                                ));

        User user =
                userRepository.findByUsername(username)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found: " + username
                                ));

        Contract contract = document.getContract();

        boolean isAdmin =
                user.getRole() != null &&
                        user.getRole().getName().equalsIgnoreCase("ContractManager");

        boolean isContractOwner =
                contract != null &&
                        contract.getCreatedBy() != null &&
                        contract.getCreatedBy().getId().equals(user.getId());

        if (!isAdmin && !isContractOwner)
        {
            throw new RuntimeException(
                    "You are not authorized to delete this document"
            );
        }

        try {

            Path path =
                    Paths.get(document.getFilePath());

            Files.deleteIfExists(path);

        } catch (IOException e) {

            throw new RuntimeException(
                    "Failed to delete document file"
            );
        }

        documentRepository.delete(document);
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

    // UPLOAD DOCUMENT
    public Document uploadDocument(
            MultipartFile file,
            Long contractId,
            String username) throws IOException {

        if (file == null || file.isEmpty()) {
            throw new RuntimeException("Please select a file");
        }

        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Contract not found with id: " + contractId));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found: " + username));

        boolean isAdmin =
                user.getRole() != null &&
                        user.getRole().getName().equalsIgnoreCase("ContractManager");

        boolean isContractOwner =
                contract.getCreatedBy() != null &&
                        contract.getCreatedBy().getId().equals(user.getId());

        if (!isAdmin && !isContractOwner)
        {
            throw new RuntimeException(
                    "You are not authorized to upload a document to this contract"
            );
        }

        Path uploadDir = Paths.get("uploads");

        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        String originalFileName = file.getOriginalFilename();

        String fileName =
                System.currentTimeMillis() + "_" + originalFileName;

        Path filePath = uploadDir.resolve(fileName);

        Files.copy(
                file.getInputStream(),
                filePath
        );

        Document document = new Document();

        document.setFileName(originalFileName);
        document.setFileType(file.getContentType());
        document.setFilePath(filePath.toString());
        document.setContract(contract);
        document.setUploadedBy(user);

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