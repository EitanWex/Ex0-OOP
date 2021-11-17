package ex0.algo;

import ex0.Building;
import ex0.simulator.Call_A;
import ex0.simulator.Simulator_A;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OnlineElevatorTest {

    @Test
    void allocateAnElevator() {
        Building B;
        OnlineElevator onlineElevator;
        Simulator_A.initData(6,null);
        B=Simulator_A.getBuilding();
        onlineElevator=new OnlineElevator(Simulator_A.getBuilding());
        Simulator_A.initAlgo(onlineElevator);
        Simulator_A.runSim();
        Simulator_A.report();


        Call_A c1=new Call_A(7,4,8);
        Call_A c2=new Call_A(-1,2,5);
        Call_A c3=new Call_A(2,2,2);
        Call_A c4=new Call_A(-6,-2,1);
        Call_A c5=new Call_A(0,2,9);

        B.getElevetor(0).goTo(5);
        B.getElevetor(1).goTo(19);
        B.getElevetor(2).goTo(-2);
        B.getElevetor(3).goTo(10);
        B.getElevetor(4).goTo(-4);
        B.getElevetor(5).goTo(0);
        B.getElevetor(6).goTo(-3);

        assertEquals(3,onlineElevator.allocateAnElevator(c1));
        assertEquals(3,onlineElevator.allocateAnElevator(c2));
        assertEquals(3,onlineElevator.allocateAnElevator(c3));
        assertEquals(7,onlineElevator.allocateAnElevator(c4));
        assertEquals(7,onlineElevator.allocateAnElevator(c5));
    }

    @org.junit.jupiter.api.Test
    void cmdElevator() {

    }
}