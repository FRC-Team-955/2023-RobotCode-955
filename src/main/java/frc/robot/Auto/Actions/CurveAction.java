package frc.robot.Auto.Actions;

import frc.robot.Auto.Auto;

public class CurveAction extends AutoAction {
    public boolean Act() {
        if(Auto.Utils.swerveDrive.followTrajectory(180)){
            return true;
        } else {
            return false;
        }
    } 
}
