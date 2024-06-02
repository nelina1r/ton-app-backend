package ru.dedov.tonappbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dedov.tonappbackend.api.TonNetworkApiClient;
import ru.dedov.tonappbackend.dto.TokenDeductionRequestDto;
import ru.dedov.tonappbackend.dto.UserDto;
import ru.dedov.tonappbackend.dto.tonapi.Account;
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
public class UserService extends AbstractService {

	private final UserRepository userRepository;
	private final UserMapper userMapper;
	private final TonNetworkApiClient tonNetworkApiClient;

	@Autowired
	public UserService(UserRepository userRepository, UserMapper userMapper, TonNetworkApiClient tonNetworkApiClient) {
		this.userRepository = userRepository;
		this.userMapper = userMapper;
		this.tonNetworkApiClient = tonNetworkApiClient;
	}

	/**
	 * Сохранить пользователя.
	 * Если пользователь с таким username уже существует возвращаем его
	 */
	@Transactional
	public ResponseEntity<?> saveOrGetUser(UserDto userDto) {
		User user = userMapper.toEntity(userDto);
		String accountId = user.getAccountId();
		if (userRepository.existsByAccountId(accountId)) {
			User actualUser = userRepository.findByAccountId(accountId).get();
			return ResponseEntity.ok(actualUser);
		}
		Account account = tonNetworkApiClient.getAccountById(accountId);
		user.setTonBalance(convertFromNanoCoins(account.getBalance().doubleValue()));
		userRepository.save(user);
		return ResponseEntity.ok(
			newSuccessDtoWithMessage("user with accountId = " + accountId + " saved successfully")
		);
	}

	/**
	 * Найти пользователя по id.
	 * Если пользователя с таким id не существует - 404
	 */
	public ResponseEntity<User> findUserByAccountId(String accountId) {
		Optional<User> optionalUser = userRepository.findByAccountId(accountId);
		return optionalUser
			.map(ResponseEntity::ok)
			.orElse(ResponseEntity.notFound().build());
	}

	@Transactional
	public User increaseUserBalanceByAccountId(String accountId, Double amount) {
		Optional<User> optionalUser = userRepository.findByAccountId(accountId);
		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			user.setAccountBalance(user.getAccountBalance() + convertFromNanoCoins(amount));
			userRepository.save(user);
			return user;
		}
		return new User();
	}

	/**
	 * Обновляем пользователя по id
	 */
	@Transactional
	public ResponseEntity<?> updateUser(User user) {
		String accountId = user.getAccountId();
		if (userRepository.existsByAccountId(accountId)) {
			userRepository.save(user);
			return ResponseEntity.ok(
				newSuccessDtoWithMessage("user with id = " + accountId + " updated successfully")
			);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
			newErrorDtoWithMessage("user with id = " + accountId + " not exist")
		);
	}

	/**
	 * Списать у пользователя с id == userId с баланса deductionAmount токенов
	 *
	 * @param deductionRequestDto accountId + deductionAmount
	 */
	@Transactional
	public ResponseEntity<?> deductTokenFromUser(TokenDeductionRequestDto deductionRequestDto) {
		Optional<User> optionalUser = userRepository.findByAccountId(deductionRequestDto.getAccountId());
		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			if (user.getAccountBalance().compareTo(deductionRequestDto.getDeductionAmount()) >= 0) {
				user.setAccountBalance(user.getAccountBalance() - deductionRequestDto.getDeductionAmount());
				userRepository.save(user);
				return ResponseEntity.ok(
					newSuccessDtoWithMessage("token(s) deducted, new balance = " + user.getAccountBalance())
				);
			}
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
				newErrorDtoWithMessage("deduction amount = " + deductionRequestDto.getDeductionAmount() +
					" is greater than user account balance = " + user.getAccountBalance())
			);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
			newErrorDtoWithMessage("user with accountId = " + deductionRequestDto.getAccountId() + " not found")
		);
	}
}
