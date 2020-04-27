package com.sarabadu.cryptosim.controllers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sarabadu.cryptosim.models.Wallet;
import com.sarabadu.cryptosim.services.IPriceService;

@RestController
@RequestMapping("/api/prices")
public class PriceController {

	@Autowired
	IPriceService service;
	
	@GetMapping(path = "/",produces = {MediaType.APPLICATION_JSON_VALUE})
	public @ResponseBody Map<String,BigDecimal> getAllWallets(@RequestParam Integer startAt, @RequestParam Integer qty ) {
		return service.getPrices(startAt, qty);
	}
	
	@GetMapping(path = "/{currency}",produces = {MediaType.APPLICATION_JSON_VALUE})
	public @ResponseBody BigDecimal getWalletById(@PathVariable String currency) {
		return service.priceOf(currency, "USD");
	}
}
