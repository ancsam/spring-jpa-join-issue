package com.example.accessingdatajpa;

import org.springframework.data.repository.CrudRepository;

public interface GPEntityRepository extends CrudRepository<GPEntity, Long> {
}
