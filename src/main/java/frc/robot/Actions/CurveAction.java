package frc.robot.Actions;

import frc.robot.Auto;

public class CurveAction extends AutoAction {
    public boolean Act() {
        if(Auto.Utils.swerveDrive.followTrajectory(180)){
            return true;
        } else {
            return false;
        }
    } 
}
