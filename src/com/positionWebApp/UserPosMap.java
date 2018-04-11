package com.positionWebApp;

import java.util.*;

public class UserPosMap {
    public static Map<String,List<Position>> tab;

    public UserPosMap(){
        this.tab=new HashMap<>();
    }

    public boolean userIsPresent(String user){
        return tab.containsKey(user);
    }

    public void addUser(String user){
        tab.put(user, new ArrayList<>());
    }

    public List<Position> getPositions(String user, int n_pos){
        List<Position> ret = new ArrayList<>();

        Iterator itr = tab.get(user).iterator();
        for(int i=n_pos; i>0 && itr.hasNext(); i--) {
            Position element = (Position) itr.next();
            ret.add(element);
        }
        return ret;
    }

    public void addPos(String user, Position p){
        tab.get(user).add(p);
    }



}
