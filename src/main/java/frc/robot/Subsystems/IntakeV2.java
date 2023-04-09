package frc.robot.Subsystems;

import frc.robot.Constants;
import frc.robot.IO;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class IntakeV2 {
    private static CANSparkMax handOffMotor = new CANSparkMax(Constants.IntakeV2.handOffMotorID, MotorType.kBrushless);
    private static TalonSRX motorLeft = new TalonSRX(Constants.IntakeV2.motorRightID);
    private static TalonSRX motorRight = new TalonSRX(Constants.IntakeV2.motorLeftID);
    private static PIDController pid = new PIDController(Constants.IntakeV2.Kp, Constants.IntakeV2.Ki, Constants.IntakeV2.Kd);
    private static RelativeEncoder relativeEncoder;
    private static double theta;
    

    public static void setup() {
        handOffMotor.setIdleMode(IdleMode.kCoast);
        handOffMotor.setSmartCurrentLimit(20);
        // motorRight.overrideLimitSwitchesEnable(false);
        motorRight.overrideLimitSwitchesEnable(false);
        motorLeft.overrideLimitSwitchesEnable(false);
        motorRight.configPeakCurrentLimit(40);
        motorLeft.configPeakCurrentLimit(40);
        // motorRight.set(ControlMode.Follower, Constants.IntakeV2.motorLeftID);
        relativeEncoder = handOffMotor.getEncoder(); 
    }

    public static void setOffset(){
        // encoder.setPosition(-Constants.Arm.angleOffset/3895.05619213716);
        //0 is in 22 is out
        relativeEncoder.setPosition(0);
        // encoder.setPosition(0);
    }
    public static double getPosition(){
        return -relativeEncoder.getPosition();
        // return encoder.getPosition();
        // / 200 *360
        // - Constants.Arm.angleOffset
    }

    public static boolean extendNoPid(){
        if (!IO.isOverrideEnabled()) {
            if(getPosition() < 14){
                handOffMotor.setVoltage(-5);
                return false;
            }else{
                handOffMotor.setVoltage(-0.2);
                return true;
            }
        }
        return false;
    }
    public static boolean extendNoPidDrop(){
        if(getPosition() < 14){
            motorLeft.set(TalonSRXControlMode.PercentOutput, Constants.IntakeV2.handoffMotorReverse);
            motorRight.set(TalonSRXControlMode.PercentOutput, Constants.IntakeV2.handoffMotorReverse);
        }else{
            motorLeft.set(TalonSRXControlMode.PercentOutput, Constants.IntakeV2.handOffMotorRunFullSpeed);
            motorRight.set(TalonSRXControlMode.PercentOutput, Constants.IntakeV2.handOffMotorRunFullSpeed);
        }
        return extendNoPid();
    }

    public static boolean retractNoPid(){
        if (!IO.isOverrideEnabled()) {
            if(getPosition() > 4){
                handOffMotor.setVoltage(5);
                return false;
            }else{
                handOffMotor.setVoltage(0.1);
                return true;
            }
        }
        return false;
    }

    public static boolean handOffNoPid(){
        if(getPosition() > 11.5){
            slowIntake();
        }else{
            reverseIntake();
        }

        if(getPosition() > 12){
            handOffMotor.setVoltage(10);
            return false;
        }else{
            handOffMotor.setVoltage(0.1);
            return true;
        }
 
        // return false;
    }
    public static void stopHandoff(){
        handOffMotor.setVoltage(0);
    }
    public static void stopIntake() {
        motorLeft.set(ControlMode.PercentOutput, 0);
        motorRight.set(ControlMode.PercentOutput, 0);
    }

    public static void slowIntake() {
        motorLeft.set(TalonSRXControlMode.PercentOutput, Constants.IntakeV2.handOffMotorSlow);
        motorRight.set(TalonSRXControlMode.PercentOutput, 0.5*Constants.IntakeV2.handOffMotorSlow);
    }

    public static void reverseIntake() {
        if (!IO.isOverrideEnabled()) {
            motorLeft.set(TalonSRXControlMode.PercentOutput, Constants.IntakeV2.handoffMotorReverse);
            motorRight.set(TalonSRXControlMode.PercentOutput, Constants.IntakeV2.handoffMotorReverse);

        } else {
            motorLeft.set(TalonSRXControlMode.PercentOutput, 0);
            motorRight.set(TalonSRXControlMode.PercentOutput, 0);
        }
    }

    public static boolean intake() {
        // if (!IO.isOverrideEnabled()) {
            motorLeft.set(TalonSRXControlMode.PercentOutput, Constants.IntakeV2.handOffMotorSlow);
            motorRight.set(TalonSRXControlMode.PercentOutput, 0.5*Constants.IntakeV2.handOffMotorSlow);
            if (motorLeft.getStatorCurrent() >= Constants.IntakeV2.intakeAmpThreshhold) {
                motorLeft.set(TalonSRXControlMode.PercentOutput, 0);
                motorRight.set(TalonSRXControlMode.PercentOutput, 0);
                return true;
            }
            else {
                return false;
            }
        // }
        // return true;
    }

    public static void displayInformation() {
        SmartDashboard.putNumber("handoffPositioin", getPosition());
        // SmartDashboard.putNumber("ams?", motorLeft.getStatorCurrent());
        // SmartDashboard.putNumber("Left Motor Amps", motorLeft.getStatorCurrent());
        // SmartDashboard.putNumber("Right Motor Amps", motorRight.getStatorCurrent());
        // SmartDashboard.putNumber("Encoder Postion", relativeEncoder.getPosition());
        // SmartDashboard.putNumber("rotations", relativeEncoder.getPosition() / 63);
        // SmartDashboard.putNumber("Angle", relativeEncoder.getPosition() / 63 * 360+42+90);
    }

}
