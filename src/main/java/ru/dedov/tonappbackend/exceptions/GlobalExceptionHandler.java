package ru.dedov.tonappbackend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global Exception Handler
 *
 * @author Alexander Dedov
 * @since 02.06.2024
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * @param ex ексепшн, выбрасываемый в случае если не пройдена валидация входных параметров
	 * @return 404 с текстом ошибки
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
		String errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
		return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(EventsNotFoundException.class)
	public ResponseEntity<String> handleEventsNotFountException(EventsNotFoundException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}
}
