package frc.robot;

import com.ctre.phoenix.sensors.CANCoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;

public class Arm {
    static CANSparkMax armMotor;
    static PIDController pid;
    static CANCoder encoder;

    public Arm() {
        armMotor = new CANSparkMax(Constants.ArmConstants.kArmMotorId, MotorType.kBrushless);
        pid = new PIDController(Constants.ArmConstants.kPArm, 
                                Constants.ArmConstants.kIArm, 
                                Constants.ArmConstants.kDArm);   
        encoder = new CANCoder(Constants.ArmConstants.kArmEncoderId); 
    }

    public void moveArm(double joyPos) {
        if (encoder.getPosition() >= Constants.ArmConstants.kArmUpperLimit ||
            encoder.getPosition() <= Constants.ArmConstants.kArmLowerLimit) {
            armMotor.set(joyPos);
        } else {
            armMotor.stopMotor();
        }
    }

    public void setArm(int level) {
        double armSetPoint = 0;
        switch(level) {
            case 0:
                armSetPoint = Constants.ArmConstants.kRetracted;
                break;
            case 1:
                armSetPoint = Constants.ArmConstants.kBottomLevel;
                break;
            case 2:
                armSetPoint = Constants.ArmConstants.kMiddleLevel;
                break;
            case 3:
                armSetPoint = Constants.ArmConstants.kTopLevel;
                break;
        }
        double output = MathUtil.clamp(pid.calculate(encoder.getPosition(), armSetPoint), -1, 1);
        armMotor.set(output);
 
    }
    }


