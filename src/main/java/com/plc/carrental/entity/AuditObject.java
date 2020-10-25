package com.plc.carrental.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

public abstract class AuditObject implements Serializable {
    private Long id;
    private String createdBy;
    private LocalDateTime createdTime;
    private String modifiedBy;
    private LocalDateTime modifiedTime;
    private Long version;

    public Long getId() {
        return id;
    }

    public AuditObject() {
    }

    public AuditObject(Long id) {
        this.id = id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public LocalDateTime getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(LocalDateTime modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
