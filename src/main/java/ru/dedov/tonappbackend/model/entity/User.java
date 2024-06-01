package ru.dedov.tonappbackend.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * Сущность Пользователь
 *
 * @author Alexander Dedov
 * @since 23.05.2024
 */
@Data
@Entity
@Table(name = "users")
@RequiredArgsConstructor
@AllArgsConstructor
public class User {

	@Id
	@Column(name = "account_id")
	@JsonProperty("account_id")
	private String accountId;

	@Column(name = "ton_balance")
	@JsonProperty("ton_balance")
	private Double tonBalance = 0.0;

	@Column(name = "account_balance")
	@JsonProperty("account_balance")
	private Double accountBalance = 0.0;

}
