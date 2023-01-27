package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.ElevatorFeedforward;
import edu.wpi.first.math.controller.PIDController;

public class Elevator {
    CANSparkMax elevatorMotor;
    PIDController pid;
    ElevatorFeedforward feedforward;

    public Elevator() {
        elevatorMotor = new CANSparkMax(Constants.Elevator.kElevatorMotorId, MotorType.kBrushless);
        pid = new PIDController(Constants.Elevator.kPElevator,
                                Constants.Elevator.kIElevator,
                                Constants.Elevator.kDElevator);
        feedforward = new ElevatorFeedforward(Constants.Elevator.kSElevator,
                                            Constants.Elevator.kGElevator,
                                            Constants.Elevator.kVElevator);
        pid.setTolerance(Constants.Elevator.kElevatorTolerance);
    }

    public void moveElevator(double joyPos) {
        if((elevatorMotor.getEncoder().getPosition() <= Constants.Elevator.kElevatorUpperLimit || joyPos < 0)
            && (elevatorMotor.getEncoder().getPosition() >= Constants.Elevator.kElevatorLowerLimit || joyPos > 0)) { // if elevator hit the top or bottom
            elevatorMotor.set(joyPos);
        } else {
            elevatorMotor.set(0);
        }
    }

    public boolean setElevator(int level) { // level = desired elevator level
            double elevatorSetpoint = Constants.Elevator.kRetracted;
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

            double amount = MathUtil.clamp(pid.calculate(elevatorMotor.getEncoder().getPosition(), elevatorSetpoint) +
                                            feedforward.calculate(elevatorMotor.getEncoder().getVelocity()), -12, 12);
            
            elevatorMotor.setVoltage(amount);

            return pid.atSetpoint();
        }
}