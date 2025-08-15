package com.ing.hub.loan.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.OffsetDateTime;

@MappedSuperclass
@EntityListeners({AuditingEntityListener.class})
public class AbstractAuditingEntity implements Serializable {
    @Column(name = "inserted_at", columnDefinition = "TIMESTAMP WITH TIME ZONE", nullable = false, updatable = false)
    @CreatedDate
    private OffsetDateTime insertedAt = OffsetDateTime.now();

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @LastModifiedDate
    private OffsetDateTime updatedAt;
}
