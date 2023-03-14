package frc.robot.Auto.Actions;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.Auto.Auto;

public class WaitAction extends AutoAction {
    public double time;

    public boolean timestamp;

    Timer timer = new Timer();

    @Override
    public void Start() {
        if (!timestamp)
            timer.start();
    }

    public boolean Act() {
        if (timestamp)
            return (Auto.autoTimer.get() >= time);
        else
            return (timer.get() >= time);
    }

    public WaitAction() {

    }
    public WaitAction(double _startTime, double _endTime, EarlyEndMode earlyEndMode, LateEndMode lateEndMode, double waitTime, boolean isTimestamp, AutoAction[] _endActions) {
        startTime = _startTime;
        endTime = _endTime;
        earlyMode = earlyEndMode;
        lateMode = lateEndMode;
        time = waitTime;
        timestamp = isTimestamp;
        endActions = new ArrayList<AutoAction>();
        for (AutoAction action : _endActions) {
            endActions.add(action);
        }
    }
}
