package ru.dedov.tonappbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.dedov.tonappbackend.dto.ErrorDto;
import ru.dedov.tonappbackend.dto.SuccessDto;
import ru.dedov.tonappbackend.dto.TokenDeductionRequestDto;
import ru.dedov.tonappbackend.dto.UserDto;
import ru.dedov.tonappbackend.mapper.UserMapper;
import ru.dedov.tonappbackend.model.entity.User;
import ru.dedov.tonappbackend.model.repository.UserRepository;

import java.util.Optional;

/**
 * Сервисный класс для операций с сущностью Пользователь
 *
 * @author Alexander Dedov
 * @since 23.05.2024
 */
@Service
public class UserService {

	private final UserMapper userMapper;
	private final UserRepository userRepository;

	@Autowired
	public UserService(UserMapper userMapper, UserRepository userRepository) {
		this.userMapper = userMapper;
		this.userRepository = userRepository;
	}

	/**
	 * Сохранить пользователя.
	 * Если пользователь с таким username уже существует - не сохраняем
	 */
	public ResponseEntity<String> saveUser(UserDto userDto) {
		User user = userMapper.toEntity(userDto);
		String username = user.getUsername();
		if (userRepository.existsByUsername(username)) {
			return ResponseEntity.badRequest().body("user with username = " + username + " already exist");
		}
		userRepository.save(user);
		return ResponseEntity.ok("user with username = " + username + " saved successfully");
	}

	/**
	 * Найти пользователя по id.
	 * Если пользователя с таким id не существует - 404
	 */
	public ResponseEntity<UserDto> findUserById(Long id) {
		Optional<User> optionalUser = userRepository.findById(id);
		return optionalUser
			.map(user -> ResponseEntity.ok(userMapper.toDto(user)))
			.orElse(ResponseEntity.notFound().build());
	}

	/**
	 * Обновляем пользователя по id
	 */
	public ResponseEntity<String> updateUser(Long id, UserDto userDto) {
		User user = userMapper.toEntity(userDto);
		if (userRepository.existsById(id)) {
			user.setId(id);
			userRepository.save(user);
			return ResponseEntity.ok("user with id = " + id + " updated successfully");
		}
		return ResponseEntity.badRequest().body("user with id = " + id + " not exist");
	}

	public ResponseEntity<?> deductTokenFromUser(TokenDeductionRequestDto deductionRequestDto) {
		Optional<User> optionalUser = userRepository.findById(deductionRequestDto.getUserId());
		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			if (user.getAccountBalance().compareTo(deductionRequestDto.getDeductionAmount()) >= 0) {
				user.setAccountBalance(user.getAccountBalance() - deductionRequestDto.getDeductionAmount());
				userRepository.save(user);
				return ResponseEntity.ok(
					new SuccessDto(
						true,
						"token(s) deducted, new balance = " + user.getAccountBalance())
				);
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
				new ErrorDto("deduction amount = " + deductionRequestDto.getDeductionAmount() +
					" is greater than user account balance = " + user.getAccountBalance())
			);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
			new ErrorDto("user with id = " + deductionRequestDto.getUserId() + " not found")
		);
	}
}
