package com.sarabadu.cryptosim.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sarabadu.cryptosim.models.Wallet;
import com.sarabadu.cryptosim.services.IWalletService;




@RestController
@RequestMapping("/api/wallets")
public class WalletController {

	@Autowired
	IWalletService service;
	
	@GetMapping(path = "/",produces = {MediaType.APPLICATION_JSON_VALUE})
	public @ResponseBody List<Wallet> getAllWallets( ) {
		return service.getAll();
	}
	
	@GetMapping(path = "/{id}",produces = {MediaType.APPLICATION_JSON_VALUE})
	public @ResponseBody Wallet getWalletById(@PathVariable Long id ) {
		return service.get(id);
	}
	
	@PostMapping(path = "/",  produces = {MediaType.APPLICATION_JSON_VALUE})
	public @ResponseBody Wallet createWallet(@RequestBody Wallet wallet) {
		return service.create(wallet);
	}
	
	@PutMapping(path = "/{id}",  produces = {MediaType.APPLICATION_JSON_VALUE})
	public @ResponseBody Wallet updateWallet(@PathVariable Long id , @RequestBody Wallet newWallet) {
		return service.update(id, newWallet);
	}
}
