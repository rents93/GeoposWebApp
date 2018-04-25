package com.positionWebApp;

import java.util.*;

public class UserPosMap {

    private static Map<String,List<Position>> tab = new HashMap<>();

    private static UserPosMap UPMap = new UserPosMap();

    public static UserPosMap getUserPosMap() {
        return UPMap;
    }

    public boolean userIsPresent(String user){
        return tab.containsKey(user);
    }

    public void addUser(String user){
        tab.put(user, new ArrayList<>());
    }

    public Position getLastPos(String user) {
        if (!userIsPresent(user))
            return null;

        ListIterator<Position> itr = tab.get(user).listIterator();
//        scorre fino al fondo e poi ritorna l ultimo
        while (itr.hasNext())
            itr.next();
        return itr.previous();
    }

    public List<Position> getPositions(String user, int n_pos){
        List<Position> ret = new ArrayList<>();
        if (!userIsPresent(user)){
            return ret;
        }

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