package frc.robot.Subsystems;

import frc.robot.Constants;
import frc.robot.IO;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

// Notes on intakev2
//positive joystick on handoff is 
//positive joystick on intake is
//moving intake outward is

public class IntakeV2 {
    private static CANSparkMax retractMotor = new CANSparkMax(Constants.IntakeV2.retractMotorID, MotorType.kBrushless);
    private static TalonSRX handOffMotorFront = new TalonSRX(Constants.IntakeV2.handoffMotorFrontID);
    private static TalonSRX handOffMotorBack = new TalonSRX(Constants.IntakeV2.handoffMotorBackID);
    private static PIDController pid = new PIDController(Constants.IntakeV2.Kp, Constants.IntakeV2.Ki, Constants.IntakeV2.Kd);
    private static RelativeEncoder relativeEncoder = retractMotor.getEncoder(); 
    private static double theta;

    public static void setup() {
        retractMotor.setIdleMode(IdleMode.kCoast);
        retractMotor.setSmartCurrentLimit(20);
        handOffMotorBack.set(ControlMode.Follower, Constants.IntakeV2.handoffMotorFrontID);
    }

    public static boolean extendNoPid(){
        if (!IO.isOverrideEnabled()) {
            theta = relativeEncoder.getPosition() * 2 * Math.PI / 63 + Math.toRadians(90+42);
            if(theta < Math.PI/2){
                retractMotor.setVoltage(3);
                return true;
            }
            retractMotor.setVoltage(0);
        }
        return false;
    }

    public static boolean retractNoPid(){
        if (!IO.isOverrideEnabled()) {
            theta = relativeEncoder.getPosition() * 2 * Math.PI / 63 + Math.toRadians(90+42);
            if(theta > Math.PI/2){
                retractMotor.setVoltage(-3);
                return true;
            }
            retractMotor.setVoltage(0);
        }
        return false;
    }

    public static boolean handOffNoPid(){
        if (!IO.isOverrideEnabled()) {
            theta = relativeEncoder.getPosition() * 2 * Math.PI / 63 + Math.toRadians(90+42);
            if(theta > Math.PI/2){
                retractMotor.setVoltage(-3);
                return true;
            }else{
                handOffMotorFront.set(ControlMode.PercentOutput, .2);
                handOffMotorBack.set(ControlMode.PercentOutput, .2);
            }
            retractMotor.setVoltage(0);
        }
        return false;
    }

    public static boolean moveIntake(double current) {
        if (!IO.isOverrideEnabled()) {
            //handOffMotor.set(TalonSRXControlMode.Current, current);
            //return handOffMotor.getStatorCurrent() >= Constants.IntakeV2.intakeAmpThreshhold;
        }
        return false;
    }

    public static void runIntakeOverride(double handoffPercentOutput, double intakePercentOutput) {
        //retractMotor.set(intakePercentOutput);
        retractMotor.setVoltage(0.3);
        //handOffMotor.set(TalonSRXControlMode.PercentOutput, handoffPercentOutput);
    }

    public static void slowIntake() {
        if(!IO.isOverrideEnabled()) {
            //handOffMotor.set(TalonSRXControlMode.PercentOutput, Constants.IntakeV2.handoffMotorSlow);
        } else {
            //handOffMotor.set(TalonSRXControlMode.PercentOutput, 0);
        }
    }

    public static void reverseIntake() {
        if (!IO.isOverrideEnabled()) {
            //handOffMotor.set(TalonSRXControlMode.PercentOutput, Constants.IntakeV2.handoffMotorReverse);
        } else {
            //handOffMotor.set(TalonSRXControlMode.PercentOutput, 0);
        }
    }

    public static boolean intake() {
        if (!IO.isOverrideEnabled()) {
            //handOffMotor.set(TalonSRXControlMode.PercentOutput, Constants.IntakeV2.handOffMotorRun);
            //if (handOffMotor.getStatorCurrent() >= Constants.IntakeV2.intakeAmpThreshhold) {
                //handOffMotor.set(TalonSRXControlMode.PercentOutput, 0);
                //return true;
            //}
            //else {
                return false;
            //}
        }
        return true;
    }

    public static void displayInformation() {
        //SmartDashboard.putNumber("Motor Amps", handOffMotor.getStatorCurrent());
        SmartDashboard.putNumber("Encoder Postion", relativeEncoder.getPosition());
        SmartDashboard.putNumber("rotations", relativeEncoder.getPosition() / 63);
        SmartDashboard.putNumber("Angle", relativeEncoder.getPosition() / 63 * 360+90+42);
    }

}
