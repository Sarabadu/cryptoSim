package com.sarabadu.cryptosim.models;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import com.sarabadu.cryptosim.exceptions.WalletInvalidMovementExeption;

import lombok.Data;

@Data

public class Wallet {

	private String name;
	private Map<String, BigDecimal> holdings;

	public Wallet() {
		this("");
	}

	public Wallet(String name) {
		this.name = name;
		this.holdings = new HashMap<String, BigDecimal>();
	}

	public BigDecimal addHolding(String coin, BigDecimal qty) {
		BigDecimal currentQty = this.holdings.getOrDefault(coin, new BigDecimal(0));
		BigDecimal newQty = currentQty.add(qty);
		this.holdings.put(coin, newQty);

		return newQty;
	}

	public BigDecimal removeHolding(String coin, BigDecimal qty) throws WalletInvalidMovementExeption {
		BigDecimal currentQty = this.holdings.getOrDefault(coin, new BigDecimal(0));
		
		if (currentQty.compareTo(qty) == 0) {
			this.holdings.remove(coin);
			return BigDecimal.ZERO;
		}

		BigDecimal newQty = currentQty.subtract(qty);

		if (newQty.compareTo(BigDecimal.ZERO) > 0) {
			this.holdings.put(coin, newQty);
		} else {
			throw new WalletInvalidMovementExeption(String.format("not enough %s holdings to remove", coin));
		}
		return newQty;

	}
}
