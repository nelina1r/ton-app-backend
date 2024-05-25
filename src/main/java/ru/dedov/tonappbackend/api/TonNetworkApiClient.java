package ru.dedov.tonappbackend.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.dedov.tonappbackend.config.TonApiClientConfig;
import ru.dedov.tonappbackend.dto.tonapi.BalancesContainer;

/**
 * Feign Client для обращения к TON Api
 *
 * @author Alexander Dedov
 * @since 24.05.2024
 */
@FeignClient(name = "tonApiClient", url = "${ton-api.base-url.net}", configuration = TonApiClientConfig.class)
public interface TonNetworkApiClient {

	@GetMapping("/accounts/{account_id}/jettons")
	BalancesContainer getBalancesById(@PathVariable("account_id") String accountId,
									  @RequestParam("currencies") String currencies);

	@GetMapping("/accounts/{account_id}/jettons")
	BalancesContainer getBalancesById(@PathVariable("account_id") String accountId);
}
