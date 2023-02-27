package frc.robot.Subsystems;

import frc.robot.Constants;
import frc.robot.IO;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.util.datalog.DataLog;
import edu.wpi.first.util.datalog.DoubleLogEntry;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.I2C.Port;

import com.revrobotics.CANSparkMax;
import com.revrobotics.ColorSensorV3;
import com.revrobotics.SparkMaxAbsoluteEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.SparkMaxAbsoluteEncoder.Type;

public class Intake {
    private static TalonSRX flapLeftMotor = new TalonSRX(Constants.Intake.flapLeftMotorId);
    private static TalonSRX flapRightMotor = new TalonSRX(Constants.Intake.flapRightMotorId);
    private static TalonSRX holdMotor = new TalonSRX(Constants.Intake.holdMotorId);
    private static CANSparkMax foldMotor = new CANSparkMax(Constants.Intake.foldMotorId, MotorType.kBrushless);

    private static ColorSensorV3 colorSensor = new ColorSensorV3(Port.kOnboard);
    private static SparkMaxAbsoluteEncoder intakeFoldMotorEncoderValue = foldMotor.getAbsoluteEncoder(Type.kDutyCycle); //this is here because 2 functions need it at the same time
    private static DoubleLogEntry flapLeftMotorLog;
    private static DoubleLogEntry flapRightMotorLog;

    public static void setup(){
        foldMotor.setIdleMode(IdleMode.kBrake);

        DataLog log = DataLogManager.getLog();
        flapLeftMotorLog = new DoubleLogEntry(log, "/intake/flapLeftMotor");
        flapRightMotorLog = new DoubleLogEntry(log, "/intake/flapRightMotor");
    }

    public static void logData() {
        flapLeftMotorLog.append(flapLeftMotor.getStatorCurrent());
        flapRightMotorLog.append(flapRightMotor.getStatorCurrent());
    }

    static public boolean senseObj() {
        // getProximity() This value is largest when an object is close to the sensor and smallest when far away.
        //System.out.println(colorSensor.getProximity());
        return colorSensor.getProximity() > 1000; //CHANGE THE 1000 not actual number
    }

    
    public static void holdItem(){
        if(!IO.isOverrideEnabled()) {
            holdMotor.set(TalonSRXControlMode.PercentOutput, Constants.Intake.holdMotorSlow);
        }
    }

    public static void releaseItem(){
        if(!IO.isOverrideEnabled()) {
            holdMotor.set(TalonSRXControlMode.PercentOutput, -Constants.Intake.holdMotorRun);
        }
    }

    public static void holdItemUntilFolded(){
        if(!IO.isOverrideEnabled()) {
            if(!foldInIntake()){
                holdItem();
            }else{
                holdMotor.set(TalonSRXControlMode.PercentOutput, 0);
            }
        }
    }

    //sucks in the game piece and stop
    public static void runFlaps(){
        if(!IO.isOverrideEnabled()) {
            if (senseObj()) {
                flapLeftMotor.set(TalonSRXControlMode.PercentOutput, 0);
                flapRightMotor.set(TalonSRXControlMode.PercentOutput, 0);
                holdMotor.set(TalonSRXControlMode.PercentOutput, Constants.Intake.holdMotorSlow);
            } else {
                flapLeftMotor.set(TalonSRXControlMode.PercentOutput, Constants.Intake.flapMotorRun);//CHANGE ALL THESE FROM - or not -
                flapRightMotor.set(TalonSRXControlMode.PercentOutput, -Constants.Intake.flapMotorRun);//depends on tests
                holdMotor.set(TalonSRXControlMode.PercentOutput, -Constants.Intake.holdMotorRun);
            }
        }
    }
    
    //spits out the game piece
    public static void reverseFlaps(){
        if(!IO.isOverrideEnabled()) {
            flapLeftMotor.set(TalonSRXControlMode.PercentOutput, -Constants.Intake.flapMotorRun);
            flapRightMotor.set(TalonSRXControlMode.PercentOutput, Constants.Intake.flapMotorRun);
        }
    }

    public static void stopFlaps() {
        if(!IO.isOverrideEnabled()) {
            flapLeftMotor.set(TalonSRXControlMode.PercentOutput, 0);
            flapRightMotor.set(TalonSRXControlMode.PercentOutput, 0);
        }
    }

    public static boolean foldInIntake() {
        if(!IO.isOverrideEnabled()) {
            //simple ps controller to tune if time
            // if (intakeFoldMotorEncoderValue.getPosition() < Constants.Intake.foldedEncoder) {
            //     foldMotor.setVoltage(MathUtil.clamp(Constants.Intake.foldMotorKs + Constants.Intake.foldMotorKp*(Constants.Intake.foldedEncoder - intakeFoldMotorEncoderValue.getPosition()), -12, 12));
            // } else {
            //     foldMotor.set(0);
            // }
            // bang bang method, use if there is not much time
            if (intakeFoldMotorEncoderValue.getPosition() < Constants.Intake.foldedEncoder) {
                foldMotor.set(Constants.Intake.foldMotorRun); //idk if it's negative or not so it's positive for now
            }
            else{
                foldMotor.set(0);
            }
            //fold in ethan wheels
            //CHANGE ALL ENCODER VALUES WHEN TESTING!!!!!!!!!!!!!!!!!!!!!!!!!!!
            if (intakeFoldMotorEncoderValue.getPosition() > 80) { // maybe use a pid?
                flapLeftMotor.set(ControlMode.Current, MathUtil.clamp(Constants.Intake.ethanWheelsKp*(90 - flapLeftMotor.getSelectedSensorPosition()), -12, 12));
                flapRightMotor.set(ControlMode.Current, MathUtil.clamp(Constants.Intake.ethanWheelsKp*(90 - flapRightMotor.getSelectedSensorPosition()), -12, 12));
            }
            return intakeFoldMotorEncoderValue.getPosition() > Constants.Intake.foldedEncoder;
        }
        return false;
    }

    public static boolean foldOutIntake(){
        if(!IO.isOverrideEnabled()) {
            //maybe implement PID, but doesn't seem nearly as needed
            if (intakeFoldMotorEncoderValue.getPosition() > Constants.Intake.unfoldedEncoder){
                foldMotor.set(-Constants.Intake.foldMotorRun); // idk if this is negative or not too.
            }
            else{
                foldMotor.set(0);
            }
            return intakeFoldMotorEncoderValue.getPosition() < Constants.Intake.unfoldedEncoder;
        }
        return false;
    }

    public static void intakeOverride(double pov) { // pov should probably be joystick.getPOV
        if(pov == -1) {
            foldMotor.set(0);
            flapLeftMotor.set(TalonSRXControlMode.PercentOutput, 0);
            flapRightMotor.set(TalonSRXControlMode.PercentOutput, 0);
            holdMotor.set(TalonSRXControlMode.PercentOutput, 0);
        } else if ((pov / 90) % 1 == 0) {
            foldMotor.set(-Math.cos(Math.toRadians(pov)) * Constants.Intake.foldMotorRun);
            holdMotor.set(TalonSRXControlMode.PercentOutput, -Math.sin(Math.toDegrees(pov)) * Constants.Intake.holdMotorRun);
            flapLeftMotor.set(TalonSRXControlMode.PercentOutput, Math.sin(Math.toDegrees(pov) * Constants.Intake.flapMotorRun));
            flapRightMotor.set(TalonSRXControlMode.PercentOutput, -Math.sin(Math.toDegrees(pov) * Constants.Intake.flapMotorRun));
        }
        // Should be the same thing, uncomment if stuff starts breaking
        // if(pov == 0) {
        //     foldMotor.set(-Constants.Intake.foldMotorRun);
        // }
        // if(pov == 180) {
        //     foldMotor.set(Constants.Intake.foldMotorRun);
        // }
        // if(pov == 90) {
        //     flapLeftMotor.set(TalonSRXControlMode.PercentOutput, Constants.Intake.flapMotorRun);
        //     flapRightMotor.set(TalonSRXControlMode.PercentOutput, -Constants.Intake.flapMotorRun);
        //     holdMotor.set(TalonSRXControlMode.PercentOutput, -Constants.Intake.holdMotorRun);
        // }
        // if(pov == 270) {
        //     flapLeftMotor.set(TalonSRXControlMode.PercentOutput, -Constants.Intake.flapMotorRun);
        //     flapRightMotor.set(TalonSRXControlMode.PercentOutput, Constants.Intake.flapMotorRun);
        //     holdMotor.set(TalonSRXControlMode.PercentOutput, Constants.Intake.holdMotorRun); 
        // }
    }
}
