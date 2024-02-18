package com.example.demo.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
public class Hero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private String race;

    @Column(nullable = false)
    private float strength;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Will will;

    // cohort column
    @Column
    private Long cohort;

    // constructiors
    public Hero() {
    }

    public Hero(String name, String race, float strength, Will will) {
        this.name = name;
        this.race = race;
        this.strength = strength;
        this.will = will;
    }

    // cohort stuff

    public Long getCohort() {
        return cohort;
    }

    public void setCohort(Long cohort) {
        this.cohort = cohort;
    }


    // getters / setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public float getStrength() {
        return strength;
    }

    public void setStrength(float strength) {
        this.strength = strength;
    }

    public Will getWill() {
        return will;
    }

    public void setWill(Will will) {
        this.will = will;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else {
            if (!(o instanceof Hero)) {
                return false;
            } else {
                Hero hero = (Hero) o;
                return Objects.equals(getId(), hero.getId());
            }
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
