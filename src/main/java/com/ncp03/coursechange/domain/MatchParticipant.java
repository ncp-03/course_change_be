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
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

/** 세션 참여자. 주는·받는 분반은 매칭 당시 스냅샷이다. */
@Entity
@Table(name = "match_participants")
public class MatchParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "participant_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "session_id", nullable = false)
    private MatchSession session;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "request_id", nullable = false)
    private ExchangeRequest request;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "offered_enrollment_id", nullable = false)
    private Enrollment offeredEnrollment;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "give_offering_id", nullable = false)
    private CourseOffering giveOffering;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "receive_offering_id", nullable = false)
    private CourseOffering receiveOffering;

    @Enumerated(EnumType.STRING)
    @Column(name = "response_status", nullable = false, length = 20)
    private ResponseStatus responseStatus;

    @Column(name = "responded_at")
    private LocalDateTime respondedAt;

    @JdbcTypeCode(SqlTypes.TINYINT)
    @Column(name = "wake_sent_count", nullable = false)
    private Integer wakeSentCount;

    protected MatchParticipant() {
    }

    public Long getId() { return id; }
    public MatchSession getSession() { return session; }
    public ExchangeRequest getRequest() { return request; }
    public Student getStudent() { return student; }
    public Enrollment getOfferedEnrollment() { return offeredEnrollment; }
    public CourseOffering getGiveOffering() { return giveOffering; }
    public CourseOffering getReceiveOffering() { return receiveOffering; }
    public ResponseStatus getResponseStatus() { return responseStatus; }
    public LocalDateTime getRespondedAt() { return respondedAt; }
    public Integer getWakeSentCount() { return wakeSentCount; }
}
