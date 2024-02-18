package com.example.demo.controller;

import com.example.demo.model.Hero;
import com.example.demo.services.Fight;
import com.example.demo.services.SuperRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;


//Ignore this as it is Spring and not Java EE (Jax-RS) controller
@RestController
@RequestMapping("/heroes")
public class MyController {

    private SuperRepository superRepository;
    private Fight fight;

    @Autowired
    public MyController(SuperRepository superRepository, Fight fight){
        this.superRepository = superRepository;
        this.fight = fight;
    }

    // POST localhost:8080/heroes/populate   (just populates H2)
    @PostMapping("/populate")
    public ResponseEntity<Void> populate() {
        superRepository.populate();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // POST localhost:8080/heroes  (create a hero)
    @PostMapping
    public ResponseEntity<List<Hero>> createHeroes(@RequestBody List<Hero> heroes){
        List<Hero> savedHeroes = superRepository.saveAll(heroes);
        return new ResponseEntity<>(savedHeroes, HttpStatus.CREATED);
    }

    // GET localhost:8080/heroes
    @GetMapping
    public ResponseEntity<List<Hero>> getAllHeroes() {
        List<Hero> heroes = superRepository.findAllOrderedByName();
        return new ResponseEntity<>(heroes, HttpStatus.OK);
    }

    // GET localhost:8080/heroes/{ID}
    @GetMapping("/{id}")
    public ResponseEntity<Hero> getHeroById(@PathVariable Long id) {
        Hero hero = superRepository.findById(id);
        return hero != null ? new ResponseEntity<>(hero, HttpStatus.OK): new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // GET localhost:8080/heroes?name={NAME}
    @GetMapping(params = "name")
    public ResponseEntity<List<Hero>> getHeroesByName(@RequestParam String name) {
        List<Hero> heroes = superRepository.findByName(name);
        return new ResponseEntity<>(heroes, HttpStatus.OK);
    }
    
    // POST localhost:8080/heroes/fight?id1=1&id2=4
    @PostMapping("/fight")
    public ResponseEntity<Hero> fight(@RequestParam Long id1, @RequestParam Long id2) {
        Hero winner = fight.fight(id1, id2);

        if (winner != null) {
            return ResponseEntity.ok(winner); 
        } else {
            return ResponseEntity.noContent().build();
        }
    }

}
