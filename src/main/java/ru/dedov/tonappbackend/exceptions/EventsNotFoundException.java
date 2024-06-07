package ru.dedov.tonappbackend.exceptions;

/**
 * TODO Class Description
 *
 * @author Alexander Dedov
 * @since 07.06.2024
 */
public class EventsNotFoundException extends RuntimeException {

	public EventsNotFoundException(String message) {
		super(message);
	}
}
