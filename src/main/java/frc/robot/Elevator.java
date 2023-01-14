package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;

public class Elevator {
    CANSparkMax elevatorMotor; 
    PIDController pid;

    public Elevator() {
       elevatorMotor = new CANSparkMax(Constants.ElevatorConstants.kElevatorMotorId, MotorType.kBrushless);
       pid = new PIDController(Constants.ElevatorConstants.kPElevator, 
                                Constants.ElevatorConstants.kIElevator, 
                                Constants.ElevatorConstants.kDElevator);
    }
    
    public void moveElevator(double joyPos) {
        if(elevatorMotor.getEncoder().getPosition() >= Constants.ElevatorConstants.kElevatorUpperLimit || 
            elevatorMotor.getEncoder().getPosition() <= Constants.ElevatorConstants.kElevatorLowerLimit) { // if elevator hit the top or bottom
            elevatorMotor.set(0);
        } else {
            elevatorMotor.set(joyPos);
        }
    }

    public void setElevator(int level) { // level = desired elevator level
        double elevatorSetpoint = 0;

        switch(level) {
            case 1:
                elevatorSetpoint = Constants.ElevatorConstants.kRetracted;
                break;
            case 2:
                elevatorSetpoint = Constants.ElevatorConstants.kBottomLevel;
                break;
            case 3:
                elevatorSetpoint = Constants.ElevatorConstants.kMediumLevel;
                break;
            case 4:
                elevatorSetpoint = Constants.ElevatorConstants.kTopLevel;
                break;
        }

        double amount = MathUtil.clamp(pid.calculate(elevatorMotor.getEncoder().getPosition(), elevatorSetpoint), -1, 1);
        elevatorMotor.set(amount);
    }
}
