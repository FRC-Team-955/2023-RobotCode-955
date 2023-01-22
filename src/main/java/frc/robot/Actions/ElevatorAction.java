package frc.robot.Actions;

import frc.robot.Auto;

public class ElevatorAction extends AutoAction{
    int location;
    public ElevatorAction(int location) {
        this.location = location;
    }

    // Options[0]: 0 = retracted, 1 = lowest, 2 = mid, 3 = high
    public boolean Act() {
        Auto.Utils.gamepieceController.setLocation(location);
        return Auto.Utils.gamepieceController.elevatorAtSetpoint();
    }
}
