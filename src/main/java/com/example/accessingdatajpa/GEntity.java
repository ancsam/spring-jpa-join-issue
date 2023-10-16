package com.example.accessingdatajpa;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
public class GEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	private String name;

	@OneToMany(mappedBy = "gEntity", orphanRemoval = true, cascade = CascadeType.ALL)
	private Set<GPEntity> gpEntitySet = new LinkedHashSet<>();

	protected GEntity() {}

	public GEntity(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Set<GPEntity> getGpEntitySet() {
		return gpEntitySet;
	}
}
