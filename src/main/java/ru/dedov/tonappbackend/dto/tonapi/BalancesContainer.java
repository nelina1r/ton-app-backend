package ru.dedov.tonappbackend.dto.tonapi;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * TODO Class Description
 *
 * @author Alexander Dedov
 * @since 25.05.2024
 */
@Data
@NoArgsConstructor
public class BalancesContainer {

	private List<Balance> balances;
}
