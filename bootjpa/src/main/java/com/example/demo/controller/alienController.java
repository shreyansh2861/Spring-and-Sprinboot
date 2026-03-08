package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.dao.AlienRepo;
import com.example.demo.model.Alien;


//@RestController // all working for restApi no need to add @ResponseBody everytime 
@Controller
public class alienController {
	
	
	@Autowired
	AlienRepo repo;
	
	@GetMapping("/")
	public String Home() {
		return "home.jsp";
	}
	
	@GetMapping("/addAlien")
	public String addAlien(Alien alien) {
		repo.save(alien);
		return "home.jsp";
	}
	
	@GetMapping("/getAlien")
	public ModelAndView getAlien(@RequestParam int aid) {
		ModelAndView mv = new ModelAndView("showAlien.jsp");
		Alien alien = repo.findById(aid).orElse(new Alien());
		mv.addObject(alien);
		return mv;
	}
	
	
//	@RequestMapping(path="/aliens", prroduces="application/xml") // to stick to only one type of returns
	@GetMapping("/aliens")
	@ResponseBody
	public List<Alien> getAliens() {
		return repo.findAll();
	}
	
	@GetMapping("/alien/{aid}")    // all inside {} called wildcard
	@ResponseBody
	public Optional<Alien> getAlien1(@PathVariable int aid) {
		return repo.findById(aid);
	}
	
	@PostMapping("/alienApiAdd")
	public String alienAddApi(Alien alien) {  // use (@ResponseBody Alien alien) for raw data from postman
		repo.save(alien);
		return "String";
	}
	
	
	@DeleteMapping("/alien/{aid}")
	public String deleteAlien(int aid) {
		repo.deleteById(aid);
		return "data  deleted";
	}
	
	
	@PutMapping("/alien")
	public Alien updateAlien(Alien alien) {
		repo.save(alien);
		return alien;
	}
	
}
