package com.orange.hr.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Team {
	@Id
	@GeneratedValue
	private Long Team_id;

	private String name;
}
