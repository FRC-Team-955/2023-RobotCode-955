package frc.robot.Auto.Actions;

import java.util.ArrayList;

import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class TestAction extends AutoAction {
    TalonSRX motor = new TalonSRX(21);

    public double speed;

    public boolean Act() {
        motor.set(TalonSRXControlMode.PercentOutput, speed);
        System.out.println("Test");
        return false;
    }
    
    @Override
    public void Finish() {
        motor.set(TalonSRXControlMode.PercentOutput, 0);
        System.out.println("Finished");
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

    public TestAction() {

    }
}
