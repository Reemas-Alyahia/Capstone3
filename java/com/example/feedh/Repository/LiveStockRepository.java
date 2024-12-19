package com.example.feedh.Repository;

import com.example.feedh.Model.LiveStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

// Ebtehal - LiveStock Repository
@Repository
public interface LiveStockRepository extends JpaRepository<LiveStock, Integer> {
    LiveStock findLiveStockById(Integer id);

    @Query("select ls from LiveStock ls where ls.breed=?1 and ls.type=?2")
    List<LiveStock> getLiveStockByBreedAndType(String breed ,String type);
}
