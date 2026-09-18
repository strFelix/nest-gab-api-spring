package br.com.gabnest.nest_gab_api.repository;

import br.com.gabnest.nest_gab_api.model.GuidelineHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GuidelineHistoryRepository extends MongoRepository<GuidelineHistory, String> {
    List<GuidelineHistory> findByGuidelineIdOrderByDateDesc(String guidelineId);
}

