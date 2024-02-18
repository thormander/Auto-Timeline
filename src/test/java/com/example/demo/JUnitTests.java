package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.model.Hero;
import com.example.demo.model.Will;
import com.example.demo.services.Fight;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@SpringBootTest
class JUnitTests {

	@PersistenceContext
    private EntityManager em;

    @BeforeEach
    public void setUp() {
        populate();
    }

	// Hero creation with a unique name
	@Test
	@Transactional
    void heroCreationTest() {
        Hero newHero = new Hero("Bob", "Human", 1000000.5f, Will.GOOD);
        em.persist(newHero);
        
		em.flush();

        Hero foundHero = em.find(Hero.class, newHero.getId());

        assertTrue(foundHero != null && "Bob".equals(foundHero.getName()));
    }

	// Listing heroes successfully.
    @Test
	@Transactional
    void listHeroesTest() {
        List<Hero> heroes = em.createQuery("SELECT h FROM Hero h", Hero.class).getResultList();
        assertTrue(heroes != null && !heroes.isEmpty());
    }

	// Fights of the same side, another side, tragedy fights when both die.
    @Autowired
    private Fight fight;

	@Test
	@Transactional
	void fightTest() {
		
		// Test 1: No fight occurs as they are on same side
		Long sameWillHero1Id = (long) 1; 
		Long sameWillHero2Id = (long) 2;
		Hero noFightResult = fight.fight(sameWillHero1Id, sameWillHero2Id);
		assertTrue(noFightResult == null, "No fight should happen between 2 heroes on same side");
	
		// Test 2: both heros die
		Long equalStrengthHero1Id = (long) 3; 
		Long equalStrengthHero2Id = (long) 8; 
		Hero bothDieResult = fight.fight(equalStrengthHero1Id, equalStrengthHero2Id);
		assertTrue(bothDieResult == null, "Both heroes should die if equal strength");
		
		em.flush();
		em.clear();
	
		// Test 3: hero fight on different sides
		Long strongerHeroId = (long) 10; 
		Long weakerHeroId = (long) 1;
		Hero hero1WinsResult = fight.fight(strongerHeroId, weakerHeroId);
		assertTrue(hero1WinsResult != null && hero1WinsResult.getId().equals(strongerHeroId), "The hero with a greater strength will win (the rat in this case)");
	
	}
	

	// necessary as we work with h2
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
}
