package com.ncp03.coursechange.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    /** 학생의 현재 수강 내역 (분반·과목 함께 로딩). */
    @Query("""
            select e from Enrollment e
            join fetch e.offering o
            join fetch o.course c
            where e.student.id = :studentId
            order by c.courseCode asc, o.sectionNo asc
            """)
    List<Enrollment> findAllByStudentIdWithOffering(@Param("studentId") Long studentId);

    /** 분반별 현재 수강 인원. 결과 행: [offeringId(Long), count(Long)] */
    @Query("""
            select e.offering.id, count(e) from Enrollment e
            where e.offering.id in :offeringIds
            group by e.offering.id
            """)
    List<Object[]> countByOfferingIds(@Param("offeringIds") Collection<Long> offeringIds);

    @Query("""
            select coalesce(sum(o.credits), 0) from Enrollment e join e.offering o
            where e.student.id = :studentId
            """)
    long sumCreditsByStudentId(@Param("studentId") Long studentId);
}
