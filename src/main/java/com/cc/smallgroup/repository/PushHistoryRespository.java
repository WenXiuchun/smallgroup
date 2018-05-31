package com.cc.smallgroup.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.*;

import java.util.List;

@Repository
public interface PushHistoryRespository extends JpaRepository<PushHistory, Long>  {
    public PushHistory findByGameId(String gameId);
    public List<PushHistory> findByChecked(int checked);
    public List<PushHistory> findByMatchDate(String match_date);
}
