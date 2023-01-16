package frc.robot;

import com.ctre.phoenix.sensors.CANCoder;
import com.ctre.phoenix.sensors.CANCoderConfiguration;
import com.ctre.phoenix.sensors.SensorTimeBase;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.Timer;

public final class Arm {
    static CANSparkMax armMotor;
    static PIDController pid;
    static CANCoder encoder;
    static ArmFeedforward feedFoward;
    static Timer timer;
    static CANCoderConfiguration config;
    public double lastVelocity = 0;

    public Arm() {
        armMotor = new CANSparkMax(Constants.Arm.kArmMotorId, MotorType.kBrushless);
        pid = new PIDController(Constants.Arm.kP, 
                                Constants.Arm.kI, 
                                Constants.Arm.kD);   
        encoder = new CANCoder(Constants.Arm.kArmEncoderId);

        // set units of the CANCoder to radians, with velocity being radians per second
        config = new CANCoderConfiguration();
        config.sensorCoefficient = 2 * Math.PI / 4096.0;
        config.unitString = "rad";
        config.sensorTimeBase = SensorTimeBase.PerSecond;
        encoder.configAllSettings(config);

        feedFoward = new ArmFeedforward(Constants.Arm.kS, Constants.Arm.kG, Constants.Arm.kV);
        timer = new Timer();
    }

    public void moveArm(double joyPos) {
        if (encoder.getPosition() >= Constants.Arm.kArmUpperLimit ||
            encoder.getPosition() <= Constants.Arm.kArmLowerLimit) {
            armMotor.set(joyPos);
        } else {
            armMotor.stopMotor();
        }
    }

    public void setArm(int level) {
        double armSetPoint = 0;
        switch(level) {
            case 0:
                armSetPoint = Constants.Arm.kRetracted;
                break;
            case 1:
                armSetPoint = Constants.Arm.kBottomLevel;
                break;
            case 2:
                armSetPoint = Constants.Arm.kMiddleLevel;
                break;
            case 3:
                armSetPoint = Constants.Arm.kTopLevel;
                break;
        }

        
        timer.start();
        double accelRadPerSecSqaured = (lastVelocity - encoder.getVelocity()) / 
                                            Math.pow(timer.get(), 2) * 0.017453; 
        timer.stop(); 
        double feedFowardCalc = feedFoward.calculate(armSetPoint, 
                                                encoder.getVelocity(), 
                                                accelRadPerSecSqaured);

        double pidCalc = MathUtil.clamp(pid.calculate(encoder.getPosition(), armSetPoint), -12, 12);

        double output = feedFowardCalc + pidCalc;
        
        armMotor.setVoltage(output); 
        lastVelocity = encoder.getVelocity();
    }
    }


