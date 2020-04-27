package com.sarabadu.cryptosim.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sarabadu.cryptosim.models.Wallet;
import com.sarabadu.cryptosim.models.WalletTransaction;

@Service
public interface IWalletService {

	public List<Wallet> getAll();

	public Wallet get(Long id);

	public Wallet create(Wallet wallet);

	public Wallet update(Long id, Wallet newWallet);

	public Boolean delete(Long id);

	public Wallet transfer(WalletTransaction any);

}
