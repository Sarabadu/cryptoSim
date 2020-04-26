package com.sarabadu.cryptosim.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
//import static org.junit.

import com.sarabadu.cryptosim.exceptions.WalletInvalidMovementExeption;

public class WalletTest {

	
	@Test
	public void test_empty_wallet_creation() {
		
		Wallet w = new Wallet();
		
		assertNotNull(w, "expect Wallet to be created");
		assertNotNull(w.getName(), "name must to be empty string if not provided");
		assertNotNull(w.getHoldings(), "expect holding to be an empty map but is null");
		
		assertEquals("", w.getName(), "name must to be empty string if not provided");
		
		
	}
	@Test
	public void test_named_wallet_creation() {
		
		Wallet w = new Wallet("myWallet");
		
		assertNotNull(w.getName(), "name must initialized");
		assertEquals("myWallet", w.getName(), "name must to be empty string if not provided");

		assertNotNull(w.getHoldings(), "expect holding to be an empty map but is null");
		
	}
	
	@Test
	public void test_new_holding_add() {
		Wallet w = new Wallet("myWallet");
		BigDecimal actualqty = w.addHolding("BTC",new BigDecimal("0.000000001"));
		
		assertEquals( new BigDecimal("0.000000001"),w.getHoldings().get("BTC"));
		assertEquals(new BigDecimal("0.000000001"),actualqty);
		
	}
	
	@Test
	public void test_existing_holding_add() {
		Wallet w = new Wallet("myWallet");
		w.addHolding("BTC",new BigDecimal("0.000000001"));
		BigDecimal actualqty = w.addHolding("BTC",new BigDecimal("0.000000003"));
		
		assertEquals( new BigDecimal("0.000000004"),w.getHoldings().get("BTC"));
		assertEquals(new BigDecimal("0.000000004"),actualqty);
		
	}
	@Test
	public void test_holding_remove() throws Exception {
		Wallet w = new Wallet("myWallet");
		w.addHolding("BTC",new BigDecimal("0.000000003"));
		
		BigDecimal actualQty = w.removeHolding("BTC",new BigDecimal("0.000000001"));
		
		
		assertEquals( new BigDecimal("0.000000002"),w.getHoldings().get("BTC"));
		assertEquals(new BigDecimal("0.000000002"),actualQty);
	
		actualQty = w.removeHolding("BTC",new BigDecimal("0.000000002"));
		
		assertEquals(0,actualQty.compareTo(BigDecimal.ZERO));
		
	}
	
	@Test
	public void test_holding_cant_remove_more_that_existing() {
		Wallet w = new Wallet("myWallet");
		w.addHolding("BTC",new BigDecimal("0.000000003"));
		
		
		Exception ex = assertThrows(WalletInvalidMovementExeption.class, ()->{
			BigDecimal actualQty = w.removeHolding("BTC",new BigDecimal("0.000000004"));
			
		});
		
		
		assertEquals( new BigDecimal("0.000000003"),w.getHoldings().get("BTC"),"expect qty didn't change on ivnalid movment");
		
		assertTrue(ex.getMessage().contains("not enough"));
		assertTrue(ex.getMessage().contains("remove"));
		assertTrue(ex.getMessage().contains("BTC"));
		
		assertEquals("not enough BTC holdings to remove", ex.getMessage());
			
	}
	
	
	@Test
	public void test_multiple_holding_operation_consistency() throws Exception {
		Wallet w = new Wallet("myWallet");
		w.addHolding("BTC",new BigDecimal("0.000000003"));
		w.addHolding("BTC",new BigDecimal("0.000000003"));
		w.addHolding("BTD",new BigDecimal("100.003"));
		
		BigDecimal actualQty = w.removeHolding("BTC",new BigDecimal("0.000000001"));
		w.addHolding("BTC",new BigDecimal("0.000000003"));
		w.addHolding("BTD",new BigDecimal("1"));
		w.removeHolding("BTD",new BigDecimal("11.003"));
		w.addHolding("BTE",new BigDecimal("1"));
		w.addHolding("BTE",new BigDecimal("1"));
		w.removeHolding("BTE",new BigDecimal("1.00000"));
		w.removeHolding("BTE",new BigDecimal("1.00000"));
		w.addHolding("BTE",new BigDecimal("1"));
		w.addHolding("BTF",new BigDecimal("8"));
		w.addHolding("BTG",new BigDecimal("1"));
		w.removeHolding("BTG",new BigDecimal("1.00000"));
		
		
		assertEquals(new BigDecimal("0.000000008"),w.getHoldings().get("BTC"));
		assertEquals(0 ,w.getHoldings().get("BTD").compareTo(new BigDecimal("90")));
		assertEquals(0,w.getHoldings().get("BTE").compareTo(new BigDecimal("1")));
		assertEquals(0,w.getHoldings().get("BTF").compareTo(new BigDecimal("8")));
		assertNull(w.getHoldings().get("BTG"));
				
		assertEquals(4, w.getHoldings().size());

	}
}
