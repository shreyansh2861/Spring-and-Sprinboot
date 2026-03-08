package com.example.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

import com.example.demo.model.Alien;

//public interface AlienRepo extends CrudRepository<Alien, Integer>{
public interface AlienRepo extends JpaRepository<Alien, Integer>{ //has crudRepository in it
	List<Alien> findByTech(String tech); // Works fine until starts with findBy or getBy
	
	List<Alien> findByAidGreaterThan(int aid); //same as above
	
	@Query("from Alien where tech=?1order by aname") // uses JPQL language
	List<Alien> findByTechSorted(String tech); // Custom Query
}
