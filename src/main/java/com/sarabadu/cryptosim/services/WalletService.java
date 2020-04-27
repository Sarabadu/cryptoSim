package com.sarabadu.cryptosim.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sarabadu.cryptosim.exceptions.WalletInvalidMovementExeption;
import com.sarabadu.cryptosim.models.Wallet;
import com.sarabadu.cryptosim.models.WalletTransaction;
import com.sarabadu.cryptosim.repositories.WalletRepository;

@Service
public class WalletService implements IWalletService {

	@Autowired
	WalletRepository repository;
	
	@Autowired
	IPriceService priceServ;
	
	@Override
	public List<Wallet> getAll() {
		return repository.findAll();
	}

	@Override
	public Wallet get(Long id) {

		return repository.findById(id).orElse(null);
	}

	@Override
	public Wallet create(Wallet wallet) {
		return repository.save(wallet);
	}

	@Override
	public Wallet update(Long id, Wallet newWallet) {
		return repository.findById(id)
				.map(oldWallet -> {
					oldWallet.setName(newWallet.getName());
					oldWallet.setHoldings(newWallet.getHoldings());
				   return repository.save(oldWallet);	})
				.orElseGet(()->{
					return repository.save(newWallet);
				});

	}

	@Override
	public Boolean delete(Long id) {
		repository.deleteById(id);
		return true;
	}

	@Override
	public Wallet transfer(WalletTransaction trx) {
		Optional<Wallet> from = null;
		if (trx.getFromWalletId() != null) {
			from = repository.findById(trx.getFromWalletId());
			
			from.ifPresent(w->{
				try {
					w.removeHolding(trx.getFromCurrency(), trx.getFromQty());
					repository.save(w);
				} catch (WalletInvalidMovementExeption e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				});
		}
		
		BigDecimal price = priceServ.priceOf(trx.getToCurrency(), trx.getFromCurrency());
		BigDecimal destQty = trx.getFromQty().divide(price); 
		
		Optional<Wallet> dest = repository.findById(trx.getToWalletId());
		dest.ifPresent(w -> {
			w.addHolding(trx.getToCurrency(), destQty);
			repository.save(w);
		});
		
		return null;
	}

}
