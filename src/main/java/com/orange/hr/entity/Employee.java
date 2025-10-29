package com.orange.hr.entity;

import com.orange.hr.enums.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="employees")
public class Employee {
    @Id
    @Column(name = "employee_id")
    private int employeeID;

    private String name;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    private Gender gender;

    @Column(name = "graduation_date")
    private LocalDate graduationDate;

    private float salary;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Employee manager;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

}
