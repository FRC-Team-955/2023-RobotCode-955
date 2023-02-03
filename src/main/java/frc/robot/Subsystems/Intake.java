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
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.SparkMaxAbsoluteEncoder.Type;

public class Intake {
    static TalonSRX intakeMotorOne;
    static TalonSRX intakeMotorTwo;
    static ColorSensorV3 colorSensor;
    static CANSparkMax intakeFoldMotor = new CANSparkMax(Constants.Intake.intakeFoldMotorNum, MotorType.kBrushless);
    //THIS IS CAN SPARK MAX, THE INTAKE FOLD THING
    static SparkMaxAbsoluteEncoder intakeFoldMotorEncoderValue = intakeFoldMotor.getAbsoluteEncoder(Type.kDutyCycle); //this is here because 2 functions need it at the same time
    static DoubleLogEntry motorOneLog;
    static DoubleLogEntry motorTwoLog;

    public static void setup(){
        intakeMotorOne = new TalonSRX(Constants.Intake.motorOneNum);
        intakeMotorTwo = new TalonSRX(Constants.Intake.motorTwoNum);;
        colorSensor = new ColorSensorV3(Port.kOnboard);

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

    //THIS IS CAN SPARK MAX USE ABSOLUTE ENCODER!!! <-----IMPORTANT IMPORTANT IMPORTANT (for future ethan kim, others ignore)
    public static boolean foldInIntake(){
        if (intakeFoldMotorEncoderValue.getPosition() > 87 && intakeFoldMotorEncoderValue.getPosition() < 93) {
            intakeFoldMotor.set(Constants.Intake.intakeFoldMotorOutput); //idk if it's negative or not so it's positive for now
        }
        return intakeFoldMotorEncoderValue.getPosition() > 87 && intakeFoldMotorEncoderValue.getPosition() < 93;
    }
            
    public static boolean foldOutIntake(){
        if (intakeFoldMotorEncoderValue.getPosition() > -3 && intakeFoldMotorEncoderValue.getPosition() < 3){
            intakeFoldMotor.set(-Constants.Intake.intakeFoldMotorNum); // idk if this is negative or not too.
        }
        return intakeFoldMotorEncoderValue.getPosition() > -3 && intakeFoldMotorEncoderValue.getPosition() < 3;
    }
}
