package com.ncp03.coursechange.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

/** 매칭 세션: 두 요청이 맞물려 교환 확정을 기다리는 상태. */
@Entity
@Table(name = "match_sessions")
public class MatchSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SessionStatus status;

    @Column(name = "opened_at", nullable = false)
    private LocalDateTime openedAt;

    @Column(name = "first_accepted_at")
    private LocalDateTime firstAcceptedAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "finished_at")
    private LocalDateTime finishedAt;

    @Column(name = "rematch_allowed_at")
    private LocalDateTime rematchAllowedAt;

    protected MatchSession() {
    }

    public Long getId() { return id; }
    public SessionStatus getStatus() { return status; }
    public LocalDateTime getOpenedAt() { return openedAt; }
    public LocalDateTime getFirstAcceptedAt() { return firstAcceptedAt; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public LocalDateTime getFinishedAt() { return finishedAt; }
    public LocalDateTime getRematchAllowedAt() { return rematchAllowedAt; }
}
