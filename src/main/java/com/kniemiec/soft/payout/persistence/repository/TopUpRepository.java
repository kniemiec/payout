package com.kniemiec.soft.payout.persistence.repository;

import com.kniemiec.soft.payout.persistence.model.TopUpStatusData;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface TopUpRepository extends ReactiveMongoRepository<TopUpStatusData, String> {
}
