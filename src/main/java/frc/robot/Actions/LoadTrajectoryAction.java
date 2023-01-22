package frc.robot.Actions;

import frc.robot.Auto;

public class LoadTrajectoryAction extends AutoAction {
    String trajectory;
    public LoadTrajectoryAction(String trajectory) {
        this.trajectory = trajectory;
    }
    public boolean Act() {
        Auto.Utils.swerveDrive.loadTrajectory(trajectory);
        return true;
    }
}
