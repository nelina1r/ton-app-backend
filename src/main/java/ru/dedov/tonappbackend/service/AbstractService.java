package ru.dedov.tonappbackend.service;

import ru.dedov.tonappbackend.dto.ErrorDto;
import ru.dedov.tonappbackend.dto.SuccessDto;

/**
 * TODO Class Description
 *
 * @author Alexander Dedov
 * @since 01.06.2024
 */
public abstract class AbstractService {

	/**
	 * @param message передаваемое сообщение
	 * @return ErrorDto
	 */
	protected ErrorDto newErrorDtoWithMessage(String message) {
		return ErrorDto.builder()
			.errorMessage(message)
			.build();
	}

	/**
	 * @param message передаваемое сообщение
	 * @return новый объект SuccessDto с сообщением
	 */
	protected SuccessDto newSuccessDtoWithMessage(String message) {
		return SuccessDto.builder()
			.success(true)
			.message(message)
			.build();
	}

	protected Double convertFromNanoCoins(Double amount) {
		return amount / 1e9;
	}

	protected Double convertFromFemtoCoins(Double amount) {
		return amount / 1e15;
	}
}
