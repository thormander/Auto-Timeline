package com.example.demo.services;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.demo.model.Hero;
import com.example.demo.model.Will;

import jakarta.persistence.*;

import org.springframework.transaction.annotation.Transactional;


@Repository
public class SuperRepository {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void populate() {
        // Good heroes
        Hero goodHero1 = new Hero("GoodHero1", "Elf", 7.5f, Will.GOOD);
        em.persist(goodHero1);
        Hero goodHero2 = new Hero("GoodHero2", "Human", 6.0f, Will.GOOD);
        em.persist(goodHero2);
        Hero goodHero3 = new Hero("GoodHero3", "Hobbit", 5.0f, Will.GOOD);
        em.persist(goodHero3);
        Hero goodHero4 = new Hero("GoodHero4", "Dwarf", 7.0f, Will.GOOD);
        em.persist(goodHero4);
        Hero goodHero5 = new Hero("GoodHero5", "Wizard", 9.0f, Will.GOOD);
        em.persist(goodHero5);

        // Bad heroes
        Hero badHero1 = new Hero("BadHero1", "Orc", 5.5f, Will.DARK);
        em.persist(badHero1);
        Hero badHero2 = new Hero("BadHero2", "Troll", 6.5f, Will.DARK);
        em.persist(badHero2);
        Hero badHero3 = new Hero("BadHero3", "Goblin", 5.0f, Will.DARK);
        em.persist(badHero3);
        Hero badHero4 = new Hero("BadHero4", "Human", 7.5f, Will.DARK);
        em.persist(badHero4);
        Hero badHero5 = new Hero("BadHero5", "Rat", 100.0f, Will.DARK);
        em.persist(badHero5);
    }

    // reference in contorller
    @Transactional
    public List<Hero> saveAll(List<Hero> heroes) {
        heroes.forEach(em::persist);

        em.flush();

        return heroes;
    }

    // reference in controller
    public List<Hero> findAllOrderedByName() {
        return em.createQuery("SELECT h FROM Hero h ORDER BY h.name", Hero.class).getResultList();
    } 

    // reference in controller
    public Hero findById(Long id) {
        return em.find(Hero.class, id);
    }

    public List<Hero> findByName(String name) {
        return em.createQuery("SELECT h FROM Hero h WHERE h.name LIKE :name", Hero.class).setParameter("name", "%" + name + "%").getResultList();
    }




}