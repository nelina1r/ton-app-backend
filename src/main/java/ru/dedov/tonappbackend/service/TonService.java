package ru.dedov.tonappbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.dedov.tonappbackend.api.TonNetworkApiClient;
import ru.dedov.tonappbackend.dto.ErrorDto;
import ru.dedov.tonappbackend.dto.TokenBalanceRequestDto;
import ru.dedov.tonappbackend.dto.TokenBalanceResponseDto;
import ru.dedov.tonappbackend.dto.tonapi.Balance;
import ru.dedov.tonappbackend.dto.tonapi.BalancesContainer;

import java.util.Optional;

/**
 * Сервис для работы с объектами сети TON
 *
 * @author Alexander Dedov
 * @since 25.05.2024
 */
@Service
public class TonService {

	private final TonNetworkApiClient tonNetworkApiClient;

	@Autowired
	public TonService(TonNetworkApiClient tonNetworkApiClient) {
		this.tonNetworkApiClient = tonNetworkApiClient;
	}

	public ResponseEntity<?> getTokenBalance(TokenBalanceRequestDto balanceRequestDto) {
		BalancesContainer balancesContainer = tonNetworkApiClient.getBalancesById(balanceRequestDto.getAccountId());
		Optional<Balance> optionalTokenBalance = balancesContainer.getBalances().stream()
			.filter(balance -> balance.getJetton().getSymbol().equals(balanceRequestDto.getTokenSymbol()))
			.findFirst();
		if (optionalTokenBalance.isPresent()) {
			Balance tokenBalance = optionalTokenBalance.get();
			return ResponseEntity.ok(
				new TokenBalanceResponseDto(tokenBalance.getBalance(), tokenBalance.getJetton().getSymbol())
			);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
			new ErrorDto("token " + balanceRequestDto.getTokenSymbol() + " not found")
		);
	}
}
