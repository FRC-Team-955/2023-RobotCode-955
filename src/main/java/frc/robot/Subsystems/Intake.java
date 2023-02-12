package frc.robot.Subsystems;

import frc.robot.Constants;

import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

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
    private static TalonSRX itemHolderMotor = new TalonSRX(Constants.Intake.itemHolderMotorId);
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

    //sucks in the game piece and stop
    public static void runFlaps(){
        if (senseObj()) {
            flapLeftMotor.set(TalonSRXControlMode.PercentOutput, 0);
            flapRightMotor.set(TalonSRXControlMode.PercentOutput, 0);
        } else {
            flapLeftMotor.set(TalonSRXControlMode.PercentOutput, Constants.Intake.motorOutput);
            flapRightMotor.set(TalonSRXControlMode.PercentOutput, -Constants.Intake.motorOutput);
        }
    }
    public static void runItemHolder(){

    }

    //spits out the game piece
    public static void reverseFlaps(){
        flapLeftMotor.set(TalonSRXControlMode.PercentOutput, -Constants.Intake.motorOutput);
        flapRightMotor.set(TalonSRXControlMode.PercentOutput, Constants.Intake.motorOutput);
    }

    public static void stopFlaps() {
        flapLeftMotor.set(TalonSRXControlMode.PercentOutput, 0);
        flapRightMotor.set(TalonSRXControlMode.PercentOutput, 0);
    }

    public static void foldInIntake(){
        if (intakeFoldMotorEncoderValue.getPosition() < Constants.Intake.foldedEncoder) {
            foldMotor.set(Constants.Intake.foldMotorOutput); //idk if it's negative or not so it's positive for now
        }
        else{
            foldMotor.set(Constants.Intake.motorStop); // idk if this is negative or not too.
        }
    }

    public static void foldOutIntake(){
        if (intakeFoldMotorEncoderValue.getPosition() > Constants.Intake.unfoldedEncoder){
            foldMotor.set(-Constants.Intake.foldMotorOutput); // idk if this is negative or not too.
        }
        else{
            foldMotor.set(Constants.Intake.motorStop);
        }
    }
      
    public static boolean isIntakeFolded(){
        return intakeFoldMotorEncoderValue.getPosition() > Constants.Intake.foldedEncoder;
    }

    public static boolean isIntakeUnfolded(){
        return intakeFoldMotorEncoderValue.getPosition() < Constants.Intake.unfoldedEncoder;
    }
}
