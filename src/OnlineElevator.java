package ex0.algo;

import ex0.Building;
import ex0.CallForElevator;
import ex0.Elevator;

public class OnlineElevator implements ElevatorAlgo {
    public static final int UP = 1, DOWN = -1;
    private int _direction;
    private Building _building;
    private SmartE[] ElevSys;
    private Elevator[] ElevStore;
    private int[] CuRrDest;

    public OnlineElevator(Building b) { //constructure
        _building = b;
        _direction = UP;
        ElevSys = new SmartE[b.numberOfElevetors()];
        ElevStore = new Elevator[b.numberOfElevetors()];
        CuRrDest = new int[b.numberOfElevetors()];
        for (int i = 0; i < b.numberOfElevetors(); i++) {
            ElevSys[i] = new SmartE(b);
            ElevStore[i] = b.getElevetor(i);
            CuRrDest[i] = b.maxFloor();

        }
    }

    @Override
    public Building getBuilding() {
        return this._building;
    }

    @Override
    public String algoName() {
        return "OnlineElevator";
    }

    @Override
    public int allocateAnElevator(CallForElevator c) {
        int E1 = 0;
        double MinTime = Double.MAX_VALUE;
        double time;
        for (int i = 1; i < _building.numberOfElevetors(); i++) {
            if ((_building.getElevetor(i).getState() == Elevator.LEVEL) || (ElevSys[i].ArrayDown.size() == 0)) { //if the elevator is in rest/level, or no calls down
                time = TimeLevel(c, i);
                if (time < MinTime) { // time update
                    MinTime = time;
                    E1 = i; // update wich elevator is coming
                }
            }

            if (c.getSrc() < c.getDest()) {// if the type of the call is up

                if (((_building.getElevetor(i).getState() != Elevator.DOWN) && (c.getSrc() >= _building.getElevetor(i).getPos()))) { //if the elevator is not going down and the call is higher than the elev pos'
                    if ((basic_cost_all(c, i)) < MinTime) { //better timing
                        MinTime = basic_cost_all(c, i);
                        E1 = i;
                    }
                } else if ((((_building.getElevetor(i).getState() == Elevator.DOWN) && (c.getSrc() <= _building.getElevetor(i).getPos())))) { // if the elev' is going down and the pos is higher than the call
                    continue;

                }
            } else if (c.getSrc() > c.getDest()) {//if the type of the call is down
                if (((_building.getElevetor(i).getState() != Elevator.UP) && (c.getSrc() <= _building.getElevetor(i).getPos()))) { //if the elevator is not going up and the call is lower than the elev pos'
                    if ((basic_cost_all(c, i)) < MinTime) { //better timing
                        MinTime = basic_cost_all(c, i);
                        E1 = i;
                    }
                } else if ((((_building.getElevetor(i).getState() == Elevator.UP) && (c.getSrc() >= _building.getElevetor(i).getPos())))) { // if the elev' is going up and the pos is lower than the call
                    continue;


                }
            }
        }
        if (c.getSrc() < c.getDest()) // if call is up than go inside arrayup [E1]
            ElevSys[E1].ArrayUp.add(c);
        else
            ElevSys[E1].ArrayDown.add(c);// if call is down than go inside arraydown [E1]
        return E1;
    }
    // this function checks how long it would take the elevator to go from it pos' to dest' iclude  the number of stops and the speed of the elevator
    private double basic_cost_all(CallForElevator c, int elev) {
        Elevator thisElev = this._building.getElevetor(elev);
        int pos = thisElev.getPos();
        double speed = thisElev.getSpeed();
        double pos2src = ((Math.abs(c.getSrc() - pos)) / speed);
        double src2des = ((Math.abs(c.getSrc() - c.getDest())) / speed);
        double open = (thisElev.getTimeForOpen() + thisElev.getStopTime());
        double close = (thisElev.getTimeForClose() + thisElev.getStartTime());
        double cost1 = pos2src + src2des;
        double cost2 = 0;
        if (c.getSrc() < c.getDest()) {
            cost2 = (open + close) * MuchStops(elev, Elevator.UP);
        } else if (c.getSrc() > c.getDest()) {
            cost2 = (open + close) * MuchStops(elev, Elevator.DOWN);
        }
        double costAll = cost1 + cost2;
        return costAll;
    }

    // this function checks how long it will take the elevator to come when is level
    private double TimeLevel(CallForElevator el, int elev) {
        Elevator thisElev = this._building.getElevetor(elev);
        double speed = thisElev.getSpeed();
        int pos = thisElev.getPos();
        double dis = (Math.abs(pos - elev))/speed;
        double ans = 0;
        if (pos == elev) {
            ans = thisElev.getTimeForOpen();
        } else {
            double open = (thisElev.getTimeForOpen() + thisElev.getStopTime());
            double close = (thisElev.getTimeForClose() + thisElev.getStartTime());
            ans = open + close + dis;
        }
        return ans;
    }
    // this function checks how many stop does the elevator has. this is help function for calculating the cost. for every call we have on the array- stop++
    private int MuchStops ( int elev, int dir){
        int stop = 0;
        if (elev < _building.numberOfElevetors()) {
            if (dir == DOWN) {
                for (int i = 0; i < ElevSys[elev].ArrayDown.size(); i++) {
                    if (ElevSys[elev].ArrayDown.get(i).getSrc() >= elev) {
                        stop++;
                    }
                    if (ElevSys[elev].ArrayDown.get(i).getDest() >= elev) {
                        stop++;
                    }

                }
            } else if (dir == UP) {
                for (int i = 0; i < ElevSys[elev].ArrayUp.size(); i++) {
                    if (ElevSys[elev].ArrayUp.get(i).getSrc() >= elev) {
                        stop++;
                    }
                    if (ElevSys[elev].ArrayUp.get(i).getDest() >= elev) {
                        stop++;
                    }

                }
            }

        }

        return stop;
    }


    @Override
    public void cmdElevator(int elev) {
        if (_building.getElevetor(elev).getState() == 0) {
            if (ElevSys[elev].ArrayUp.size() > 0) { // if call is up than go inside arrayup [E1]
                if (CuRrDest[elev] == _building.maxFloor() || CuRrDest[elev] < _building.getElevetor(elev).getPos()) {
                    ElevStore[elev].goTo(ElevSys[elev].ArrayUp.get(0).getSrc()); // take an order from the arrayup
                    CuRrDest[elev] = ElevSys[elev].ArrayUp.get(0).getSrc();
                }

                if (CuRrDest[elev] == _building.getElevetor(elev).getPos()) { //  if the call ended
                    ElevSys[elev].ArrayUp.remove(0); // remove call from arrayup
                    CuRrDest[elev] = _building.maxFloor(); // update cuurent dest
                }
            }
            if (ElevSys[elev].ArrayDown.size() > 0) { // if call is up than go inside arraydown [E1]
                if (CuRrDest[elev] == _building.minFloor() || CuRrDest[elev] > _building.getElevetor(elev).getPos()) {
                    ElevStore[elev].goTo(ElevSys[elev].ArrayDown.get(0).getSrc()); // take an order from the arrayup
                    CuRrDest[elev] = ElevSys[elev].ArrayDown.get(0).getSrc();
                }

                if (CuRrDest[elev] == _building.getElevetor(elev).getPos()) { //  if the call ended
                    ElevSys[elev].ArrayDown.remove(0); // remove call from arraydown
                    CuRrDest[elev] = getBuilding().minFloor(); // update cuurent dest
                }
            }
        }
    }
}