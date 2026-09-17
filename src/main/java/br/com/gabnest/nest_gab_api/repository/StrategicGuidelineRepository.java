package br.com.gabnest.nest_gab_api.repository;

import br.com.gabnest.nest_gab_api.model.StrategicGuideline;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StrategicGuidelineRepository extends MongoRepository<StrategicGuideline, String> {
    List<StrategicGuideline> findByActiveTrue();
}