package org.example.unicare_backend.repositories;

import org.example.unicare_backend.models.TestResult;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestResultRepository extends MongoRepository<TestResult, String> {
    List<TestResult> findByUserId(String userId, Sort sort);
}
