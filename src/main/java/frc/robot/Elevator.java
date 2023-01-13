package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;

public class Elevator {
    CANSparkMax motor; 
    PIDController pid;

    public Elevator() {
       motor = new CANSparkMax(0, MotorType.kBrushless);
       pid = new PIDController(1, 0, 0);
    }
    
    public void moveElevator(double amount) {
        if(motor.getEncoder().getPosition() == 0) {
            motor.set(0);
        }
        else if(motor.getEncoder().getPosition() == 3500) { // the 3500 is random
            motor.set(0);
        }
        else {
            motor.set(amount);
        }
    }

    public void setElevator(int level) { // level = desired elevator level
        double elevatorSetpoint = 0;
        switch(level) {
            case 1:
                elevatorSetpoint = 0;
                break;
            case 2:
                elevatorSetpoint = 1000;
                break;
            case 3:
                elevatorSetpoint = 2000;
                break;
            case 4:
                elevatorSetpoint = 3000;
                break;
        }
        double amount = MathUtil.clamp(pid.calculate(motor.getEncoder().getPosition(), elevatorSetpoint), -1, 1);
        motor.set(amount);
    }
}
