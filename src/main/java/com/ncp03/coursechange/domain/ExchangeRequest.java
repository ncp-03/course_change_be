package com.ncp03.coursechange.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

/**
 * 교환 요청: "이 수강 내역을 내놓고 희망 분반 중 하나를 받고 싶다".
 * DB의 active_enrollment_id 생성 컬럼 + UNIQUE가 같은 수강 내역의 진행 중 요청을 1개로 제한한다
 * (생성 컬럼이라 엔티티에는 매핑하지 않는다).
 */
@Entity
@Table(name = "exchange_requests")
public class ExchangeRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "offered_enrollment_id", nullable = false)
    private Enrollment offeredEnrollment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RequestStatus status;

    @Column(name = "queued_at", nullable = false)
    private LocalDateTime queuedAt;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    protected ExchangeRequest() {
    }

    public Long getId() { return id; }
    public Student getStudent() { return student; }
    public Enrollment getOfferedEnrollment() { return offeredEnrollment; }
    public RequestStatus getStatus() { return status; }
    public LocalDateTime getQueuedAt() { return queuedAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
