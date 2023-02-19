package frc.robot.Subsystems;

import frc.robot.Constants;
import frc.robot.IO;

import java.security.spec.ECField;

// import com.ctre.phoenix.sensors.CANCoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.ElevatorFeedforward;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.util.datalog.DataLog;
import edu.wpi.first.util.datalog.DoubleLogEntry;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.DutyCycleEncoder;

public class Elevator {
    static CANSparkMax motor;
    static PIDController pid;
    static PIDController pidTemp;
    static ElevatorFeedforward feedforward;
    static DoubleLogEntry motorlog;
    static DoubleLogEntry encoderlog;
    static RelativeEncoder encoder;
    static DutyCycleEncoder dutyCycleEncoder1;
    static DutyCycleEncoder dutyCycleEncoder2;
    
    static double setpoint = Constants.Elevator.retracted;

    public static void setup() {
        motor = new CANSparkMax(Constants.Elevator.motorID, MotorType.kBrushless);
        motor.setIdleMode(IdleMode.kBrake);
        dutyCycleEncoder1 = new DutyCycleEncoder(Constants.Elevator.coder1ID);
        dutyCycleEncoder2 = new DutyCycleEncoder(Constants.Elevator.coder2ID);
        pid = new PIDController(Constants.Elevator.kP,
                                Constants.Elevator.kI,
                                Constants.Elevator.kD);
        feedforward = new ElevatorFeedforward(Constants.Elevator.kS,
                                            Constants.Elevator.kG,
                                            Constants.Elevator.kV);
        pid.setTolerance(Constants.Elevator.tolerance);
        
                                    
        encoder = motor.getEncoder();
        encoder.setPosition(0);
        // encoder.setPosition(ElevatorPosition.calculate(
        //     dutyCycleEncoder1.getAbsolutePosition(),
        //     dutyCycleEncoder2.getAbsolutePosition()));

        DataLog log = DataLogManager.getLog();
        motorlog = new DoubleLogEntry(log, "/elevator/motor");
        encoderlog = new DoubleLogEntry(log, "/elevator/encoder");
    }

    public static void logData() {
        motorlog.append(motor.getOutputCurrent());
        encoderlog.append(encoder.getPosition());
    }

    public static void moveElevator(double joyPos) {
        if(!IO.isOverrideEnabled() && ((encoder.getPosition() <= Constants.Elevator.upperLimit - Constants.Elevator.kG || joyPos < 0)
            && (encoder.getPosition() >= Constants.Elevator.lowerLimit || joyPos > 0))) { // if elevator hit the top or bottom
            motor.set(joyPos + Constants.Elevator.kG);
        }
    }
    
    public static void moveElevatorOverride(double joyPos) {
        System.out.println("Elevator Encoder Position: " + encoder.getPosition());
        motor.set(joyPos);
    }
    public static boolean setElevatorTempUp(){
        if(!IO.isOverrideEnabled()) {
            double amount = MathUtil.clamp(pid.calculate(encoder.getPosition(), setpoint) +
                                            Constants.Elevator.kG, -12, 12);
            
            motor.setVoltage(amount);

            return pid.atSetpoint();
        }
        return false;
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
            case DoubleSubstation:
                setpoint= Constants.Elevator.doubleSubstationPosition;
        }
    }

    public static boolean setElevator(){
        if(!IO.isOverrideEnabled()) {
            double amount = MathUtil.clamp(pid.calculate(encoder.getPosition(), setpoint) +
                                            Constants.Elevator.kG, -12, 12);
            
            motor.setVoltage(amount);

            return pid.atSetpoint();
        }
        return false;
    }
}
