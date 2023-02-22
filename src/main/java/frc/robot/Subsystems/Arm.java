package frc.robot.Subsystems;

import frc.robot.Constants;
import frc.robot.IO;

import com.ctre.phoenix.sensors.CANCoder;
import com.ctre.phoenix.sensors.CANCoderConfiguration;
import com.ctre.phoenix.sensors.SensorTimeBase;
import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.SparkMaxAbsoluteEncoder.Type;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.util.datalog.DataLog;
import edu.wpi.first.util.datalog.DoubleLogEntry;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;

public final class Arm {
    //static CANSparkMax motor;
    static CANSparkMax motor;
    static PIDController pid;
    // static AbsoluteEncoder encoder;
    static RelativeEncoder encoder;
    static ArmFeedforward feedforward;
    static Timer timer;
    static CANCoderConfiguration config;
    static double lastVelocity = 0;
    static DoubleLogEntry motorLog;
    static DoubleLogEntry encoderLog;

    public static void setup() {
        //motor = new CANSparkMax(Constants.Arm.motorID, af]atMotorType.kBrushless);
        motor = new CANSparkMax(Constants.Arm.motorID, MotorType.kBrushless);
        motor.setIdleMode(IdleMode.kBrake);
        //motor.setSmartCurrentLimit(40);
        
        pid = new PIDController(Constants.Arm.kP, 
                                Constants.Arm.kI, 
                                Constants.Arm.kD);   
        // encoder = motor.getAbsoluteEncoder(Type.kDutyCycle);
        encoder = motor.getAlternateEncoder(8192);
        // encoder.setZeroOffset(Constants.Arm.angleOffset);

        // set units of the CANCoder to radians, with velocity being radians per second
        /*
        config = new CANCoderConfiguration();
        config.sensorCoefficient = 2 * Math.PI / 4096.0;
        config.unitString = "rad";
        cosnfig.sensorTimeBase = SensorTimeBase.PerSecond;
        encoder.configAllSettings(config);
        */

        feedforward = new ArmFeedforward(Constants.Arm.kS, Constants.Arm.kG, Constants.Arm.kV);
        timer = new Timer();

        DataLog log = DataLogManager.getLog();
        motorLog = new DoubleLogEntry(log, "/arm/motor");
        encoderLog = new DoubleLogEntry(log, "/arm/encoder");
    }
    public static double getOffsetPosition(){
        return encoder.getPosition() / (80 * (2/3));
    }
    public static void logData() {
        motorLog.append(motor.get());
        
        encoderLog.append(getOffsetPosition());
    }

    public static void moveArm(double joyPos) {
        // System.out.println("Arm Absolute Encoder Position: "+ getOffsetPosition());
        if (!IO.isOverrideEnabled()) { 
            if ((getOffsetPosition() >= Constants.Arm.upperLimit && joyPos > 0)|| 
                (getOffsetPosition() <= Constants.Arm.lowerLimit && joyPos < 0)) { // If arm reach top AND trying to go up
                motor.stopMotor(); 
            }
            else {
                double feedForwardCalc = Constants.Arm.kG * Math.cos(Math.toRadians(getOffsetPosition()));
                feedForwardCalc = 0;
                motor.setVoltage(joyPos*12+ feedForwardCalc);
            }
        }
    }

    public static void moveArmOverride(double joyPos) {
        // System.out.println("Arm Absolute Encoder Position: "+ getOffsetPosition());
        motor.set(-joyPos*0.8);
    }
    //0.5600
    public static double setpoint = 0;
    public static void setArm(IO.GridArmPosition level) {
        switch(level) {
            case Retract:
                setpoint = Constants.Arm.retracted;
                break;
            case ConePrep:
                setpoint = Constants.Arm.conePrep;
                break;
            case ConeReady:
                setpoint = Constants.Arm.coneReady;
                break;
            case CubePrep:
                setpoint = Constants.Arm.cubePrep;
                break;
            case CubeReady:
                setpoint = Constants.Arm.cubeReady;
                break;
            case DoubleSubstation:
                setpoint = Constants.Arm.doubleSubstation;
                break;
        }
    }
    public static boolean setArm(){
        if (!IO.isOverrideEnabled()) {
            // timer.stop(); 
            // double accelRadPerSecond = (lastVelocity - encoder.getVelocity()) / timer.get(); 
            // timer.reset();
            // timer.start();
            // double feedForwardCalc = feedforward.calculate(setpoint, 
            //                                         encoder.getVelocity(), 
            //                                         accelRadPerSecond);
            double feedForwardCalc = Constants.Arm.kG * Math.cos(Math.toRadians(getOffsetPosition()));

            double output = MathUtil.clamp(pid.calculate(getOffsetPosition(), setpoint) + feedForwardCalc, -12, 12);
            
            motor.setVoltage(output); 
            return pid.atSetpoint();
        }
        return false;
    }
}



