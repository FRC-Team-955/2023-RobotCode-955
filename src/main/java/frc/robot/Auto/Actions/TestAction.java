package frc.robot.Auto.Actions;

import java.util.ArrayList;

import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class TestAction extends AutoAction {
    TalonSRX motor;

    public boolean Act() {
        motor.set(TalonSRXControlMode.PercentOutput, 0.5);
        return false;
    }

    public TestAction(double _startTime, double _endTime, EarlyEndMode earlyEndMode, LateEndMode lateEndMode, int motorId, AutoAction[] _endActions) {
        startTime = _startTime;
        endTime = _endTime;
        earlyMode = earlyEndMode;
        lateMode = lateEndMode;
        endActions = new ArrayList<AutoAction>();
        for (AutoAction action : _endActions) {
            endActions.add(action);
        }

        motor = new TalonSRX(motorId);
    }
}
