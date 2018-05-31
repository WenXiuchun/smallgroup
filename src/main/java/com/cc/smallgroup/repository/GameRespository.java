package com.cc.smallgroup.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.*;
import java.util.List;

@Repository
public interface GameRespository extends JpaRepository<Game, Long> {
    List<Game> findByMatchId(String matchId);
    Game findById(String id);

    @Query("select g from Game g where g.matchDate = ?2 and g.year = ?1")
    public List<Game>findGamesByYearAndDate(String year, String date);
}
