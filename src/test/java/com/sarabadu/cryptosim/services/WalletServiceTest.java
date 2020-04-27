package com.sarabadu.cryptosim.services;

import org.h2.command.dml.MergeUsing.When;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.datatype.jdk8.OptionalDoubleSerializer;
import com.sarabadu.cryptosim.models.Wallet;
import com.sarabadu.cryptosim.models.WalletTransaction;
import com.sarabadu.cryptosim.repositories.WalletRepository;

@ExtendWith(SpringExtension.class)
public class WalletServiceTest {

	@Mock
	WalletRepository repo;
	
	@Mock
	IPriceService priceServ;
	
	@InjectMocks
	WalletService service;
	
	@Captor
	ArgumentCaptor<Wallet> walletCaptor;
	
	@Captor
	ArgumentCaptor<Wallet> walletCaptor2;
	
	@Test
	public void test_create_wallet() {
		Wallet wallet = new Wallet("testWallet");
		
		when(repo.save(Mockito.any(Wallet.class))).thenReturn(wallet);
		service.create(wallet);
		verify(repo,times(1)).save(wallet);
		
	}
	
	@Test
	public void test_update_wallet() {
		Wallet wallet = new Wallet("testWallet");
		Wallet wallet2 = new Wallet("testWallet2");
		wallet.setId(1l);
		
		when(repo.findById(1l)).thenReturn(Optional.of( wallet));
		when(repo.save(Mockito.any(Wallet.class))).thenReturn(wallet2);
		
		service.update(1l, wallet2);
		
		verify(repo,times(1)).findById(1l);
		verify(repo,times(1)).save(walletCaptor.capture());
		
		assertEquals(1l, walletCaptor.getValue().getId());
		assertEquals("testWallet2", walletCaptor.getValue().getName());
	}
	
	@Test
	public void test_delete_wallet() {
	
		service.delete(1l);
		
		verify(repo,times(1)).deleteById(1l);
		
	}
	
	@Test
	public void test_get_wallet() {
		Wallet wallet = new Wallet("testWallet");
		
		when(repo.findById(1l)).thenReturn(Optional.of( wallet));
		Wallet result = service.get(1l);
		verify(repo,times(1)).findById(1l);
		
		assertEquals(wallet, result);
		
	}
	@Test
	public void test_getAll_wallet() {
		Wallet wallet = new Wallet("testWallet");
		Wallet wallet2 = new Wallet("testWallet2");
		
		List<Wallet> res = new ArrayList<Wallet>();
		res.add(wallet);
		res.add(wallet2);
		
		when(repo.findAll()).thenReturn(res);
		
		List<Wallet> result = service.getAll();
		
		verify(repo,times(1)).findAll();
		assertEquals(2, result.size());	
	}
	
	@Test
	public void test_transfer_wallet() {
		WalletTransaction transaction = new WalletTransaction();
		transaction.setFromCurrency("USD");
		transaction.setFromQty(new BigDecimal(100));
		transaction.setFromWalletId(1l);

		transaction.setToCurrency("BTC");
		transaction.setToWalletId(2l);
		
		Wallet wallet = new Wallet("testWallet");
		wallet.addHolding("USD", new BigDecimal(500));
		wallet.setId(1l);
		
		Wallet wallet2 = new Wallet("testWallet2");
		wallet2.setId(2l);
		
		when(repo.findById(1l)).thenReturn(Optional.of(wallet));
		when(repo.findById(2l)).thenReturn(Optional.of(wallet2));
		
		when(priceServ.priceOf("BTC","USD")).thenReturn(new BigDecimal(50));
		
		
		Wallet result = service.transfer(transaction);
		

		verify(repo,times(2)).findById(Mockito.any(Long.class));

		verify(repo,times(2)).save(walletCaptor.capture());
//		verify(repo,times(1)).save(walletCaptor2.capture());
		
		verify(priceServ, times(1)).priceOf("BTC","USD");
		
		List<Wallet> wallets = walletCaptor.getAllValues();

			System.out.println(wallets);
		Wallet w1 = wallets.stream().filter(wal -> Long.valueOf(1).equals(wal.getId()))
				.findAny().orElse(null);
		Wallet w2 = wallets.stream().filter(wal -> Long.valueOf(2).equals(wal.getId()))
				.findAny().orElse(null);
		
		assertEquals("testWallet", w1.getName());
		assertEquals("testWallet2", w2.getName());

		assertEquals(2, w2.getHoldings().get("BTC").doubleValue());	
		assertEquals(400, w1.getHoldings().get("USD").doubleValue());	
	}

	
}
