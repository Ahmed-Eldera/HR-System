package com.orange.hr.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="departments")
public class Department {
    @Id
    @GeneratedValue
    @Column(name="department_id")
    private int departmentId;
    private String name;

}
