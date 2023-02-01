package frc.robot.gamepieceManager;

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
    static CANSparkMax elevatorMotor;
    static PIDController pid;
    static ElevatorFeedforward feedforward;
    static DoubleLogEntry motorlog;
    static DoubleLogEntry encoderlog;
    static RelativeEncoder encoder;
    static double elevatorSetpoint = Constants.Elevator.kRetracted;

    public static void setup() {
        elevatorMotor = new CANSparkMax(Constants.Elevator.kElevatorMotorId, MotorType.kBrushless);
        pid = new PIDController(Constants.Elevator.kPElevator,
                                Constants.Elevator.kIElevator,
                                Constants.Elevator.kDElevator);
        feedforward = new ElevatorFeedforward(Constants.Elevator.kSElevator,
                                            Constants.Elevator.kGElevator,
                                            Constants.Elevator.kVElevator);
        pid.setTolerance(Constants.Elevator.kElevatorTolerance);

        encoder = elevatorMotor.getEncoder();
        encoder.setPosition(ElevatorPosition.calculate(
            (new CANCoder(Constants.Elevator.kCoder1ID)).getPosition(),
            (new CANCoder(Constants.Elevator.kCoder2ID)).getPosition()
        ));

        DataLog log = DataLogManager.getLog();
        motorlog = new DoubleLogEntry(log, "/elevator/motor");
        encoderlog = new DoubleLogEntry(log, "/elevator/encoder");
    }

    public static void logData() {
        motorlog.append(elevatorMotor.getOutputCurrent());
        encoderlog.append(elevatorMotor.getEncoder().getPosition());
    }

    public static void moveElevator(double joyPos) {
        if(!IO.isOverrideEnabled() && ((elevatorMotor.getEncoder().getPosition() <= Constants.Elevator.kElevatorUpperLimit || joyPos < 0)
            && (elevatorMotor.getEncoder().getPosition() >= Constants.Elevator.kElevatorLowerLimit || joyPos > 0))) { // if elevator hit the top or bottom
            elevatorMotor.set(joyPos);
        }
    }
    
    public static void moveElevatorOverride(double joyPos) {
        elevatorMotor.set(joyPos);
    }

    public static void setElevatorPosition(int level) { // level = desired elevator level
        switch(level) {
            case 0:
                elevatorSetpoint = Constants.Elevator.kRetracted;
                break;
            case 1:
                elevatorSetpoint = Constants.Elevator.kBottomLevel;
                break;
            case 2:
                elevatorSetpoint = Constants.Elevator.kMediumLevel;
                break;
            case 3:
                elevatorSetpoint = Constants.Elevator.kTopLevel;
                break;
        }
    }

    public static boolean setElevator() {
        if(!IO.isOverrideEnabled()) {
            double amount = MathUtil.clamp(pid.calculate(encoder.getPosition(), elevatorSetpoint) +
                                            feedforward.calculate(encoder.getVelocity()), -12, 12);
            
            elevatorMotor.setVoltage(amount);

            return pid.atSetpoint();
        }
        return false;
    }
}
