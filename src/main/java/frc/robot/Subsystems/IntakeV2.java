package frc.robot.Subsystems;

import frc.robot.Constants;
import frc.robot.IO;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.util.datalog.DataLog;
import edu.wpi.first.util.datalog.DoubleLogEntry;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ColorSensorV3;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

// Notes on intakev2
//positive joystick on handoff is 
//positive joystick on intake is
//moving intake outward is

public class IntakeV2 {
    private static CANSparkMax retractMotor = new CANSparkMax(Constants.IntakeV2.retractMotorID, MotorType.kBrushless);
    private static TalonSRX handOffMotor = new TalonSRX(Constants.IntakeV2.handoffMotorID);
    private static PIDController pid = new PIDController(Constants.IntakeV2.Kp, Constants.IntakeV2.Ki, Constants.IntakeV2.Kd);
    private static RelativeEncoder relativeEncoder = retractMotor.getEncoder(); 
    private static double theta;

    public static void setup() {
        retractMotor.setIdleMode(IdleMode.kBrake);
        relativeEncoder.setPosition(-1 / 4);
    }

    public static void runIntakeOverride(double handoffPercentOutput, double intakePercentOutput) {
        retractMotor.set(intakePercentOutput);
        handOffMotor.set(TalonSRXControlMode.PercentOutput, handoffPercentOutput);
    }

    private static boolean moveMotor(double setpoint) {
        theta = relativeEncoder.getPosition() * 2 * Math.PI;
        pid.setSetpoint(setpoint);
        retractMotor.setVoltage(MathUtil.clamp(Constants.IntakeV2.Ks * Math.cos(theta) + pid.calculate(theta), -12, 12));
        return pid.atSetpoint();
    }

    public static boolean extend() {
        if (!IO.isOverrideEnabled()) {
            return moveMotor(Constants.IntakeV2.ExtendPosition);
        }
        return false;
    }

    public static boolean retract() {
        if (!IO.isOverrideEnabled()) {
            return moveMotor(Constants.IntakeV2.RetractPosition);
        }
        return false;
    }

    public static boolean handOff() {
        if (!IO.isOverrideEnabled()){
           return moveMotor(Constants.IntakeV2.HandoffPosition);
        }
        return false;
    }

    public static void slowIntake() {
        if(!IO.isOverrideEnabled()) {
            handOffMotor.set(TalonSRXControlMode.PercentOutput, Constants.IntakeV2.handoffMotorSlow);
        } else {
            handOffMotor.set(TalonSRXControlMode.PercentOutput, 0);
        }
    }

    public static void reverseIntake() {
        if (!IO.isOverrideEnabled()) {
            handOffMotor.set(TalonSRXControlMode.PercentOutput, Constants.IntakeV2.handoffMotorReverse);
        } else {
            handOffMotor.set(TalonSRXControlMode.PercentOutput, 0);
        }
    }

    public static boolean intake() {
        if (!IO.isOverrideEnabled()) {
            handOffMotor.set(TalonSRXControlMode.PercentOutput, Constants.IntakeV2.handOffMotorRun);
            if (handOffMotor.getStatorCurrent() >= Constants.IntakeV2.intakeAmpThreshhold) {
                handOffMotor.set(TalonSRXControlMode.PercentOutput, 0);
                return true;
            }
            else {
                return false;
            }
        }
        return true;
    }

    public static void displayInformation() {
        SmartDashboard.putNumber("Motor Amps", handOffMotor.getStatorCurrent());
        SmartDashboard.putNumber("Angle", relativeEncoder.getPosition() * 2 * Math.PI);
    }

}
