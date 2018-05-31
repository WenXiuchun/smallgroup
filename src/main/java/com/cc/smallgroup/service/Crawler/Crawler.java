package com.cc.smallgroup.service.Crawler;
import com.cc.smallgroup.repository.UserRespository;
import com.cc.smallgroup.repository.GameRespository;
import com.cc.smallgroup.repository.ProbabilityRespository;
import com.cc.smallgroup.service.Algorithm;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.maya.football.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class Crawler {
    private static Logger logger = LoggerFactory.getLogger(Crawler.class);
    @Autowired
    private GameRespository gameRespository;
    @Autowired
    private ProbabilityRespository probabilityRespository;
    @Autowired
    private UserRespository attackRespository;
    @Autowired
    private Algorithm algorithm;

    @Scheduled(cron = "0 0/1 * * * ?") // 每10分钟执行一次
    private void captureBFData(){
        try {
            String sBfdataUrl = "http://live.titan007.com/vbsxml/bfdata.js?r=007" + new Date().getTime();
            Document bfdataDoc = Jsoup.connect(sBfdataUrl)
                    .header("Accept", "*/*")
                    .header("Accept-Encoding", "gzip, deflate")
                    .header("Connection", "keep-alive")
                    .header("Cookie", "win007BfCookie=null")
                    .header("Host", "live.titan007.com")
                    .header("Referer", "http://live.titan007.com/")
                    .header("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1")
                    .header("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8")
                    .ignoreContentType(true).get();
            String html = bfdataDoc.html();

            //String result = getUTF8BytesFromGBKString(html);

            String keyFirstMatcheTime = "firstschematchtime=\"";
            int indexofFirstMacheTime = html.indexOf(keyFirstMatcheTime);
            String firstMatcheTime = "";
            if(indexofFirstMacheTime >= 0) {
                firstMatcheTime = html.substring(indexofFirstMacheTime + keyFirstMatcheTime.length(),
                        html.indexOf("\";", indexofFirstMacheTime));
            }
            String keyArray = "var A=Array(";
            int aLength = 0;
            int indexofArray = html.indexOf(keyArray);
            if(indexofArray >= 0) {
                String aArrayLength = html.substring(indexofArray + keyArray.length(),
                        html.indexOf(")", indexofArray));
                aLength = Integer.parseInt(aArrayLength);
            }
            for(int i = 1; i < aLength; i++){
                String head = "A[" + i + "]=\"";
                String ALine = html.substring(html.indexOf(head) + head.length(),
                        html.indexOf("\".split('^')", html.indexOf(head)));
                parseArrayA(ALine, firstMatcheTime);
            }
            System.out.print("hello");

        } catch(Exception e){
            e.printStackTrace();
        }

    }

    //有损转换
    public String getUTF8BytesFromGBKString(String gbkStr) throws UnsupportedEncodingException {
        int n = gbkStr.length();
        byte[] utfBytes = new byte[3 * n];
        int k = 0;
        for (int i = 0; i < n; i++) {
            int m = gbkStr.charAt(i);
            if (m < 128 && m >= 0) {
                utfBytes[k++] = (byte) m;
                continue;
            }
            utfBytes[k++] = (byte) (0xe0 | (m >> 12));
            utfBytes[k++] = (byte) (0x80 | ((m >> 6) & 0x3f));
            utfBytes[k++] = (byte) (0x80 | (m & 0x3f));
        }
        if (k < utfBytes.length) {
            byte[] tmp = new byte[k];
            System.arraycopy(utfBytes, 0, tmp, 0, k);
            utfBytes = tmp;


        }
        return new String(utfBytes,"UTF-8");
    }

    private void parseArrayA(String ALine, String firstMatcheTime) {

        String[] arrayA = ALine.split("\\^");
        if (arrayA.length != 53 || !arrayA[51].equals("1")) {
            return;
        }
        //logger.info(ALine);
        Game game = new Game();
        String gameId = arrayA[0] + arrayA[43] + arrayA[36];
        game.setMatchId(arrayA[0]);
        game.setMatchDate(arrayA[36]);
        game.setColor(arrayA[1]);
        game.setHomeTeam(arrayA[5]);
        game.setGuestTeam(arrayA[8]);
        game.setHomeTeamEng(arrayA[7]);
        game.setGuestTeamEng(arrayA[10]);
        game.setHomeTeamId(arrayA[37]);
        game.setGuestTeamId(arrayA[38]);
        try {
            game.setHomeHalfCorners(arrayA[15].equals("") ? 0 : Integer.parseInt(arrayA[15]));
            game.setGuestHalfCorners(arrayA[16].equals("") ? 0 : Integer.parseInt(arrayA[16]));
            game.setMatchState(arrayA[13].equals("") ? 0 : Integer.parseInt(arrayA[13]));
            game.setHomeScore(arrayA[14].equals("") ? 0 : Integer.parseInt(arrayA[14]));
            game.setGuestScore(arrayA[15].equals("") ? 0 : Integer.parseInt(arrayA[15]));
            game.setHomeHalfScore(arrayA[16].equals("") ? 0 : Integer.parseInt(arrayA[16]));
            game.setGuesteHalfScore(arrayA[17].equals("") ? 0 : Integer.parseInt(arrayA[17]));
            game.setHomeRedCards(arrayA[18].equals("") ? 0 : Integer.parseInt(arrayA[18]));
            game.setGuestRedCards(arrayA[19].equals("") ? 0 : Integer.parseInt(arrayA[19]));
            game.setHomeYellowCards(arrayA[20].equals("") ? 0 : Integer.parseInt(arrayA[20]));
            game.setGuestYellowCards(arrayA[21].equals("") ? 0 : Integer.parseInt(arrayA[21]));
            game.setHomeCorners(arrayA[48].equals("") ? 0 : Integer.parseInt(arrayA[48]));
            game.setGuestCorners(arrayA[49].equals("") ? 0 : Integer.parseInt(arrayA[49]));
            game.setLeague(arrayA[2]);
            game.setLeagueEng(arrayA[4]);
            game.setOpenTime(arrayA[11]);
            game.setStartsTime(arrayA[12]);
            game.setYear(arrayA[43]);
            game.setId(gameId);
            game.setHasProAttack(arrayA[51]);

            gameRespository.save(game);


            if(arrayA[13].equals("1") || arrayA[13].equals("3")){
                captureLiveAnalysisData(arrayA[0], firstMatcheTime, gameId);
                algorithm.setGame(game);
                algorithm.pushMessage();
            }



        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void captureLiveAnalysisData(String matchId, String firstMatchTime, String gameId){

        String liveDataurl = "http://live.titan007.com/flashdata/get?id=" + matchId + "&t=" + new Date().getTime();
        String cookie = "win007BfCookie=null; bfWin007FirstMatchTime=" + firstMatchTime;
        try {
            Document bfdataDoc = Jsoup.connect(liveDataurl)
                    .header("Accept", "*/*")
                    .header("Accept-Encoding", "gzip, deflate")
                    .header("Connection", "keep-alive")
                    .header("Cookie", cookie)
                    .header("Host", "live.titan007.com")
                    .header("Referer", "http://live.titan007.com/")
                    .header("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1")
                    .header("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8")
                    .ignoreContentType(true)
                    .get();

            Charset charset = bfdataDoc.charset();
            String html = bfdataDoc.html();
            String keyBody = "<body>\n ";
            String liveData = html.substring(html.indexOf(keyBody) + keyBody.length(), html.indexOf("\n </body>"));
            String[] arrayLive = liveData.split("\\^");

            int beginP = arrayLive[11].indexOf("!");
            if (beginP >= 0){
                String firstP = arrayLive[11].substring(beginP+1);
                parseProbability(firstP, "1-15", gameId);
            } else {
                return;
            }
            parseProbability(arrayLive[12], "16-30", gameId);
            parseProbability(arrayLive[13], "31-45", gameId);
            parseProbability(arrayLive[14], "46-60", gameId);
            parseProbability(arrayLive[15], "61-75", gameId);
            int endP = arrayLive[16].indexOf("!");
            if (endP >= 0) {
                String lastP = arrayLive[16].substring(0, endP);
                parseProbability(lastP, "76-90", gameId);
            } else {
                parseProbability(arrayLive[16], "76-90", gameId);
            }

            for(int i = 22; i < arrayLive.length; i++){
                parseAttack(arrayLive[i], gameId);
            }
            //arrayLive
            System.out.println(html);

        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

    private void parseProbability(String proString, String time, String gameId) {
        String[] arrayP = proString.split("\\,");
        if (arrayP.length != 4){
            return;
        }
        List<Probability> probabilities = probabilityRespository.findByGameIdAndTimePeriodAndHomeTeamPAndHomeTeamLostPAndGuestTeamPAndGuestTeamLostP(
                gameId, time, arrayP[0], arrayP[1], arrayP[2], arrayP[3]);
        if (probabilities.size() > 0){
            return;
        }
        Probability pro = new Probability();
        pro.setGameId(gameId);
        pro.setTimePeriod(time);
        pro.setHomeTeamP(arrayP[0]);
        pro.setHomeTeamLostP(arrayP[1]);
        pro.setGuestTeamP(arrayP[2]);
        pro.setGuestTeamLostP(arrayP[3]);
        probabilityRespository.save(pro);
    }

    private void parseAttack(String attackString, String gameId){
        String[] arrayAttack = attackString.split("\\,");
        if (arrayAttack.length != 5){
            return;
        }
        List<Attack> attacks = attackRespository.findByGameIdAndFirstAndAttackTypeAndTeamIdAndThirdAndAttackTime(gameId,
                arrayAttack[0],
                arrayAttack[1],
                arrayAttack[2], arrayAttack[3], arrayAttack[4]);
        if (attacks.size() > 0){
            return;
        }

        Attack attack = new Attack();
        attack.setFirst(arrayAttack[0]);
        attack.setAttackType(arrayAttack[1]);
        attack.setTeamId(arrayAttack[2]);
        attack.setThird(arrayAttack[3]);
        attack.setAttackTime(arrayAttack[4]);
        attack.setGameId(gameId);
        attackRespository.save(attack);

    }

}
