package com.orange.hr.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
@Setter
@Builder
@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Role name;

    @ManyToMany(mappedBy = "roles")
    private Collection<User> users;
}
