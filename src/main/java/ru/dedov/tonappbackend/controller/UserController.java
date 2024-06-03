package ru.dedov.tonappbackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.dedov.tonappbackend.dto.TokenDeductionRequestDto;
import ru.dedov.tonappbackend.dto.UserDto;
import ru.dedov.tonappbackend.model.entity.User;
import ru.dedov.tonappbackend.service.UserService;

/**
 * Контроллер для обработки операций связанных с пользователями
 *
 * @author Alexander Dedov
 * @since 23.05.2024
 */
@Log
@RestController
@RequestMapping("/api/v1")
public class UserController {

	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@Operation(summary = "списать токены")
	@PostMapping("/deduct_token")
	public ResponseEntity<?> deductToken(@Valid @RequestBody TokenDeductionRequestDto tokenDeductionRequestDto) {
		log.info("accepted POST request method deduct token from user: " + tokenDeductionRequestDto.toString());
		return userService.deductTokenFromUser(tokenDeductionRequestDto);
	}

	@Operation(summary = "создать или вернуть пользователя")
	@PostMapping("/user")
	public ResponseEntity<?> createOrGetUser(@Valid @RequestBody UserDto userDto) {
		log.info("accepted POST request method create user: " + userDto.toString());
		return userService.saveOrGetUser(userDto);
	}

	// @Operation(summary = "обновить пользователя по accountId")
	// @PutMapping("/user")
	public ResponseEntity<?> updateUser(@RequestBody User user) {
		log.info("accepted PUT request method update user: " + user.toString());
		return userService.updateUser(user);
	}

	@Operation(summary = "получить пользователя по accountId")
	@GetMapping("/user/{accountId}")
	public ResponseEntity<User> findUser(@PathVariable String accountId)
	{
		log.info("accepted GET request method find user: " + accountId);
		return userService.findUserByAccountId(accountId);
	}

}
