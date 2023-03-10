package frc.robot.Auto.Actions;

import java.util.ArrayList;

import edu.wpi.first.math.geometry.Translation2d;
import frc.robot.AutoAlign;
import frc.robot.Drivebase;

public class WaypointAction extends AutoAction {
    public double x;
    public double y;
    public double heading;
    public double speed;
    public boolean Act() {
        return AutoAlign.alignOdometry(new Translation2d(x * speed, y * speed), heading);
    }
    public void Finish() {
        // AutoAlign.alignOdometry(Drivebase.getPose().getTranslation(), heading);
    }

    public WaypointAction() {

    }
    public WaypointAction(double targetX, double targetY, double targetHeading, double _startTime, double _endTime, EarlyEndMode earlyEndMode, LateEndMode lateEndMode, AutoAction[] _endActions) {
        x = targetX;
        y = targetY;
        heading = targetHeading;
        startTime = _startTime;
        endTime = _endTime;
        earlyMode = earlyEndMode;
        lateMode = lateEndMode;
        endActions = new ArrayList<AutoAction>();
        for (AutoAction action : _endActions) {
            endActions.add(action);
        }
    }
    public WaypointAction(Translation2d position, double targetHeading, double _startTime, double _endTime, EarlyEndMode earlyEndMode, LateEndMode lateEndMode, AutoAction[] _endActions) {
        x = position.getX();
        y = position.getY();
        heading = targetHeading;
        startTime = _startTime;
        endTime = _endTime;
        earlyMode = earlyEndMode;
        lateMode = lateEndMode;
        endActions = new ArrayList<AutoAction>();
        for (AutoAction action : _endActions) {
            endActions.add(action);
        }
    }
}