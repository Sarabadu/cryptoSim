package com.sarabadu.cryptosim.controllers;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.sarabadu.cryptosim.models.Wallet;
import com.sarabadu.cryptosim.models.WalletTransaction;
import com.sarabadu.cryptosim.services.IWalletService;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

	@MockBean
	IWalletService service;

	@Autowired
	MockMvc mockMvc;

	@Captor
	ArgumentCaptor<WalletTransaction> walletTrxCaptor;
	

	
	@Test
	public void test_wallet_transaction() throws Exception {
		Wallet w1 = new Wallet("mockWallet");

		when(service.transfer(Mockito.any(WalletTransaction.class))).thenReturn(w1);

		mockMvc.perform(post("/api/transactions/")
				        .content("{\n" + 
				        		"  \"fromWalletId\": 1,\n" + 
				        		"  \"fromCurrency\": \"USD\",\n" + 
				        		"  \"fromQty\": 100,\n" + 
				        		"  \"toWalletId\": 2,\n" + 
				        		"  \"toCurrency\": \"BTC\"\n" + 
				        		"}")
				        .contentType(MediaType.APPLICATION_JSON)
				        .characterEncoding("utf-8"))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$").isMap())
				.andExpect(jsonPath("$.name",equalTo("mockWallet")));

		verify(service,times(1)).transfer(walletTrxCaptor.capture());
		
		WalletTransaction recibedTrx = walletTrxCaptor.getValue();
		
		assertEquals(1l,recibedTrx.getFromWalletId());
		assertEquals(2l,recibedTrx.getToWalletId());
		assertEquals("USD",recibedTrx.getFromCurrency());
		assertEquals("BTC",recibedTrx.getToCurrency());
		assertEquals(0, recibedTrx.getFromQty().compareTo(new BigDecimal("100")));
		
	}
}
