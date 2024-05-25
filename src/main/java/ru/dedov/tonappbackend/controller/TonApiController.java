package ru.dedov.tonappbackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dedov.tonappbackend.dto.TokenBalanceRequestDto;
import ru.dedov.tonappbackend.service.TonService;

/**
 * Контроллер для обработки операций с TON-сетью
 *
 * @author Alexander Dedov
 * @since 23.05.2024
 */
@RestController
@RequestMapping("api/v1")
public class TonApiController {

	private final TonService tonService;

	@Autowired
	public TonApiController(TonService tonService) {
		this.tonService = tonService;
	}

	@GetMapping("/get_token_balance")
	public ResponseEntity<?> getTokenBalance(@RequestBody TokenBalanceRequestDto balanceRequestDto) {
		return tonService.getTokenBalance(balanceRequestDto);
	}
}
