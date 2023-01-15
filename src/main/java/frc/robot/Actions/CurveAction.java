package frc.robot.Actions;

public class CurveAction implements AutoAction {
    public boolean Act() {
        if(swerveDrive.followTrajectory(180)){
            return true;
        } else {
            return false;
        }
    } 
}
