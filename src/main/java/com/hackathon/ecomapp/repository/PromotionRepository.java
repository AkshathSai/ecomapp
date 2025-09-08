package com.hackathon.ecomapp.repository;

import com.hackathon.ecomapp.model.Promotion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PromotionRepository extends MongoRepository<Promotion, String> {
    Optional<Promotion> findByCodeAndActiveTrue(String code);
}
