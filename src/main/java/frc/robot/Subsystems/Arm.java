package frc.robot.Subsystems;

import frc.robot.Constants;
import frc.robot.IO;

import com.ctre.phoenix.sensors.CANCoder;
import com.ctre.phoenix.sensors.CANCoderConfiguration;
import com.ctre.phoenix.sensors.SensorTimeBase;
import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.CANSparkMax;
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
    static AbsoluteEncoder encoder;
    static ArmFeedforward feedforward;
    static Timer timer;
    static CANCoderConfiguration config;
    static double lastVelocity = 0;
    static DoubleLogEntry motorLog;
    static DoubleLogEntry encoderLog;

    public static void setup() {
        //motor = new CANSparkMax(Constants.Arm.motorID, MotorType.kBrushless);
        motor = new CANSparkMax(Constants.Arm.motorID, MotorType.kBrushless);
        //motor.setSmartCurrentLimit(40);
        
        pid = new PIDController(Constants.Arm.kP, 
                                Constants.Arm.kI, 
                                Constants.Arm.kD);   
        encoder = motor.getAbsoluteEncoder(Type.kDutyCycle);

        // set units of the CANCoder to radians, with velocity being radians per second
        /*
        config = new CANCoderConfiguration();
        config.sensorCoefficient = 2 * Math.PI / 4096.0;
        config.unitString = "rad";
        config.sensorTimeBase = SensorTimeBase.PerSecond;
        encoder.configAllSettings(config);
        */

        feedforward = new ArmFeedforward(Constants.Arm.kS, Constants.Arm.kG, Constants.Arm.kV);
        timer = new Timer();

        DataLog log = DataLogManager.getLog();
        motorLog = new DoubleLogEntry(log, "/arm/motor");
        encoderLog = new DoubleLogEntry(log, "/arm/encoder");
    }

    public static void logData() {
        motorLog.append(motor.get());
        
        encoderLog.append(encoder.getPosition());
    }

    public static void moveArm(double joyPos) {
        if (!IO.isOverrideEnabled()) { 
            if ((encoder.getPosition() >= Constants.Arm.upperLimit && joyPos > 0)|| 
                (encoder.getPosition() <= Constants.Arm.lowerLimit && joyPos < 0)) { // If elevator reach top AND trying to go up
                motor.stopMotor(); 
            }
            else {
                motor.set(joyPos);
            }
        }
    }
    public static void moveArmOverride(double joyPos) {
        motor.set(joyPos);
    }

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
            double feedForwardCalc = Constants.Arm.kG * Math.cos(Math.toRadians(encoder.getPosition()));

            double output = MathUtil.clamp(pid.calculate(encoder.getPosition(), setpoint) + feedForwardCalc, -12, 12);
            
            motor.setVoltage(output); 
            lastVelocity = encoder.getVelocity();
            return pid.atSetpoint();
        }
        return false;
    }
}



