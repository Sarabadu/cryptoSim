package com.sarabadu.cryptosim.models;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.MapKeyColumn;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import com.sarabadu.cryptosim.exceptions.WalletInvalidMovementExeption;

import lombok.Data;

@Data
@Entity
public class Wallet {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private String name;
	
	@ElementCollection
	@JoinTable(name="holdings", joinColumns= @JoinColumn(name="id"))
	@MapKeyColumn (name="holding_id")
	@Column(name="qty")
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
