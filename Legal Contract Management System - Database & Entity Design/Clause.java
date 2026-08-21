package com.legalcontract.legal_contract_management_system.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "clauses")
public class Clause {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String clauseNumber;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String clauseTitle;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id", nullable = false)
    private Contract contract;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public Clause() {
    }

    public Clause(Long id, String clauseNumber, String clauseTitle,
                  String content, Contract contract,
                  LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.clauseNumber = clauseNumber;
        this.clauseTitle = clauseTitle;
        this.content = content;
        this.contract = contract;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClauseNumber() {
        return clauseNumber;
    }

    public void setClauseNumber(String clauseNumber) {
        this.clauseNumber = clauseNumber;
    }

    public String getClauseTitle() {
        return clauseTitle;
    }

    public void setClauseTitle(String clauseTitle) {
        this.clauseTitle = clauseTitle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
