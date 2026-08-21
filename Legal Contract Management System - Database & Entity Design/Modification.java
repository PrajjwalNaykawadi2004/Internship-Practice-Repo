package com.legalcontract.legal_contract_management_system.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "modifications")
public class Modification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String oldContent;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String newContent;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String reason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clause_id", nullable = false)
    private Clause clause;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "version_id", nullable = false)
    private Version version;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modified_by", nullable = false)
    private User modifiedBy;

    private LocalDateTime modifiedAt;

    public Modification() {
    }

    public Modification(Long id, String oldContent, String newContent,
                        String reason, Clause clause, Version version,
                        User modifiedBy, LocalDateTime modifiedAt) {
        this.id = id;
        this.oldContent = oldContent;
        this.newContent = newContent;
        this.reason = reason;
        this.clause = clause;
        this.version = version;
        this.modifiedBy = modifiedBy;
        this.modifiedAt = modifiedAt;
    }

    @PrePersist
    protected void onCreate() {
        modifiedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOldContent() {
        return oldContent;
    }

    public void setOldContent(String oldContent) {
        this.oldContent = oldContent;
    }

    public String getNewContent() {
        return newContent;
    }

    public void setNewContent(String newContent) {
        this.newContent = newContent;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Clause getClause() {
        return clause;
    }

    public void setClause(Clause clause) {
        this.clause = clause;
    }

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public User getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(User modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }
}