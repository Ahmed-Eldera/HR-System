package com.orange.hr.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "raises")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Raise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "raise_id")
    private Integer raiseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "raise_percentage")
    private Double percentage;

    @Column(name = "salary_after_raise")
    private Double salaryAfterRaise;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_raise")
    private Raise lastRaise;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
