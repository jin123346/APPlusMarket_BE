package com.aplus.aplusmarket.repository;

import com.aplus.aplusmarket.documents.TokenHistory;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TokenHistoryRepository extends MongoRepository<TokenHistory,String> {

    Optional<TokenHistory> findByRefreshToken(String token);
}
