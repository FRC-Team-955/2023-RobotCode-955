package frc.robot;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public final class Arm {
    static CANSparkMax armMotor;
    static PIDController pid;
    Encoder encoder;
    static ArmFeedforward feedFoward;
    Timer timer;
    double lastVelocity;
    double output;

    public Arm(CANSparkMax motor, Encoder encoder) {
        armMotor = motor;
        armMotor.setSmartCurrentLimit(40);
        pid = new PIDController(Constants.Arm.kP, 
                                Constants.Arm.kI, 
                                Constants.Arm.kD);   
        this.encoder = encoder;

        feedFoward = new ArmFeedforward(Constants.Arm.kS, Constants.Arm.kG, Constants.Arm.kV);
        timer = new Timer();
        timer.start();
        lastVelocity = 0;
        SmartDashboard.putNumber("encoder postion hopefully not dead", encoder.get());
    }

    public void moveArm(double joyPos) {
        if (IO.isOverrrideEnabled() || ((encoder.get() < Constants.Arm.kArmUpperLimit || joyPos < 0) && (encoder.get() > Constants.Arm.kArmLowerLimit || joyPos > 0))) {
            armMotor.set(joyPos);
        } else {
            armMotor.stopMotor();
        }
    }


    public void setArm(int level, double joyPos) {
        if (IO.isOverrrideEnabled() == false) {
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
            double accelRadPerSecond = (lastVelocity - encoder.getRate()) / timer.get(); 
            timer.reset();
            timer.start();
            lastVelocity = encoder.getRate();
            double feedFowardCalc = feedFoward.calculate(armSetPoint, 
                encoder.getRate(), accelRadPerSecond);
            output = MathUtil.clamp(pid.calculate(encoder.get(), armSetPoint) + feedFowardCalc, -1, 1);
            SmartDashboard.putNumber("motor output", output);
            armMotor.set(output);
        } else {
            armMotor.set(joyPos);
        }
    }
}
