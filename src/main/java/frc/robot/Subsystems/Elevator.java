package frc.robot.Subsystems;

// import com.ctre.phoenix.sensors.CANCoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.ElevatorFeedforward;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.util.datalog.DataLog;
import edu.wpi.first.util.datalog.DoubleLogEntry;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.IO;

public class Elevator {
    static CANSparkMax motor;
    static PIDController pid;
    static PIDController pidTemp;
    static ElevatorFeedforward feedforward;
    static DoubleLogEntry motorlog;
    static DoubleLogEntry encoderlog;
    public static RelativeEncoder encoder;
    static DutyCycleEncoder dutyCycleEncoder1;
    static DutyCycleEncoder dutyCycleEncoder2;
    
    static double setpoint = Constants.Elevator.retracted;

    public static void setup() {
        motor = new CANSparkMax(Constants.Elevator.motorID, MotorType.kBrushless);
        motor.setIdleMode(IdleMode.kBrake);
        motor.setSmartCurrentLimit(60);

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
    
    public static void setOffset(){
        encoder.setPosition(1.08761993);
    }

    public static void disableElevator(){
        motor.setVoltage(0);
    }

    public static boolean elevatorRetract = true; 
    public static void setElevator(IO.GridRowPosition level) { // level = desired elevator level
        switch(level) {
            case UpRetract:
                setpoint = Constants.Elevator.upRetract;
                elevatorRetract = true;
                break;
            case Retract:
                setpoint = Constants.Elevator.retracted;
                elevatorRetract = true; 
                break;
            case Low:
                setpoint = Constants.Elevator.low;
                elevatorRetract = false;
                break;
            case ConeCloseMid:
                setpoint = Constants.Elevator.coneCloseMid;
                elevatorRetract = false;
                break;
            case MidConeFar:
                setpoint = Constants.Elevator.coneFarMid;
                elevatorRetract = false;
                break;
            case ConeCloseHigh:
                setpoint = Constants.Elevator.coneCloseHigh;
                elevatorRetract = false;
                break;
            case HighFarCone:
                setpoint = Constants.Elevator.highFarCone;
                elevatorRetract = false;
                break;
            case HighFarConeAuto:
                setpoint = Constants.Elevator.highFarConeAuto;
                elevatorRetract = false;
                break;
            case MidCube:
                setpoint = Constants.Elevator.midCube;
                elevatorRetract = false;
                break;
            case HighCube:
                setpoint = Constants.Elevator.highCube;
                elevatorRetract = false;
                break;
            case SingleSubstation:
                setpoint = Constants.Elevator.singleSubstation;
                elevatorRetract = true;
                break;
            case DoubleSubstation:
                setpoint = Constants.Elevator.doubleSubstation;
                elevatorRetract = true;
                break;
            case CubeIntake:
                setpoint = Constants.Elevator.cubeIntake;
                elevatorRetract = true;
                break;
            case CubeRetract:
                setpoint  = Constants.Elevator.cubeRetract;
                elevatorRetract = true;
                break;
            case ConeIntake:
                setpoint = Constants.Elevator.coneIntake;
                break;
        }
    }
    public static boolean setElevator(){
        if(!IO.isOverrideEnabled()) {
            double amount = MathUtil.clamp(pid.calculate(encoder.getPosition(), setpoint) +
                                            Constants.Elevator.kG, -12, 12);
            // System.out.println(setpoint);
            // System.out.println(encoder.getPosition());

            motor.setVoltage(amount);
            return Math.abs(encoder.getPosition() - setpoint) < Constants.Elevator.tolerance;
            // return true;
            // return pid.atSetpoint();
        }
        return false;
    }
    public static void displayInformation(){
        // SmartDashboard.putBoolean("Set Elevator", Math.abs(encoder.getPosition() - setpoint) < Constants.Elevator.tolerance);
        SmartDashboard.putNumber("elevatorpostion", encoder.getPosition());
    }
}
