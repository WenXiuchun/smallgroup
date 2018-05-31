package com.cc.smallgroup.model;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class PredictMessage {
    public String getSportName() {
        return sportName;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getGuest() {
        return guest;
    }

    public void setGuest(String guest) {
        this.guest = guest;
    }

    public Boolean getLive() {
        return isLive;
    }

    public void setLive(Boolean live) {
        isLive = live;
    }

    public String getBetTypeName() {
        return betTypeName;
    }

    public void setBetTypeName(String betTypeName) {
        this.betTypeName = betTypeName;
    }

    public String toJsonStr(){
        return JSONObject.fromObject(this).toString();
    }

    String sportName;
    String home;
    String guest;
    Boolean isLive;
    String betTypeName;

}
