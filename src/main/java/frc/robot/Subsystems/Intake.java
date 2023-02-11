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
    static TalonSRX intakeMotorOne = new TalonSRX(Constants.Intake.motorOneNum);
    static TalonSRX intakeMotorTwo = new TalonSRX(Constants.Intake.motorTwoNum);
    static ColorSensorV3 colorSensor = new ColorSensorV3(Port.kOnboard);
    static CANSparkMax intakeFoldMotor = new CANSparkMax(Constants.Intake.intakeFoldMotorNum, MotorType.kBrushless);
    static SparkMaxAbsoluteEncoder intakeFoldMotorEncoderValue = intakeFoldMotor.getAbsoluteEncoder(Type.kDutyCycle); //this is here because 2 functions need it at the same time
    static DoubleLogEntry motorOneLog;
    static DoubleLogEntry motorTwoLog;

    public static void setup(){
        intakeFoldMotor.setIdleMode(IdleMode.kBrake);

        DataLog log = DataLogManager.getLog();
        motorOneLog = new DoubleLogEntry(log, "/intake/motorOne");
        motorTwoLog = new DoubleLogEntry(log, "/intake/motorTwo");
    }

    public static void logData() {
        motorOneLog.append(intakeMotorOne.getStatorCurrent());
        motorTwoLog.append(intakeMotorTwo.getStatorCurrent());
    }

    static public boolean senseObj() {
        //System.out.println(colorSensor.getProximity());
        return colorSensor.getProximity() < 1000; //CHANGE THE 1000 not actual number
    }

    //sucks in the game piece and stop
    public static void runEthanWheels(){
        if (senseObj()) {
            intakeMotorOne.set(TalonSRXControlMode.PercentOutput, Constants.Intake.motorOutput);
            intakeMotorTwo.set(TalonSRXControlMode.PercentOutput, -Constants.Intake.motorOutput);
        } else {
            intakeMotorOne.set(TalonSRXControlMode.PercentOutput, 0);
            intakeMotorTwo.set(TalonSRXControlMode.PercentOutput, 0);
        }
    }

    //spits out the game piece
    public static void reverseEthanWheels(){
        intakeMotorOne.set(TalonSRXControlMode.PercentOutput, -Constants.Intake.motorOutput);
        intakeMotorTwo.set(TalonSRXControlMode.PercentOutput, Constants.Intake.motorOutput);
    }

    public static void stopEthanWheels() {
        intakeMotorOne.set(TalonSRXControlMode.PercentOutput, 0);
        intakeMotorTwo.set(TalonSRXControlMode.PercentOutput, 0);
    }

    public static void foldInIntake(){
        if (intakeFoldMotorEncoderValue.getPosition() < Constants.Intake.intakeFoldedEncoder) {
            intakeFoldMotor.set(Constants.Intake.intakeFoldMotorOutput); //idk if it's negative or not so it's positive for now
        }
        else{
            intakeFoldMotor.set(Constants.Intake.intakeMotorStop); // idk if this is negative or not too.
        }
    }

    public static void foldOutIntake(){
        if (intakeFoldMotorEncoderValue.getPosition() > Constants.Intake.intakeUnFoldedEncoder){
            intakeFoldMotor.set(-Constants.Intake.intakeFoldMotorOutput); // idk if this is negative or not too.
        }
        else{
            intakeFoldMotor.set(Constants.Intake.intakeMotorStop);
        }
    }
      
    public static boolean isIntakeFolded(){
        return intakeFoldMotorEncoderValue.getPosition() > Constants.Intake.intakeFoldedEncoder;
    }

    public static boolean isIntakeUnFolded(){
        return intakeFoldMotorEncoderValue.getPosition() < Constants.Intake.intakeUnFoldedEncoder;
    }
}
