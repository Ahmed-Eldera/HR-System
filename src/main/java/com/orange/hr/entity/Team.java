package com.orange.hr.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class Team {
	@Id
	@GeneratedValue
	private Long Team_id;

	private String name;
}
