package frc.robot;

import com.ctre.phoenix.sensors.CANCoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;

public class Arm {
    CANSparkMax armMotor;
    PIDController pid;
    CANCoder encoder;
    public Arm() {
        armMotor = new CANSparkMax(0, MotorType.kBrushless);
        pid = new PIDController(0, 0, 0);   
        encoder = new CANCoder(0); 
        };
    public void moveArm(double joyPos) {
        if (encoder.getPosition() <= 5000 || encoder.getPosition() >= 0) {
            armMotor.set(joyPos);
        } else {
            armMotor.stopMotor();
        }
    }

    public void setArm(int level) {
        double armSetPoint = 0;
        switch(level) {
            case 1:
                armSetPoint = 1000;
                break;
            case 2:
                armSetPoint = 2000;
                break;
            case 3:
                armSetPoint = 3000;
                break;
            case 4:
                armSetPoint = 4000;
                break;
        }
        double output = MathUtil.clamp(pid.calculate(encoder.getPosition(), armSetPoint), -1, 1);
        armMotor.set(output);
 
    }
    }


