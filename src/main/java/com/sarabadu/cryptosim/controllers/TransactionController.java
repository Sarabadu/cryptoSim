package com.sarabadu.cryptosim.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sarabadu.cryptosim.models.Wallet;
import com.sarabadu.cryptosim.models.WalletTransaction;
import com.sarabadu.cryptosim.services.IWalletService;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

	@Autowired
	IWalletService service;
	
	
	@PostMapping(path = "/",  produces = {MediaType.APPLICATION_JSON_VALUE})
	public @ResponseBody Wallet createWallet(@RequestBody WalletTransaction transaction) {
		return service.transfer(transaction);
	}
}
