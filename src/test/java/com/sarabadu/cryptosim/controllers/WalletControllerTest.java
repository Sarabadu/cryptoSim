package com.sarabadu.cryptosim.controllers;



import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.spi.json.GsonJsonProvider;
import com.sarabadu.cryptosim.models.Wallet;
import com.sarabadu.cryptosim.services.IWalletService;

@WebMvcTest(WalletController.class)
public class WalletControllerTest {

	@MockBean
	IWalletService service;

	@Autowired
	MockMvc mockMvc;

	@Captor
	ArgumentCaptor<Wallet> walletCaptor;
	
	@Captor
	ArgumentCaptor<Long> longCaptor;
//	
//	@InjectMocks
//	WalletController controller;
//	
//	JacksonTester<Wallet> jsonWallet;
//	
	@BeforeAll
	protected static void setUpBeforeClass() throws Exception {

	}
//
//	@BeforeEach
//	protected void setUp() throws Exception {
//	}

	@Test
	public void test_get_all_wallets() throws Exception {
		List<Wallet> wallets = new ArrayList<Wallet>();
		Wallet w1 = new Wallet("firstWallet");
		w1.addHolding("BTC", new BigDecimal(2));
		w1.addHolding("BTU", new BigDecimal(1));

		wallets.add(w1);
		wallets.add(new Wallet("secondWallet"));
		wallets.add(new Wallet("thirdWallet"));

		when(service.getAll()).thenReturn(wallets);

		mockMvc.perform(get("/api/wallets/")).andDo(print()).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$", hasSize(3)))
				.andExpect(jsonPath("$[*].name", hasItem("firstWallet")))
				.andExpect(jsonPath("$[*].name", hasItem("secondWallet")))
				.andExpect(jsonPath("$[*].name", hasItem("thirdWallet")))
				.andExpect(jsonPath("$[0].holdings").isMap())
				.andExpect(jsonPath("$[0].holdings",hasEntry("BTC", 2)))
				.andExpect(jsonPath("$[0].holdings",hasEntry("BTU", 1)));
		// .andExpect(jsonPath("$[*]",hasItem(allOf(Matchers.<Wallet>hasProperty("name",equalTo("secondWallet"))))));
		
		verify(service,times(1)).getAll();
		
	}
	
	@Test
	public void test_get_single_wallet() throws Exception {
		Wallet w1 = new Wallet("firstWallet");
		w1.addHolding("BTC", new BigDecimal(2));
		w1.addHolding("BTU", new BigDecimal(1));

		when(service.get(any())).thenReturn(w1);

		mockMvc.perform(get("/api/wallets/1/"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$").isMap())
				.andExpect(jsonPath("$.name",equalTo("firstWallet")))
				.andExpect(jsonPath("$.holdings").isMap())
				.andExpect(jsonPath("$.holdings",hasEntry("BTC", 2)))
				.andExpect(jsonPath("$.holdings",hasEntry("BTU", 1)));

		verify(service,times(1)).get(1l);
		
	}
	
	@Test
	public void test_create_wallet() throws Exception {
		Wallet w1 = new Wallet("mockWallet");

		when(service.create(Mockito.any(Wallet.class))).thenReturn(w1);

		mockMvc.perform(post("/api/wallets/")
				        .content("{\"name\": \"aWallet\"}")
				        .contentType(MediaType.APPLICATION_JSON)
				        .characterEncoding("utf-8"))
				.andDo(print())
				.andExpect(status().isOk())
				//.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$").isMap())
				.andExpect(jsonPath("$.name",equalTo("mockWallet")));

		verify(service,times(1)).create(walletCaptor.capture());
		
		Wallet recibedWallet = walletCaptor.getValue();
		
		assertEquals("aWallet",recibedWallet.getName());
		assertEquals(0,recibedWallet.getHoldings().size());
		
		
	}
	
	@Test
	public void test_update_wallet() throws Exception {
		Wallet w1 = new Wallet("mockWallet");

		when(service.update(any(),Mockito.any(Wallet.class))).thenReturn(w1);

		mockMvc.perform(put("/api/wallets/1")
				        .content("{\"name\": \"aWallet\"}")
				        .contentType(MediaType.APPLICATION_JSON)
				        .characterEncoding("utf-8"))
				.andDo(print())
				.andExpect(status().isOk())
				//.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$").isMap())
				.andExpect(jsonPath("$.name",equalTo("mockWallet")));

		verify(service,times(1)).update(longCaptor.capture(),walletCaptor.capture());
		
		Wallet recibedWallet = walletCaptor.getValue();
		
		assertEquals("aWallet",recibedWallet.getName());
		assertEquals(0,recibedWallet.getHoldings().size());
		assertEquals(1l, longCaptor.getValue());
		
		
	}

}
