package com.orange.hr.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.GenerationTime;

import java.time.LocalDateTime;

@Entity
@Table(name = "salary_adjustments")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
public class Adjustment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "adjustment_id")
    private Integer adjustmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    private Double amount;

    @org.hibernate.annotations.Generated(GenerationTime.INSERT)
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;


}
