package com.example.curso.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
		String token = bearerToken.substring(7);
		JwtUser jwtUser = validator.validate(token);
		task.setUserId(Long.valueOf(jwtUser.getId()));
		task.setStatus("to-do");
		taskService.saveTask(task);
		return new ResponseEntity<Void>(HttpStatus.CREATED);
	}
	
	@PutMapping("/task")
	public ResponseEntity<?> updateTask(@RequestBody Task task, @RequestHeader (name="Authorization") String bearerToken){
		Task taskUpdate = taskService.findByIdSQL(task.getId());
		taskUpdate.setStatus(task.getStatus());
		taskService.saveTask(taskUpdate);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	@GetMapping("/task/list")
	public ResponseEntity<?> getTask(@RequestHeader (name="Authorization") String bearerToken){
		String token = bearerToken.substring(7);
		JwtUser jwtUser = validator.validate(token);
		List<Task> listTask = taskService.getTasksUser(jwtUser.getId());
		if(listTask != null) {
			if(listTask.size()!=0) {
				return new ResponseEntity<>(listTask, HttpStatus.OK);
			}else{
				return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
			}
		}else {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
	}
	
	@DeleteMapping("/task/{id}")
	public ResponseEntity<Void> deleteTask(@PathVariable(value="id")Long id){
		taskService.deleteTask(id);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	
	
	

}
