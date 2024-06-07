package ru.dedov.tonappbackend.service;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dedov.tonappbackend.api.TonNetworkApiClient;
import ru.dedov.tonappbackend.dto.*;
import ru.dedov.tonappbackend.dto.tonapi.*;
import ru.dedov.tonappbackend.exceptions.EventsNotFoundException;
import ru.dedov.tonappbackend.model.entity.User;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

/**
 * Сервис для работы с объектами сети TON
 *
 * @author Alexander Dedov
 * @since 25.05.2024
 */
@Log
@Service
public class TonService extends AbstractService {

	private static final int MAX_RETRY = 12;

	private final TonNetworkApiClient tonNetworkApiClient;
	private final UserService userService;

	@Autowired
	public TonService(TonNetworkApiClient tonNetworkApiClient, UserService userService) {
		this.tonNetworkApiClient = tonNetworkApiClient;
		this.userService = userService;
	}

	/**
	 * Метод возвращает информацию о балансе (количеству токенов) по symbol токена
	 * @param accountId accountId
	 * @param tokenSymbol tokenSymbol
	 */
	public ResponseEntity<?> getTokenBalance(String accountId, String tokenSymbol) {
		BalancesContainer balancesContainer = tonNetworkApiClient.getBalancesById(accountId);
		Optional<Balance> optionalTokenBalance = balancesContainer.getBalances().stream()
			.filter(balance -> balance.getJetton().getSymbol().equals(tokenSymbol))
			.findFirst();
		if (optionalTokenBalance.isPresent()) {
			Balance tokenBalance = optionalTokenBalance.get();
			Double convertedBalance = tokenBalance.getJetton().getDecimals() == 9
				? convertFromNanoCoins(Double.valueOf(tokenBalance.getBalance()))
				: convertFromFemtoCoins(Double.valueOf(tokenBalance.getBalance()));
			return ResponseEntity.ok(
				new TokenBalanceResponseDto(convertedBalance.toString(), tokenBalance.getJetton().getSymbol())
			);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
			newErrorDtoWithMessage("token " + tokenSymbol + " not found")
		);
	}

	@Transactional
	public ResponseEntity<?> processUserBalanceAfterSuccessfulTransaction(UpdateUserBalanceRequestDto updateDto)
		throws InterruptedException, EventsNotFoundException
	{
		Event firstTakenEvent = getEventsByTonApi(updateDto).getEvents().get(0);
		log.info("firstTakenEvent = " + firstTakenEvent);
		for (int i = 0; i < MAX_RETRY; i++) {
			Event expectedNewestEvent = getEventsByTonApi(updateDto).getEvents().get(0);
			log.info(i + " try to get newestEvent, now candidate is " + expectedNewestEvent);
			if (!firstTakenEvent.equals(expectedNewestEvent)) {
				log.info("REAL newest event is " + expectedNewestEvent);
				Optional<Action> optionalAction = expectedNewestEvent.getActions().stream()
					.filter(filteringAction ->
						filteringAction.getType().equals("JettonTransfer") && filteringAction.getStatus().equals("ok"))
					.findFirst();
				if (optionalAction.isPresent()) {
					Action action = optionalAction.get();
					JettonTransfer jettonTransfer = action.getJettonTransfer();
					Double transactionAmount = Double.valueOf(jettonTransfer.getAmount());
					User user = userService.increaseUserBalanceByAccountId(updateDto.getAccountId(), transactionAmount);
					return ResponseEntity.ok(
						newSuccessDtoWithMessage("balance updated, new balance = " + user.getAccountBalance())
					);
				}
			} else {
				Thread.sleep(5000);
			}
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
			newErrorDtoWithMessage("failed to fetch new transactions after " + MAX_RETRY + " attempts")
		);
	}

	public EventsContainer getEventsByTonApi(UpdateUserBalanceRequestDto updateDto) {
		LocalDateTime dateTimeNow = LocalDateTime.now();
		EventsContainer eventsContainer = tonNetworkApiClient.getEventsByParameters(
			updateDto.getAccountId(),
			updateDto.getJettonId(),
			5L,
			updateDto.getEndDate() == null
				? dateTimeNow.minusMinutes(120).atZone(ZoneId.systemDefault()).toEpochSecond()
				: updateDto.getStartDate(),
			updateDto.getStartDate() == null
				? dateTimeNow.atZone(ZoneId.systemDefault()).toEpochSecond()
				: updateDto.getEndDate()
		);
		if (eventsContainer.getEvents().isEmpty()) {
			log.info("events not found");
			throw new EventsNotFoundException("events not found");
		}
		return eventsContainer;
	}
}
