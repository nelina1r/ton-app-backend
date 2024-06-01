package ru.dedov.tonappbackend.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.dedov.tonappbackend.dto.TokenBalanceRequestDto;
import ru.dedov.tonappbackend.dto.UpdateUserBalanceRequestDto;
import ru.dedov.tonappbackend.service.TonService;

/**
 * Контроллер для обработки операций с TON-сетью
 *
 * @author Alexander Dedov
 * @since 23.05.2024
 */
@Log
@RestController
@RequestMapping("api/v1")
public class TonApiController {

	private final TonService tonService;

	@Autowired
	public TonApiController(TonService tonService) {
		this.tonService = tonService;
	}

	@Operation(summary = "запрос баланса по конкретному токену и id аккаунта")
	@GetMapping("/get_token_balance")
	public ResponseEntity<?> getTokenBalance(@Valid @RequestBody TokenBalanceRequestDto balanceRequestDto) {
		log.info("accepted GET request method get token balance: " + balanceRequestDto.toString());
		return tonService.getTokenBalance(balanceRequestDto);
	}

	@Operation(summary = "обновить баланс аккаунта после транзакции (чекается тон апи)")
	@PostMapping("/update_balance_after_transaction")
	public ResponseEntity<?> updateBalanceAfterTransaction(@Valid @RequestBody UpdateUserBalanceRequestDto updateDto)
		throws InterruptedException
	{
		log.info("accepted POST request method update balance after transaction: " + updateDto.toString());
		return tonService.processUserBalanceAfterSuccessfulTransaction(updateDto);
	}
}
