package com.antonmesias.app.controller;

import java.sql.Savepoint;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.antonmesias.app.entity.User;
import com.antonmesias.app.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	//Create a new user
	@PostMapping
	public ResponseEntity<?> create (@RequestBody User user){
		return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(user));
	}
	
	//Read an user
	@GetMapping("/{id}")
	/*
	 * Dado que la variable de id de @PathVariable (userId) no se llama igual que la variable de @GetMapping("/{id}")
	 * lo definimos de esta forma "(@PathVariable(value = "id") Long userId)" para que sepa que "userId" es "id"
	 */
	public ResponseEntity<?> read (@PathVariable(value = "id") Long userId){
		Optional<User> oUser = userService.findById(userId);
		
		if(!oUser.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok(oUser);
	}
	
	//Update an user
	@PutMapping("/{id}")
	public ResponseEntity<?> update (@RequestBody User userDetails, @PathVariable(value = "id") Long userId){
		Optional<User> user = userService.findById(userId);
		
		if(!user.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		
		user.get().setName(userDetails.getName());
		user.get().setSurname(userDetails.getSurname());
		user.get().setEmail(userDetails.getEmail());
		user.get().setEnabled(userDetails.getEnabled());
		
		return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(user.get()));
	}
	
	//Delete an user
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletEntity (@PathVariable(value = "id") Long userId){
		if(!userService.findById(userId).isPresent()) {
			return ResponseEntity.notFound().build();
		}
		
		userService.deleteById(userId);
		return ResponseEntity.ok().build();
	}
	
	//read all users
	@GetMapping
	public List<User> readAll(){
		
		List<User> users = StreamSupport.stream(userService.findAll().spliterator(), false)
				.collect(Collectors.toList());
		
		return users;
	}
	
}
