package com.cc.smallgroup.service;
import com.cc.smallgroup.web.WebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import com.cc.smallgroup.model.PredictMessage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.cc.smallgroup.repository.PushHistoryRespository;
import com.cc.smallgroup.repository.UserRespository;
import com.cc.smallgroup.repository.GameRespository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
@Configuration
public class Algorithm {
    private static Logger logger = LoggerFactory.getLogger(Algorithm.class);
    static  public Map<String, Boolean> pushedMatchTime  = new HashMap<String, Boolean>();
    private Game game;
    @Autowired
    private WebSocket webSocket;
    @Autowired
    private PushHistoryRespository pushHistoryRespository;
    @Autowired
    private UserRespository attackRespository;
    @Autowired
    private GameRespository gameRespository;

    public PredictMessage compute() {

        if(game.getHomeScore() ==0 && game.getGuestScore() == 0){
            PredictMessage predictMessage = new PredictMessage();
            predictMessage.setBetTypeName("小球");
            predictMessage.setGuest(game.getGuestTeamEng());
            predictMessage.setHome(game.getHomeTeamEng());
            predictMessage.setLive(true);
            predictMessage.setSportName("足球");
            logger.info(predictMessage.toJsonStr());
            return predictMessage;
        }
        return null;
    }

    public void pushMessage() {
        String matchTime = game.getStartsTime();
        try {
            if (null != matchTime && null == pushHistoryRespository.findByGameId(game.getId())) {
                double minutes = timeDifference(matchTime);
                //中场休息15分钟
                if (minutes > 58.0 && minutes < 62.0) {
                    PredictMessage predictMessage = compute();
                    if (null != predictMessage) {
                        savePushHistory(predictMessage);
                    }

                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void savePushHistory(PredictMessage predictMessage){

        PushHistory history = new PushHistory();
        SimpleDateFormat format = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss");

        history.setGameId(game.getId());
        history.setGuessType(predictMessage.getBetTypeName());
        history.setMatchDate(game.getMatchDate());
        history.setLeague(game.getLeagueEng());
        history.setHomeTeam(game.getHomeTeamEng());
        history.setGuestTeam(game.getGuestTeamEng());
        history.setHomeTeamId(game.getHomeTeamId());
        history.setGuestTeamId(game.getGuestTeamId());
        history.setOpenTime(game.getOpenTime());
        history.setPushTime(format.format(new Date()));
        history.setChecked(0);
        logger.info("push result: " + history.toString());
        pushHistoryRespository.save(history);
    }

    public double timeDifference(String matchTime) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy,MM,dd,HH,mm,ss");
        String[] times = matchTime.split(",");
        matchTime = times[0]  +"," + (Integer.parseInt(times[1])+1) + "," + times[2] +"," + times[3] + "," + times[4] +"," + times[5];

        long difftime = new Date().getTime() - new Date().getTime();
        Date dMatchTime = df.parse(matchTime);
        Date now = new Date();
        if(game.getMatchState() == 1) {
            double goTime = Math.floor((now.getTime() - dMatchTime.getTime() - difftime)/60000);
            if (goTime > 45.0) goTime = 45;
            if (goTime < 1.0) goTime = 1;
            logger.info("startTime:" + matchTime + " matchstate:" + game.getMatchState() + " goTime:" + goTime);
            return goTime;
        } else if (game.getMatchState() == 3) {
            double goTime = Math.floor((now.getTime() - dMatchTime.getTime() - difftime)/60000) + 46;
            if (goTime > 90.0) goTime = 90;
            if (goTime < 46.0) goTime = 46;
            logger.info("startTime:" + matchTime + " matchstate:" + game.getMatchState() + " goTime:" + goTime);
            return goTime;
        }

        return 0.0;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    @Scheduled(cron = "0 0 0/1 * * ?") // 每xiaoshi执行一次
    private void checkForecast(){
        List<PushHistory> histories = pushHistoryRespository.findByChecked(0);
        for (PushHistory history: histories){
            Game game = gameRespository.findById(history.getGameId());
            if(game.getMatchState() >= 0){
                continue;
            }

            List<Attack> homeAttacks = attackRespository.findByGameIdAndTeamIdAndAttackType(history.getGameId(),
                    history.getHomeTeamId(),"3");
            int homeScore = game.getHomeScore();
            String goalRecord = "";
            int bingoHome = 1;
            for (Attack attack: homeAttacks){
                goalRecord = goalRecord + attack.getAttackTime() + ";";
                if (Integer.parseInt(attack.getAttackTime()) < 80){
                    bingoHome = 0;
                }
            }
            List<Attack> guestAttacks = attackRespository.findByGameIdAndTeamIdAndAttackType(history.getGameId(),
                    history.getGuestTeamId(),"3");
            int bingoGuest = 1;
            for (Attack attack: guestAttacks){
                goalRecord = goalRecord + attack.getAttackTime() + ";";
                if (Integer.parseInt(attack.getAttackTime()) < 80){
                    bingoGuest = 0;
                }
            }
            int guestScore = game.getGuestScore();
            int bingo = 0;
            if (bingoHome == 1 && bingoGuest == 1){
                bingo = 1;
            }
            history.setHomeScore(homeScore);
            history.setGuestScore(guestScore);
            history.setGoalRecord(goalRecord);
            history.setBingo(bingo);
            history.setChecked(1);
            logger.info("check push result :" + history.toString());
            pushHistoryRespository.save(history);
        }

    }
}