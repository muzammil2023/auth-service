package com.boom.auth_service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<String> handleUserNotFound() {
		return ResponseEntity.status(404).body("User not found");
	}

	@ExceptionHandler(InvalidPasswordException.class)
	public ResponseEntity<String> handleInvalidPassword() {
		return ResponseEntity.status(401).body("Invalid password");
	}
	
	@ExceptionHandler(UserAlreadyExistsException.class)
	public ResponseEntity<String> handleUserAlreadyExists() {
		return ResponseEntity.status(401).body("User already exists");
	}

}
