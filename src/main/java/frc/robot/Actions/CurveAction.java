package frc.robot.Actions;

public class CurveAction implements AutoAction {
    double holonomicRotation;

    public CurveAction(double holonomicRotation) {
        this.holonomicRotation = holonomicRotation;
    }

    public boolean Act() {
        if(swerveDrive.followTrajectory(holonomicRotation)){
            return true;
        } else {
            return false;
        }
    } 
}
