package com.kniemiec.soft.payout.repository;

import com.kniemiec.soft.payout.model.TopUpStatusData;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface TopUpRepository extends ReactiveMongoRepository<TopUpStatusData, String> {
}
