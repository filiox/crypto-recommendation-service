package com.xm.crypto.recommendation.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class IntegrationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	@DisplayName("Should Return Normalized Range for All Cryptos")
	void getNormalizedRange_ForAllCryptos_ShouldReturnNormalizedRange() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/cryptos/sorted-by-normalized-range"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$[?(@.symbol == 'ETH')].normalizedRange").value(0.64))
				.andExpect(MockMvcResultMatchers.jsonPath("$[?(@.symbol == 'XRP')].normalizedRange").value(0.5061))
				.andExpect(MockMvcResultMatchers.jsonPath("$[?(@.symbol == 'DOGE')].normalizedRange").value(0.5047))
				.andExpect(MockMvcResultMatchers.jsonPath("$[?(@.symbol == 'LTC')].normalizedRange").value(0.5))
				.andExpect(MockMvcResultMatchers.jsonPath("$[?(@.symbol == 'BTC')].normalizedRange").value(0.43));
	}

	@Test
	@DisplayName("Should Return Crypto Stats for BTC")
	void getCryptoStats_ForBTC_ShouldReturnCryptoStatsForBTC() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/cryptos/BTC/stats"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.min").value("33276.59"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.max").value("47722.66"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.newest").value("38415.79"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.oldest").value("46813.21"));
	}

	@Test
	@DisplayName("Should Return Crypto Stats for BTC within Date Range")
	void getCryptoStats_ForBTCWithinDateRange_ShouldReturnCryptoStats() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/cryptos/BTC/stats?startDate=2022-01-01&endDate=2022-03-01"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.min").value("33276.59"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.max").value("47722.66"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.newest").value("38415.79"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.oldest").value("46813.21"));
	}

	@Test
	@DisplayName("Should Handle Missing End Date for BTC Crypto Stats")
	void getCryptoStats_MissingEndDateForBTC_ShouldHandleMissingEndDate() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/cryptos/BTC/stats?startDate=2022-01-01&endDate="))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.errorType").value("Bad Request"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("A valid time range needs to be provided!"));
	}

	@Test
	@DisplayName("Should Handle Invalid Date Range for BTC Crypto Stats")
	void getCryptoStats_InvalidDateRangeForBTC_ShouldHandleInvalidDateRange() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/cryptos/BTC/stats?startDate=2022-03-01&endDate=2022-01-01"))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.errorType").value("Bad Request"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("Start date must be before end date!"));
	}

	@Test
	@DisplayName("Should Handle Invalid Date Format for BTC Crypto Stats")
	void getCryptoStats_InvalidDateFormatForBTC_ShouldHandleInvalidDateFormat() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/cryptos/BTC/stats?startDate=2022-01-01&endDate=202255-03-01"))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.errorType").value("Bad Request"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("Invalid date format. Please use yyyy-MM-dd."));
	}

	@Test
	@DisplayName("Should Handle Missing Start Date for BTC Crypto Stats")
	void getCryptoStats_MissingStartDateForBTC_ShouldHandleMissingStartDate() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/cryptos/BTC/stats?endDate=2022-01-01"))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.errorType").value("Bad Request"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("A valid time range needs to be provided!"));
	}

	@Test
	@DisplayName("Should Handle Invalid Symbol for Crypto Stats")
	void getCryptoStats_InvalidSymbol_ShouldHandleInvalidSymbol()  throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/cryptos/BTCC/stats"))
				.andExpect(MockMvcResultMatchers.status().isNotFound())
				.andExpect(MockMvcResultMatchers.jsonPath("$.errorType").value("Not Found"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("No data found for symbol: BTCC"));
	}

	@Test
	@DisplayName("No Data exist for the input Date in Highest Normalized Range")
	void getHighestNormalizedRange_NoDataForDate_ShouldHandleNoDataForDate()  throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/cryptos/highest-normalized-range/2023-10-01"))
				.andExpect(MockMvcResultMatchers.status().isNotFound())
				.andExpect(MockMvcResultMatchers.jsonPath("$.errorType").value("Not Found"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("No cryptocurrencies found with valid data for the date: 2023-10-01"));
	}

	@Test
	@DisplayName("Should Handle an Invalid Date Format in Highest Normalized Range")
	void getHighestNormalizedRange_InvalidDateFormat_ShouldHandleInvalidDateFormat()  throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/cryptos/highest-normalized-range/2023-10-011"))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.jsonPath("$.errorType").value("Bad Request"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("Invalid date format. Please use yyyy-MM-dd."));
	}

	@Test
	@DisplayName("Should Return the Highest Normalized Range for a date")
	void getHighestNormalizedRange_ShouldReturnHighestNormalizedRange() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/cryptos/highest-normalized-range/2022-01-01"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.symbol").value("ETH"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.normalizedRange").value("0.64"));
	}

}