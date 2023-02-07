package frc.robot.Actions;

public class CurveAction implements AutoAction {
    double holonomicRotation;

    public CurveAction(double holonomicRotation) {
        this.holonomicRotation = holonomicRotation;
    }

    public boolean Act() {
        return swerveDrive.followTrajectory(holonomicRotation);
    } 
}
