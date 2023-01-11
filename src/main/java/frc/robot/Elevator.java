package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class Elevator {
    CANSparkMax motor; 

    public Elevator() {
       motor = new CANSparkMax(0, MotorType.kBrushless);
    }

    public void moveElevator(double speed) {
        motor.set(speed);
    }
}
