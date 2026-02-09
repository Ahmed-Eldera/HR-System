package com.orange.hr.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.springframework.data.domain.Persistable;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "payments")
@Entity
@SequenceGenerator(name = "NAME_SEQUENCE", sequenceName = "SEQ_ID", initialValue = 1, allocationSize = 1)
public class Payment implements Persistable<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Double amount;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "salaryId")
    private Salary salary;

    @CreationTimestamp(source = SourceType.DB)
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;


    @Override
    public boolean isNew() {
        return id == null;
    }
}
