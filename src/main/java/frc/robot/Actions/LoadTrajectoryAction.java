package frc.robot.Actions;

public class LoadTrajectoryAction implements AutoAction {
    String trajectory;
    public LoadTrajectoryAction(String trajectory) {
        this.trajectory = trajectory;
    }
    public boolean Act() {
        swerveDrive.loadTrajectory(trajectory);
        return true;
    }
}
