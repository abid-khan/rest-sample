package com.appdirect.entity;

import com.appdirect.constant.StatusType;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;

/**
 *
 */
@MappedSuperclass public abstract class AbstractAuditablEntity extends AbstractPersistable<Long> {

    @Version
    @Column(name = "version") protected Long version;

    @Enumerated(EnumType.STRING) protected StatusType status;

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public StatusType getStatus() {
        return status;
    }

    public void setStatus(StatusType status) {
        this.status = status;
    }
}
