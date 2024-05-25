package ru.dedov.tonappbackend.dto.tonapi;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TODO Class Description
 *
 * @author Alexander Dedov
 * @since 25.05.2024
 */
@Data
@NoArgsConstructor
public class Lock {

	private String amount;
	private long till;
}
