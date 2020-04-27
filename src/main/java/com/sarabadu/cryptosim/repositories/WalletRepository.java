package com.sarabadu.cryptosim.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.sarabadu.cryptosim.models.Wallet;

@Repository
public interface WalletRepository extends CrudRepository<Wallet, Long> {
	
	List<Wallet> findAll();

}
