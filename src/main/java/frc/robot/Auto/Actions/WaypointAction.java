package frc.robot.Auto.Actions;

import edu.wpi.first.math.geometry.Translation2d;
import frc.robot.AutoAlign;
import frc.robot.Drivebase;

public class WaypointAction extends AutoAction {
    public double x;
    public double y;
    public double heading;
    public double speed;
    public boolean Act() {
        return AutoAlign.alignOdometry(new Translation2d((x - Drivebase.getPose().getX()) * speed, (y - Drivebase.getPose().getY()) * speed), heading);
    }
    public void Finish() {
        AutoAlign.alignOdometry(Drivebase.getPose().getTranslation(), heading);
    }

    public WaypointAction() {

    }
    public WaypointAction(double targetX, double targetY, double targetHeading) {
        x = targetX;
        y = targetY;
        heading = targetHeading;
    }
}