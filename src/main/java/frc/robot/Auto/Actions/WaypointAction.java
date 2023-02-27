package frc.robot.Auto.Actions;

import edu.wpi.first.math.geometry.Translation2d;
import frc.robot.AutoAlign;
import frc.robot.Drivebase;

public class WaypointAction extends AutoAction {
    public double x;
    public double y;
    public double heading;
    public boolean Act() {
        return AutoAlign.alignOdometry(new Translation2d(x - Drivebase.getPose().getX(), y - Drivebase.getPose().getY()), heading);
    }
    public void Finish() {

    }

    public WaypointAction() {

    }
    public WaypointAction(double targetX, double targetY, double targetHeading) {
        x = targetX;
        y = targetY;
        heading = targetHeading;
    }
}