package pl.vavatech.auction.blc.service;

import java.math.BigDecimal;
import java.util.List;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.vavatech.auction.blc.aop.Trace;
import pl.vavatech.auction.blc.model.Auction;
import pl.vavatech.auction.blc.repo.AuctionRepo;

@Transactional
@Service
public class AuctionService {
	@Value("${maxShippingPrice}")
	private BigDecimal maxShippingPrice;

	@Inject
	private AuctionRepo repo;

	public Auction find(Long id) {
		return repo.find(id);
	}

	@Trace
	public List<Auction> findAll() {
		return repo.findAll();
	}

	@Trace
	public Long insert(Auction auction) {
		return repo.insert(auction);
	}

	public void update(Auction auction) {
		repo.update(auction);
	}

	public void delete(Long id) {
		repo.delete(id);
	}

}
