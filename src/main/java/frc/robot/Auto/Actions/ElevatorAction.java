package frc.robot.Auto.Actions;

import frc.robot.GamepieceManager;
import frc.robot.IO;

public class ElevatorAction extends AutoAction{
    IO.GridRowPosition elevatorPos;
    IO.GridArmPosition armPos;
    public ElevatorAction(IO.GridRowPosition elevatorPos, IO.GridArmPosition armPos) {
        this.elevatorPos = elevatorPos;
        this.armPos = armPos;
    }

    // 0 = retracted, 1 = lowest, 2 = mid, 3 = high
    public boolean Act() {
        GamepieceManager.setExtention(elevatorPos, armPos);
        return true;
    }
}
