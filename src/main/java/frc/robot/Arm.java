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
        armMotor.setSmartCurrentLimit(40);
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
        if (Joystick.isOverrrideEnabled() == false) { 
            if (encoder.getPosition() >= Constants.Arm.kArmUpperLimit && joyPos > 0) { // If elevator reach top AND trying to go up
                armMotor.stopMotor(); //
            } else if (encoder.getPosition() <= Constants.Arm.kArmLowerLimit && joyPos < 0) { // If elevator reach bottom ANd trying to go down
                armMotor.stopMotor();
            } else if  (encoder.getPosition() >= Constants.Arm.kArmUpperLimit || // if arm reaches max height
                        encoder.getPosition() <= Constants.Arm.kArmLowerLimit) { // or 
                armMotor.stopMotor();
            } else {
                armMotor.set(joyPos);
            }
        } else {
            armMotor.set(joyPos);
        }
    }

    public void setArm(int level, double joyPos) {
        if (Joystick.isOverrrideEnabled() == false) {
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
        

            timer.stop(); 
            double accelRadPerSecond = (lastVelocity - encoder.getVelocity()) / timer.get(); 
            timer.reset();
            timer.start();
            double feedFowardCalc = feedFoward.calculate(armSetPoint, 
                                                    encoder.getVelocity(), 
                                                    accelRadPerSecond);

            double output = MathUtil.clamp(pid.calculate(encoder.getPosition(), armSetPoint) + feedFowardCalc, -12, 12);
            
            armMotor.setVoltage(output); 
            lastVelocity = encoder.getVelocity();
            } else {
                armMotor.set(joyPos);
        }
    }
}



