package com.sarabadu.cryptosim.services;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.apache.tomcat.util.http.fileupload.util.Streams;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.codec.ClientCodecConfigurer;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sun.el.stream.Stream;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PriceService implements IPriceService{

	private final WebClient webClient;
	private static class TypedMap extends HashMap<String, BigDecimal>{}
	
	public PriceService(WebClient.Builder webClientBuilder) {
		
		this.webClient = webClientBuilder
				.baseUrl("https://min-api.cryptocompare.com/data/")
				.defaultHeader(HttpHeaders.AUTHORIZATION, "Apikey bce6e903f5a7c36c049324061ed18c0fa06a6c80abc92854b956b595cc344497")
		//		.codecs(configurer)
				.build();
	}
	@Override
	public BigDecimal priceOf(String currency, String expressedIn) {
		  return webClient.get().uri( uribuilder -> uribuilder
				.path("price")
				.queryParam("fsym", currency)
				.queryParam("tsyms", expressedIn)
				.build()
				)
		.retrieve().bodyToFlux(TypedMap.class)
		.map(map -> map.get(expressedIn)).blockFirst();
	}

	@Override
	public Map<String, BigDecimal> getPrices(Integer startAt, Integer qty) {
		List<String> sublist = new ArrayList<String>();
		
		try {
			Integer fetchQty = qty > 300 ? 300 : qty;
			List<String> currencies = getAllCurrencies(); 
			  sublist= currencies.subList(startAt, Math.min(startAt+ fetchQty, currencies.size()) );
			
			
			
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String a = String.join(",", sublist);
		
		return webClient.get().uri( uribuilder -> uribuilder
				.path("pricemulti")
				.queryParam("fsyms", a)
				.queryParam("tsyms", "USD")
				.build()
				)
				.retrieve().bodyToFlux(TypedMap.class)
				
				.log()
				.blockFirst();
		//return null;
	}
	
	

	private List<String> getAllCurrencies() throws InterruptedException{
		
		List<String> result =  new ArrayList<String>();
		
		JsonNode data = webClient.get().uri( uribuilder -> uribuilder
				.path("all/coinlist")
				.build()
				)
		.retrieve().bodyToFlux(ObjectNode.class)
		.map(node -> node.path("Data")).blockFirst();
	
		data.fields().forEachRemaining(field -> result.add(field.getKey()));
		System.out.println(result);
		return result;
	}

}

