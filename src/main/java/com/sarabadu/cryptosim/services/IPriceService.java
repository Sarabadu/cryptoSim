package com.sarabadu.cryptosim.services;

import java.math.BigDecimal;
import java.util.Map;

public interface IPriceService {

	BigDecimal priceOf(String currency, String expressedIn);
	Map<String, BigDecimal> getPrices(Integer startAt,Integer qty); 

}
