package frc.robot.Subsystems;

import frc.robot.Constants;
import frc.robot.IO;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class IntakeV2 {
    private static CANSparkMax handOffMotor = new CANSparkMax(Constants.IntakeV2.handOffMotorID, MotorType.kBrushless);
    private static TalonSRX motorLeft = new TalonSRX(Constants.IntakeV2.motorLeftID);
    private static TalonSRX motorRight = new TalonSRX(Constants.IntakeV2.motorRightID);
    private static PIDController pid = new PIDController(Constants.IntakeV2.Kp, Constants.IntakeV2.Ki, Constants.IntakeV2.Kd);
    private static RelativeEncoder relativeEncoder = handOffMotor.getEncoder(); 
    private static double theta;

    public static void setup() {
        handOffMotor.setIdleMode(IdleMode.kCoast);
        handOffMotor.setSmartCurrentLimit(20);
        motorRight.overrideLimitSwitchesEnable(false);
        motorRight.set(ControlMode.Follower, Constants.IntakeV2.motorLeftID);
    }

    public static boolean extendNoPid(){
        if (!IO.isOverrideEnabled()) {
            theta = relativeEncoder.getPosition() * 2 * Math.PI / 63 + Math.toRadians(42+90);
            if(theta > Math.PI/3){
                handOffMotor.setVoltage(-4);
                return false;
            }else{
                handOffMotor.setVoltage(0);
                return true;
            }
        }
        return false;
    }

    public static boolean retractNoPid(){
        if (!IO.isOverrideEnabled()) {
            theta = relativeEncoder.getPosition() * 2 * Math.PI / 63 + Math.toRadians(42+90);
            if(theta < 2*Math.PI/3){
                handOffMotor.setVoltage(4);
            }else{
                handOffMotor.setVoltage(0);
                return true;
            }
        }
        return false;
    }

    public static boolean handOffNoPid(){
        if (!IO.isOverrideEnabled()) {
            theta = relativeEncoder.getPosition() * 2 * Math.PI / 63 + Math.toRadians(42+90);
            if(theta < 2*Math.PI/3){
                handOffMotor.setVoltage(4);
                return true;
            }else{
                motorLeft.set(ControlMode.PercentOutput, Constants.IntakeV2.handoffMotorReverse);
                handOffMotor.setVoltage(0);
            }
        }
        return false;
    }

    public static void stopIntake() {
        motorLeft.set(ControlMode.PercentOutput, 0);
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
        handOffMotor.setVoltage(0.3);
        //handOffMotor.set(TalonSRXControlMode.PercentOutput, handoffPercentOutput);
    }

    public static void slowIntake() {
        if(!IO.isOverrideEnabled()) {
            motorLeft.set(TalonSRXControlMode.PercentOutput, Constants.IntakeV2.handoffMotorSlow);
        } else {
            motorLeft.set(TalonSRXControlMode.PercentOutput, 0);
        }
    }

    public static void reverseIntake() {
        if (!IO.isOverrideEnabled()) {
            motorLeft.set(TalonSRXControlMode.PercentOutput, Constants.IntakeV2.handoffMotorReverse);
        } else {
            motorRight.set(TalonSRXControlMode.PercentOutput, 0);
        }
    }

    public static boolean intake() {
        if (!IO.isOverrideEnabled()) {
            motorLeft.set(TalonSRXControlMode.PercentOutput, Constants.IntakeV2.handoffMotorSlow);
            if (motorLeft.getStatorCurrent() >= Constants.IntakeV2.intakeAmpThreshhold) {
                motorLeft.set(TalonSRXControlMode.PercentOutput, 0);
                return true;
            }
            else {
                return false;
            }
        }
        return true;
    }

    public static void displayInformation() {
        // SmartDashboard.putNumber("Left Motor Amps", motorLeft.getStatorCurrent());
        // SmartDashboard.putNumber("Right Motor Amps", motorRight.getStatorCurrent());
        // SmartDashboard.putNumber("Encoder Postion", relativeEncoder.getPosition());
        // SmartDashboard.putNumber("rotations", relativeEncoder.getPosition() / 63);
        // SmartDashboard.putNumber("Angle", relativeEncoder.getPosition() / 63 * 360+42+90);
    }

}
