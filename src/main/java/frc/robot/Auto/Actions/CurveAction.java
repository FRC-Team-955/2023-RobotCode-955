package frc.robot.Auto.Actions;

import frc.robot.Auto.Auto;

public class CurveAction extends AutoAction {
    double holonomicRotation;

    public CurveAction(double holonomicRotation) {
        this.holonomicRotation = holonomicRotation;
    }

    public boolean Act() {
        return Auto.Utils.swerveDrive.followTrajectory(holonomicRotation);
    } 
    
    public void Finish() {
        
    }
}
