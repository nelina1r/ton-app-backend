package ru.dedov.tonappbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.dedov.tonappbackend.dto.TokenDeductionRequestDto;
import ru.dedov.tonappbackend.dto.UserDto;
import ru.dedov.tonappbackend.service.UserService;

/**
 * Контроллер для обработки операций связанных с пользователями
 *
 * @author Alexander Dedov
 * @since 23.05.2024
 */
@RestController
@RequestMapping("/api/v1")
public class UserController {

	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/deduct_token")
	public ResponseEntity<?> deductToken(@RequestBody TokenDeductionRequestDto tokenDeductionRequestDto) {
		return userService.deductTokenFromUser(tokenDeductionRequestDto);
	}

	@PostMapping("/user")
	public ResponseEntity<String> createUser(@RequestBody UserDto userDto) {
		return userService.saveUser(userDto);
	}

	@PutMapping("/user/{id}")
	public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
		return userService.updateUser(id, userDto);
	}

	@GetMapping("/user/{id}")
	public ResponseEntity<UserDto> findUser(@PathVariable Long id) {
		return userService.findUserById(id);
	}

}
