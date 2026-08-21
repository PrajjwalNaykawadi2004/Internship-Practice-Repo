package com.legalcontract.legal_contract_management_system.entity;

import jakarta.persistence.*;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;

@Entity
@Table(name = "contracts")
public class Contract
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContractStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public Contract()
    {

    }

    public Contract(long id, String title, String  description, ContractStatus status, User createdBy,
                    LocalDateTime createdAt, LocalDateTime updatedAt)
    {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @PrePersist
    protected void onCreate()
    {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if(status == null)
        {
            status = ContractStatus.DRAFT;
        }
    }

    @PreUpdate
    protected void onUpdate()
    {
        updatedAt = LocalDateTime.now();
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public ContractStatus getStatus()
    {
        return status;
    }

    public void setStatus(ContractStatus status)
    {
        this.status = status;
    }

    public User getCreatedBy()
    {
        return createdBy;
    }

    public void setCreatedBy(User createdBy)
    {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedAt()
    {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt()
    {
        return updatedAt;
    }
}


