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
    private static TalonSRX motorLeft = new TalonSRX(Constants.IntakeV2.motorLeftID);
    private static TalonSRX motorRight = new TalonSRX(Constants.IntakeV2.motorRightID);
    private static PIDController pid = new PIDController(Constants.IntakeV2.Kp, Constants.IntakeV2.Ki, Constants.IntakeV2.Kd);
    private static RelativeEncoder relativeEncoder;
    private static double theta;
    

    public static void setup() {
        handOffMotor.setIdleMode(IdleMode.kCoast);
        handOffMotor.setSmartCurrentLimit(20);
        motorRight.overrideLimitSwitchesEnable(false);
        motorRight.set(ControlMode.Follower, Constants.IntakeV2.motorLeftID);
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
            if(getPosition() < 15){
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
            if(getPosition() > 10){
                handOffMotor.setVoltage(4);
            }else{
                handOffMotor.setVoltage(0);
                return true;
            }
        }
        return false;
    }

    public static boolean handOffNoPid(){
        if(getPosition() > 10){
            slowIntake();
            handOffMotor.setVoltage(4);
            return false;
        }else if(getPosition() > 8){
            slowIntake();
            handOffMotor.setVoltage(0);
            return false;
        }else{
            reverseIntake();
            handOffMotor.setVoltage(0);
            return true;
        }
        // return false;
    }
    public static void stopHandoff(){
        handOffMotor.setVoltage(0);
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
        SmartDashboard.putNumber("relativeEncoder", relativeEncoder.getPosition());
        SmartDashboard.putNumber("ams?", motorLeft.getStatorCurrent());
        SmartDashboard.putNumber("handoffAngle", getPosition());

        // SmartDashboard.putNumber("Left Motor Amps", motorLeft.getStatorCurrent());
        // SmartDashboard.putNumber("Right Motor Amps", motorRight.getStatorCurrent());
        // SmartDashboard.putNumber("Encoder Postion", relativeEncoder.getPosition());
        // SmartDashboard.putNumber("rotations", relativeEncoder.getPosition() / 63);
        // SmartDashboard.putNumber("Angle", relativeEncoder.getPosition() / 63 * 360+42+90);
    }

}
