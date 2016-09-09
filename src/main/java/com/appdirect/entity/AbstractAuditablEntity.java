package com.appdirect.entity;

import com.appdirect.constant.StatusType;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;

/**
 *
 */
@MappedSuperclass public abstract class AbstractAuditablEntity extends AbstractPersistable<Long> {

    @Version
    @Column(name = "version") protected Long version;

    @Enumerated(EnumType.STRING) protected StatusType status;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    protected DateTime createdDate;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    protected DateTime lastModifiedDate;

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

    public DateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(DateTime createdDate) {
        this.createdDate = createdDate;
    }

    public DateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(DateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
