package com.example.demo.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.security.AuthenticationResponse;
import com.example.demo.Utils.JwUtil;

@RestController
public class control {

	@Autowired
	UserRepo userRepo;

	@Autowired
	BCryptPasswordEncoder passwordEncoder;

	@Autowired
	AuthenticationManager authManager;

	@Autowired
	JwUtil jwtUtil;
	
	
	private User user;

	@GetMapping("/path")
	public String get() {
		return "Hello JPA";
	}

	@GetMapping("/get")
	public List<User> getAll() {
		return (List<User>) userRepo.findAll();
	}

	@GetMapping("/")
	public  List<User> getList() {
		
		return (List<User>)userRepo.findAll();
	}

	@RequestMapping("/add")
	public String add(@RequestBody User user) {
		String p=passwordEncoder.encode(user.getPwd());
		System.out.println(p);
		User newUser = new User();
		newUser.setId(user.getId());
		newUser.setUsername(user.getUsername());
		newUser.setPwd(p);
		newUser.setRoles(user.getRoles());
		userRepo.save(user);
		return "Added Sucessfully";
	}

	@PostMapping("/authenticate")
	public AuthenticationResponse generateToken(@RequestBody AuthRequest authRequest) throws Exception {
		try {
			authManager.authenticate(
					new UsernamePasswordAuthenticationToken(authRequest.getUsername(),authRequest.getPwd()));
		} catch (Exception ex) {
			throw new Exception("inavalid username/password");
		}
		final String jwt = jwtUtil.generateToken(authRequest.getUsername());
		return new AuthenticationResponse(jwt);
	}
}
