package com.sarabadu.cryptosim.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sarabadu.cryptosim.models.Wallet;


@RestController
@RequestMapping("/api/wallets")
public class WalletController {

	
	
	@GetMapping("/")
	public List<Wallet> test( ) {
		return null;
		
	}
}
