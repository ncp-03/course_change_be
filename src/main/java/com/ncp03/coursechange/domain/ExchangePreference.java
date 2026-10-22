package com.ncp03.coursechange.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/** 요청별 희망 분반. 여러 행은 "이 중 하나"를 뜻한다. rank 1이 가장 원하는 분반. */
@Entity
@Table(name = "exchange_preferences")
public class ExchangePreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "preference_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "request_id", nullable = false)
    private ExchangeRequest request;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "target_offering_id", nullable = false)
    private CourseOffering targetOffering;

    @JdbcTypeCode(SqlTypes.TINYINT)
    @Column(name = "preference_rank", nullable = false)
    private Integer preferenceRank;

    protected ExchangePreference() {
    }

    public Long getId() { return id; }
    public ExchangeRequest getRequest() { return request; }
    public CourseOffering getTargetOffering() { return targetOffering; }
    public Integer getPreferenceRank() { return preferenceRank; }
}
