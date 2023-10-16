package com.example.accessingdatajpa;

import org.springframework.data.repository.CrudRepository;

public interface PEntityRepository extends CrudRepository<PEntity, Long> {
}
