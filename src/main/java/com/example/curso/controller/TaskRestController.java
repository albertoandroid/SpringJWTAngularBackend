package com.example.curso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.curso.entity.Task;
import com.example.curso.model.JwtUser;
import com.example.curso.security.JwtValidator;
import com.example.curso.service.ITaskService;

@RestController
@RequestMapping("/api")
public class TaskRestController {
	
	@Autowired
	private ITaskService taskService;
	
	@Autowired
	private JwtValidator validator;
	
	@PostMapping("/task")
	public ResponseEntity<?> createTask(@RequestBody Task task, @RequestHeader (name="Authorization") String bearerToken){
		System.out.println(bearerToken + "ddddd");

		String token = bearerToken.substring(7);
		JwtUser jwtUser = validator.validate(token);
		task.setUserId(Long.valueOf(jwtUser.getId()));
		task.setStatus("to-do");
		taskService.saveTask(task);
		return new ResponseEntity<Void>(HttpStatus.CREATED);
	}

}
