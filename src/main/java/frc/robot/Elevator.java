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
        elevatorMotor = new CANSparkMax(Constants.ElevatorConstants.kElevatorMotorId, MotorType.kBrushless);
        pid = new PIDController(Constants.ElevatorConstants.kPElevator,
                                Constants.ElevatorConstants.kIElevator,
                                Constants.ElevatorConstants.kDElevator);
        feedforward = new ElevatorFeedforward(Constants.ElevatorConstants.kSElevator,
                                            Constants.ElevatorConstants.kGElevator,
                                            Constants.ElevatorConstants.kVElevator);
    }

    public void moveElevator(double joyPos) {
        if(Joystick.isOverrideEnabled() || ((elevatorMotor.getEncoder().getPosition() <= Constants.ElevatorConstants.kElevatorUpperLimit || joyPos < 0)
            && (elevatorMotor.getEncoder().getPosition() >= Constants.ElevatorConstants.kElevatorLowerLimit || joyPos > 0))) { // if elevator hit the top or bottom
            elevatorMotor.set(joyPos);
        } else {
            elevatorMotor.set(0);
        }
    }

    public void setElevator(int level) { // level = desired elevator level
        if(!Joystick.isOverrideEnabled()) {
            double elevatorSetpoint = 0;
            switch(level) {
                case 0:
                    elevatorSetpoint = Constants.ElevatorConstants.kRetracted;
                    break;
                case 1:
                    elevatorSetpoint = Constants.ElevatorConstants.kBottomLevel;
                    break;
                case 2:
                    elevatorSetpoint = Constants.ElevatorConstants.kMediumLevel;
                    break;
                case 3:
                    elevatorSetpoint = Constants.ElevatorConstants.kTopLevel;
                    break;
            }

            double amount = MathUtil.clamp(pid.calculate(elevatorMotor.getEncoder().getPosition(), elevatorSetpoint) +
                                            feedforward.calculate(elevatorMotor.getEncoder().getVelocity()), -12, 12);
            
            elevatorMotor.setVoltage(amount);
        }
        else {
            elevatorMotor.set(0);
        }
    }
}
