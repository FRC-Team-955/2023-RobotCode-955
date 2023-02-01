package frc.robot.Actions;

import frc.robot.gamepieceManager.GamepieceManager;

public class ElevatorAction implements AutoAction{
    int location;
    public ElevatorAction(int location) {
        this.location = location;
    }

    // 0 = retracted, 1 = lowest, 2 = mid, 3 = high
    public boolean Act() {
        GamepieceManager.setLocation(location);
        return true;
    }
}
