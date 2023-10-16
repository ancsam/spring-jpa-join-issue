package com.example.accessingdatajpa;

import org.springframework.data.repository.CrudRepository;

public interface GEntityRepository extends CrudRepository<GEntity, Long> {

	GEntity findById(long id);
}
