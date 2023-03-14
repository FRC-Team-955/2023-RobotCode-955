package frc.robot.Auto.Actions.Deprecated;

import frc.robot.Auto.Auto;
import frc.robot.Auto.Actions.AutoAction;

public class LoadTrajectoryAction extends AutoAction {
    String trajectory;
    public LoadTrajectoryAction(String trajectory) {
        this.trajectory = trajectory;
    }
    public boolean Act() {
        Auto.Utils.swerveDrive.loadTrajectory(trajectory);
        return true;
    }
    
    public void Finish() {
        
    }
}
