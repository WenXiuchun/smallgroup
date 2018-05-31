package com.cc.smallgroup.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Probability entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProbabilityRespository extends JpaRepository<Probability, Long> {
   public List<Probability> findByGameId(String gameId);
   public List<Probability> findByGameIdAndTimePeriodAndHomeTeamPAndHomeTeamLostPAndGuestTeamPAndGuestTeamLostP(
           String gameId, String TimePeriod, String HomeTeamP, String HomeTeamLostP, String GuestTeamP, String GuestTeamLostP
   );
}
