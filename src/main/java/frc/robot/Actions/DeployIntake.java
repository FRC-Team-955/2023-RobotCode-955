package frc.robot.Actions;

import frc.robot.GamepieceManager;

public class DeployIntake implements AutoAction {
    int location;

    public DeployIntake(int location) {
        this.location = location;
    }

    public boolean Act() {
        return GamepieceManager.foldIntake(location);
    }
    
}
