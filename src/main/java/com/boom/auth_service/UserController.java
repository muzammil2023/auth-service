package com.boom.auth_service;

import java.util.Optional;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

record AuthResponse(String token) {
}

record ApiResponse<T>(String message, T data) {
}

record UserLoginDTO(

		@NotBlank(message = "Email is required") @Email(message = "Invalid email format") String email,

		@NotBlank(message = "Password is required") @Size(min = 6, message = "Password must be at least 6 characters") String password

) {
}

record UserRegisterDTO(

		@NotBlank(message = "Name is required") @Size(min = 3, max = 50, message = "Name must be 3 to 50 Characters") String name,

		@NotBlank(message = "Email is Required") @Email String email,

		@NotBlank(message = "Password is required") @Size(min = 6, message = "Password must be at least 6 characters") String password

) {
}

@Service
class UserService {

	@Autowired
	UserRepository userRepo;

	@Autowired
	JwtUtil jwtUtil;

	public String login(String email, String password) {

		User user = userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("User Not Found"));

		if (user == null) {
			throw new UserNotFoundException();
		}

		if (user.getPassword().equals(password)) {
			throw new InvalidPasswordException();

		}

		return jwtUtil.generateToken(email);
	}

	public String register(String name, String email, String password) {
		if (userRepo.findByEmail(email).isPresent()) {
			throw new UserAlreadyExistsException();
		}

		User user = new User();
		user.setName(name);
		user.setEmail(email);
		user.setPassword(password);

		userRepo.save(user);
		return jwtUtil.generateToken(email);
	}

}

@RestController
public class UserController {

	@Autowired
	private UserService service;

	@PostMapping("/login")
	public AuthResponse login(@Valid @RequestBody UserLoginDTO request) {

		String token = service.login(request.email(), request.password());

		return new AuthResponse(token);

	}

	@PostMapping("/register")
	public ApiResponse<AuthResponse> register(@Valid @RequestBody UserRegisterDTO request) {

		String token = service.register(request.name(), request.email(), request.password());

		return new ApiResponse<>("Done!!", new AuthResponse(token));
	}

}
