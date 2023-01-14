package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;

public class Elevator {
    CANSparkMax elevatorMotor; 
    PIDController pid;

    public Elevator() {
       elevatorMotor = new CANSparkMax(Constants.Elevator.kElevatorMotorId, MotorType.kBrushless);
       pid = new PIDController(Constants.Elevator.kP, 
                                Constants.Elevator.kI, 
                                Constants.Elevator.kP);
    }
    
    public void moveElevator(double joyPos) {
        if(elevatorMotor.getEncoder().getPosition() == Constants.Elevator.kBotLimit || 
            elevatorMotor.getEncoder().getPosition() == Constants.Elevator.kTopLimit) { // if elevator hit the top or bottom
            elevatorMotor.set(0);
        } else {
            elevatorMotor.set(joyPos);
        }
    }

    public void setElevator(int level) { // level = desired elevator level
        double elevatorSetpoint = 0;

        switch(level) {
            case 1:
                elevatorSetpoint = Constants.Elevator.kRetracted;
                break;
            case 2:
                elevatorSetpoint = Constants.Elevator.kBottomLevel;
                break;
            case 3:
                elevatorSetpoint = Constants.Elevator.kMediumLevel;
                break;
            case 4:
                elevatorSetpoint = Constants.Elevator.kTopLevel;
                break;
        }

        double amount = MathUtil.clamp(pid.calculate(elevatorMotor.getEncoder().getPosition(), elevatorSetpoint), -1, 1);
        elevatorMotor.set(amount);
    }
}
