package ex0.algo;

import ex0.Building;
import ex0.CallForElevator;

import java.util.ArrayList;
//this class is build from 2 arraylists- 1.save up calls 2.save down calls
//In OnlineElevator we defined a SmartE array where each index in the array represents an elevator for me and in each of the elevators I have the 2 ArrayList that are defined as lists of CallForElevator. One for up calls and the other for down calls.
public class SmartE {
    private Building _building;
    public ArrayList<CallForElevator> ArrayUp;
    public ArrayList<CallForElevator> ArrayDown;
//    public ArrayList<Integer> NextStop;
    public int _direction;
    public SmartE(Building b){
        _building=b;
        ArrayUp=new ArrayList<CallForElevator>();
        ArrayDown=new ArrayList<CallForElevator>();
        _direction=CallForElevator.UP;
    }
    public void Add (CallForElevator c1) // the function gets a call and add to the right place.
    {
        if(c1.getSrc()>c1.getDest())
            ArrayDown.add(c1);
        if(c1.getSrc()<c1.getSrc())
            ArrayUp.add(c1);
    }
    public void Remove(CallForElevator c1) // when the call changes to a down call it will remove the call from the array.
    {
            if(ArrayUp.contains(c1))
                if(c1.getState()==CallForElevator.DONE){
                    ArrayUp.remove(c1);
                }
            if(ArrayDown.contains(c1))
                if(c1.getState()==CallForElevator.DONE){
                    ArrayDown.remove(c1);
                }
    }
}
