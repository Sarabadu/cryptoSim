package com.sarabadu.cryptosim.models;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class WalletTransaction {

	private Long fromWalletId;
	private String fromCurrency;
	private BigDecimal fromQty;
	
	private Long toWalletId;
	private String toCurrency;
	
}
