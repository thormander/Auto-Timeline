package com.example.demo.services;

import com.example.demo.model.Hero;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class Fight {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private SuperRepository superRepository;

    @Transactional
    public Hero fight(Long id1, Long id2) {

        Hero hero1 = superRepository.findById(id1);
        Hero hero2 = superRepository.findById(id2);

        if (hero1 == null || hero2 == null){
            return null;
        } 

        // Check for cohort membership and calculate combined strengths if applicable
        float id1Strength = calculateTotalStrength(hero1);
        float id2Strength = calculateTotalStrength(hero2);

        // If heroes are on the same side, they will form/join a cohort.
        if (hero1.getWill() == hero2.getWill()) {
            mergeHeroes(hero1, hero2);
            return null; 
        }

        if (id1Strength == id2Strength) {
            // Both heroes (or cohorts) die
            removeHeroOrCohort(hero1);
            removeHeroOrCohort(hero2);
            return null;
        } else if (id1Strength > id2Strength) {
            // Hero1 (or the cohort) wins, Hero2 (or the cohort) dies
            removeHeroOrCohort(hero2);
            return hero1;
        } else {
            // opposite of above
            removeHeroOrCohort(hero1);
            return hero2;
        }
    }

    // functions for making a cohort
    public void mergeHeroes(Hero hero1, Hero hero2) {

        // Check if either hero is part of a cohort.
        Long existingCohortId = null;

        if (hero1.getCohort() != null) {

            existingCohortId = hero1.getCohort();

        } else if (hero2.getCohort() != null) {

            existingCohortId = hero2.getCohort();

        }
    
        if (existingCohortId == null) {
            // Neither hero is part of an existing cohort; we can make a new one.
            existingCohortId = generateNewCohortId();
        }

        hero1.setCohort(existingCohortId);
        hero2.setCohort(existingCohortId);
    
        em.merge(hero1);
        em.merge(hero2);
    }

    private Long generateNewCohortId() {
        Long maxCohortId = em.createQuery("SELECT MAX(h.cohort) FROM Hero h", Long.class).getSingleResult();
        
        // if null consider it 0 and add 1
        if (maxCohortId == null) {
            return (long) 1;
        } else {
            return maxCohortId + 1;
        }
    }

    private float calculateTotalStrength(Hero hero) {
        float totalStrength = 0f;
    
        // Check if the hero is part of a cohort
        if (hero.getCohort() != null) {

            Double sum = em.createQuery("SELECT SUM(h.strength) FROM Hero h WHERE h.cohort = :cohort", Double.class).setParameter("cohort", hero.getCohort()).getSingleResult();

            if (sum != null) {
                totalStrength = sum.floatValue();
            } else {
                totalStrength = 0f; //default
            }
        } else {

            // If the hero is not part of a cohort, just use their individual strength
            totalStrength = hero.getStrength();
        }
    
        return totalStrength;
    }
    
    private void removeHeroOrCohort(Hero hero) {
        if (hero.getCohort() != null) {
            // Remove all heroes in the cohort

            em.createQuery("DELETE FROM Hero h WHERE h.cohort = :cohort").setParameter("cohort", hero.getCohort()).executeUpdate();
        } else {

            // Remove only the hero (case when he is not in a cohort)
            em.remove(hero);
        }
    }
    
    
}
