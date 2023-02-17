package frc.robot.Subsystems;

import frc.robot.Constants;
import frc.robot.IO;

import com.ctre.phoenix.sensors.CANCoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.ElevatorFeedforward;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.util.datalog.DataLog;
import edu.wpi.first.util.datalog.DoubleLogEntry;
import edu.wpi.first.wpilibj.DataLogManager;

public class Elevator {
    static CANSparkMax motor;
    static PIDController pid;
    static ElevatorFeedforward feedforward;
    static DoubleLogEntry motorlog;
    static DoubleLogEntry encoderlog;
    static RelativeEncoder encoder;
    
    static double setpoint = Constants.Elevator.retracted;

    public static void setup() {
        motor = new CANSparkMax(Constants.Elevator.motorID, MotorType.kBrushless);
        pid = new PIDController(Constants.Elevator.kP,
                                Constants.Elevator.kI,
                                Constants.Elevator.kD);
        feedforward = new ElevatorFeedforward(Constants.Elevator.kS,
                                            Constants.Elevator.kG,
                                            Constants.Elevator.kV);
        pid.setTolerance(Constants.Elevator.tolerance);

        encoder = motor.getEncoder();
        encoder.setPosition(ElevatorPosition.calculate(
            (new CANCoder(Constants.Elevator.coder1ID, "electrical_issue")).getPosition(),
            (new CANCoder(Constants.Elevator.coder2ID, "electrical_issue")).getPosition()
        ));

        DataLog log = DataLogManager.getLog();
        motorlog = new DoubleLogEntry(log, "/elevator/motor");
        encoderlog = new DoubleLogEntry(log, "/elevator/encoder");
    }

    public static void logData() {
        motorlog.append(motor.getOutputCurrent());
        encoderlog.append(motor.getEncoder().getPosition());
    }

    public static void moveElevator(double joyPos) {
        if(!IO.isOverrideEnabled() && ((motor.getEncoder().getPosition() <= Constants.Elevator.upperLimit || joyPos < 0)
            && (motor.getEncoder().getPosition() >= Constants.Elevator.lowerLimit || joyPos > 0))) { // if elevator hit the top or bottom
            motor.set(joyPos);
        }
    }
    
    public static void moveElevatorOverride(double joyPos) {
        motor.set(joyPos);
    }

    public static void setElevator(IO.GridRowPosition level) { // level = desired elevator level
        switch(level) {
            case Retract:
                setpoint = Constants.Elevator.retracted;
                break;
            case Low:
                setpoint = Constants.Elevator.bottomLevel;
                break;
            case Mid:
                setpoint = Constants.Elevator.mediumLevel;
                break;
            case High:
                setpoint = Constants.Elevator.topLevel;
                break;
        }
    }

    public static boolean setElevator(){
        if(!IO.isOverrideEnabled()) {
            double amount = MathUtil.clamp(pid.calculate(encoder.getPosition(), setpoint) +
                                            feedforward.calculate(encoder.getVelocity()), -12, 12);
            
            motor.setVoltage(amount);

            return pid.atSetpoint();
        }
        return false;
    }
}
