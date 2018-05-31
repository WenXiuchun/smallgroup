package com.cc.smallgroup.web;

import com.cc.smallgroup.repository.UserRespository;
import com.cc.smallgroup.repository.GameRespository;
import com.cc.smallgroup.repository.ProbabilityRespository;
import com.cc.smallgroup.repository.PushHistoryRespository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.ResponseEntity;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import javax.validation.Valid;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RestController
@RequestMapping("api")
@Api("足球比赛相关的api")
public class GameController
{
    private static Logger logger = LoggerFactory.getLogger(GameController.class);
    @Autowired
    private GameRespository gameRespository;
    @Autowired
    private ProbabilityRespository probabilityRespository;
    @Autowired
    private UserRespository attackRespository;

    @Autowired
    private PushHistoryRespository pushHistoryRespository;

    @ApiOperation(value = "获取当日比赛场次，date 2-18")
    @GetMapping(value = "/match/{year}/{date}")
    public List<Game> getGame(@PathVariable String year, @PathVariable String date) {

        return gameRespository.findGamesByYearAndDate(year, date);
    }

    @ApiOperation(value = "获取丢球概率")
    @GetMapping(value = "/probability/{gameId}")
    public List<Probability> getProbability(@PathVariable String gameId) {
        return probabilityRespository.findByGameId(gameId);
    }

    @ApiOperation(value = "获取进攻数据")
    @GetMapping(value = "/attack/{gameId}")
    public List<Attack> getAttack(@PathVariable String gameId) {
        return attackRespository.findByGameId(gameId);
    }


    @ApiOperation(value = "下注的数据")
    @PostMapping("/bodata")
    public ResponseEntity<Map<String,Object>> bodata(@Valid @RequestBody Object data) {
        Map<String,Object> map = new HashMap<String,Object>();
        logger.info("BODATA ---- post bodata from external:" + JSONObject.fromObject(data).toString());
        return new ResponseEntity<Map<String,Object>>(map, HttpStatus.OK);
    }

    @ApiOperation(value = "推送历史, ex: date 3-08")
    @GetMapping(value = "/pushResult/{date}")
    public List<PushHistory> bodata(@PathVariable String date) {
        return pushHistoryRespository.findByMatchDate(date);
    }
}
