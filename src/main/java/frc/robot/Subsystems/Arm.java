package frc.robot.Subsystems;
import com.ctre.phoenix.sensors.CANCoderConfiguration;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.util.datalog.DataLog;
import edu.wpi.first.util.datalog.DoubleLogEntry;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.IO;

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
        motor.setIdleMode(IdleMode.kCoast);
        motor.setSmartCurrentLimit(30);
        motor.setClosedLoopRampRate(0);
        motor.setOpenLoopRampRate(0);
        motor.burnFlash();
    
        pid = new PIDController(Constants.Arm.kP, 
                                Constants.Arm.kI,
                                Constants.Arm.kD);   
        pid.setTolerance(Constants.Arm.tolerance);

        // encoder = motor.getAbsoluteEncoder(Type.kDutyCycle);
        // encoder = motor.getAlternateEncoder(8192);
        // encoder = motor.getAlternateEncoder( 42);
        encoder = motor.getEncoder();
        // encoder.setPositionConversionFactor(1/90*  2.513758659362793);
        // encoder.setPosition(-Constants.Arm.angleOffset);
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
    public static void setOffset(){
        // encoder.setPosition(-Constants.Arm.angleOffset/3895.05619213716);
        encoder.setPosition(Constants.Arm.angleOffset/360/90 *6800*2.513758659362793);
        // encoder.setPosition(0);
    }
    
    public static void setUpperOffset(){
        encoder.setPosition(Constants.Arm.upperAngleOffset/360/90 *6800* 2.513758659362793);
    }
    public static double getOffsetPosition(){
        //return encoder.getPosition() * 90 / 2.513758659362793;
        return encoder.getPosition() * 360 * 90 / 6800 / 2.513758659362793;
        // return encoder.getPosition();
        // / 200 *360
        // - Constants.Arm.angleOffset
    }
    public static void logData() {
        motorLog.append(motor.get());
        
        encoderLog.append(getOffsetPosition());
    }
    public static void disableArm(){
        motor.setVoltage(0);
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
        motor.setVoltage(-joyPos*0.8 *12+ Constants.Arm.kG * Math.cos(Math.toRadians(getOffsetPosition())));
    }
    //0.5600
    public static double setpoint = 0;
    public static boolean armRetract = true;
    public static void setArm(IO.GridArmPosition level) {
        switch(level) {
            case Retract:
                setpoint = Constants.Arm.retracted;
                armRetract = true;
                break;
            case ConeClosePrepMid:
                setpoint = Constants.Arm.coneClosePrepMid;
                armRetract = false;
                break;
            case ConeCloseReadyMid:
                setpoint = Constants.Arm.coneCloseReadyMid;
                armRetract = false;
                break;
            case ConeClosePrepHigh:
                setpoint = Constants.Arm.coneClosePrepHigh;
                armRetract = false;
                break;
            case ConeCloseReadyHigh:
                setpoint = Constants.Arm.coneCloseReadyHigh;
                armRetract = false;
                break;
            case ConeFarPrepMid:
                setpoint = Constants.Arm.coneFarPrepMid;
                armRetract = false;
                break;
            case ConeFarReadyMid:
                setpoint = Constants.Arm.coneFarReadyMid;
                armRetract = false;
                break;
            case ConeFarPrepHigh:
                setpoint = Constants.Arm.coneFarPrepHigh;
                armRetract = false;
                break;
            case ConeFarReadyHigh:
                setpoint = Constants.Arm.coneFarReadyHigh;
                armRetract = false;
                break;
            case coneFarPrepHighAuto:
                setpoint = Constants.Arm.coneFarPrepHighAuto;
                armRetract = false;
                break;  
            case ConeFarReadyHighAuto:
                setpoint = Constants.Arm.coneFarReadyHighAuto;
                armRetract = false;
                break;
            case CubePrep:
                setpoint = Constants.Arm.cubePrep;
                armRetract = false;
                break;
            case CubeReady:
                setpoint = Constants.Arm.cubeReady;
                armRetract = false;
                break;
            case SingleSubstation:
                setpoint = Constants.Arm.singleSubstation;
                armRetract = false;
                break;
            case DoubleSubstation:
                setpoint = Constants.Arm.doubleSubstation;
                armRetract = false;
                break;
            case Hybrid:
                setpoint = Constants.Arm.hybrid;
                armRetract = true;
                break;
            case NewHybrid:
                setpoint = Constants.Arm.newHybrid;
                armRetract = false;
                break;
            case CubeIntake:
                setpoint = Constants.Arm.cubeIntake;
                armRetract = true;
                break;
            case CubeRetract:
                setpoint = Constants.Arm.cubeRetract;
                armRetract = true;
                break;
            case ConeIntake:
                setpoint = Constants.Arm.coneIntake;
                break;
            case Up:
                setpoint = Constants.Arm.up;
                armRetract = true;
                break;
        }
    }
    public static boolean setArm(){
        double feedForwardCalc = Constants.Arm.kG * Math.cos(Math.toRadians(getOffsetPosition()));

        double output = MathUtil.clamp(pid.calculate(getOffsetPosition(), setpoint) + feedForwardCalc, -12, 12);
        
        System.out.println(setpoint);
        System.out.println(getOffsetPosition());
        System.out.println(encoder.getPosition()/4096);
        motor.setVoltage(output); 
        return Math.abs(getOffsetPosition() - setpoint) < Constants.Arm.tolerance;
            // return pid.atSetpoint();
    }
    public static boolean atConeFarPrepHighPosition(){
        return Math.abs(getOffsetPosition() - Constants.Arm.coneFarPrepHigh) <20;
    }
    public static boolean atConeFarPrepMidPosition(){
        return Math.abs(getOffsetPosition() - Constants.Arm.coneFarPrepMid) < 20;
    }

    public static boolean atConeClosePrepHighPosition(){
        return Math.abs(getOffsetPosition() - Constants.Arm.coneClosePrepHigh) < 20;
    }
    public static boolean atConeClosePrepMidPosition(){
        return Math.abs(getOffsetPosition() - Constants.Arm.coneClosePrepMid) < 20;
    }
    public static boolean atCubeIntakePosition(){
        return Math.abs(getOffsetPosition() - Constants.Arm.cubeIntake) < 20;
    }
    public static boolean atRetractedPosition(){
        return Math.abs(getOffsetPosition() - Constants.Arm.retracted) < 10;
    }
    public static boolean atCubeRetractPosition(){
        return Math.abs(getOffsetPosition() - Constants.Arm.cubeRetract) < 10;
    }
    public static void displayInformation(){
        SmartDashboard.putNumber("Arm getOffsetPosition", getOffsetPosition());
    }
}



