package com.xm.crypto.recommendation.integration;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class IntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void test_getNormalizedRange_for_all_cryptos() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/cryptos/normalized-range"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$[?(@.symbol == 'ETH')].normalizedRange").value(0.64))
				.andExpect(MockMvcResultMatchers.jsonPath("$[?(@.symbol == 'XRP')].normalizedRange").value(0.5061))
				.andExpect(MockMvcResultMatchers.jsonPath("$[?(@.symbol == 'DOGE')].normalizedRange").value(0.5047))
				.andExpect(MockMvcResultMatchers.jsonPath("$[?(@.symbol == 'LTC')].normalizedRange").value(0.5))
				.andExpect(MockMvcResultMatchers.jsonPath("$[?(@.symbol == 'BTC')].normalizedRange").value(0.43));
	}

	@Test
	void test_getCryptoStats_for_BTC() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/cryptos/stats/BTC"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.min").value("33276.59"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.max").value("47722.66"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.newest").value("38415.79"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.oldest").value("46813.21"));
	}

	@Test
	void test_getCryptoStats_for_BTC_for_date_range() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/cryptos/stats/BTC?startDate=2022-01-01&endDate=2022-03-01"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.min").value("33276.59"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.max").value("47722.66"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.newest").value("38415.79"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.oldest").value("46813.21"));
	}

	@Test
	void test_getCryptoStats_for_BTC_for_date_missing_endDate() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/cryptos/stats/BTC?startDate=2022-01-01&endDate="))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.errorType").value("Bad Request"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("A valid time range needs to be provided!"));
	}

	@Test
	void test_getCryptoStats_for_BTC_for_date_invalid_date_range() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/cryptos/stats/BTC?startDate=2022-03-01&endDate=2022-01-01"))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.errorType").value("Bad Request"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("Start date must be before end date!"));
	}

	@Test
	void test_getCryptoStats_for_BTC_for_date_invalid_format() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/cryptos/stats/BTC?startDate=2022-01-01&endDate=202255-03-01"))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.errorType").value("Bad Request"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("Invalid date format. Please use yyyy-MM-dd."));
	}

	@Test
	void test_getCryptoStats_for_BTC_for_date_missing_startDate() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/cryptos/stats/BTC?endDate=2022-01-01"))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.errorType").value("Bad Request"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("A valid time range needs to be provided!"));
	}

	@Test
	void test_getCryptoStats_for_invalid_symbol() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/cryptos/stats/BTCC"))
				.andExpect(MockMvcResultMatchers.status().isNotFound())
				.andExpect(MockMvcResultMatchers.jsonPath("$.errorType").value("Not Found"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("No data found for symbol: BTCC"));
	}

	@Test
	void test_get_highest_normalized_range_no_data_for_date() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/cryptos/highest-normalized-range/2023-10-01"))
				.andExpect(MockMvcResultMatchers.status().isNotFound())
				.andExpect(MockMvcResultMatchers.jsonPath("$.errorType").value("Not Found"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("No cryptocurrencies found with valid data for the date: 2023-10-01"));
	}

	@Test
	void test_get_highest_normalized_range_invalid_date_format() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/cryptos/highest-normalized-range/2023-10-011"))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.errorType").value("Bad Request"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("Invalid date format. Please use yyyy-MM-dd."));
	}

	@Test
	void test_get_highest_normalized_range() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/cryptos/highest-normalized-range/2022-01-01"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.symbol").value("ETH"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.normalizedRange").value("0.64"));
	}

}