package com.example.accessingdatajpa;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class GPEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private GEntity gEntity;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private PEntity pEntity;

    public GPEntity() {
    }

    public GPEntity(GEntity gEntity, PEntity pEntity) {
        this.gEntity = gEntity;
        this.pEntity = pEntity;
    }

    public Long getId() {
        return id;
    }

    public GEntity getgEntity() {
        return gEntity;
    }

    public PEntity getpEntity() {
        return pEntity;
    }

}
