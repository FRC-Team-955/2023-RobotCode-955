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
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.I2C.Port;

import com.revrobotics.CANSparkMax;
import com.revrobotics.ColorSensorV3;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxAbsoluteEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.SparkMaxAbsoluteEncoder.Type;

public class IntakeV2 {
    private static CANSparkMax retractMotor = new CANSparkMax(Constants.IntakeV2.retractMotorID, MotorType.kBrushless);
    private static TalonSRX handOffMotor = new TalonSRX(Constants.IntakeV2.retractMotorID);
    private static PIDController pid = new PIDController(0, 0, 0);
    private static RelativeEncoder encoder;

    public static void setup() {
        retractMotor.setIdleMode(IdleMode.kBrake);
        encoder = retractMotor.getEncoder();
        pid.setTolerance(Constants.IntakeV2.tolerance);
    }

    private static double getMovement(double setpoint) {
        return pid.calculate(setpoint) + Math.cos(encoder.getPosition() * 2 * Math.PI) * Constants.IntakeV2.feedforwardConstant;
    }

    public static boolean extend() {
        if (!IO.isOverrideEnabled()) {
            retractMotor.setVoltage(MathUtil.clamp(getMovement(Constants.IntakeV2.extendPosition), -12, 12));
            return pid.atSetpoint();
        }
        return false;
    }
    

    public static boolean retract() {
        if (!IO.isOverrideEnabled()) {
            retractMotor.setVoltage(MathUtil.clamp(getMovement(Constants.IntakeV2.retractPosition), -12, 12));
            return pid.atSetpoint();
        }
        return false;
    }

    public static boolean intake() {
        if (!IO.isOverrideEnabled()) {
            if (handOffMotor.getStatorCurrent() >= Constants.IntakeV2.intakeAmpThreshhold) {
                handOffMotor.set(TalonSRXControlMode.PercentOutput, 0);
                return true;
            }
            else {
                handOffMotor.set(TalonSRXControlMode.PercentOutput, Constants.IntakeV2.handOffMotorRun);
                return false;
            }
        }
        return true;
    }

    public static boolean handOff() {
        if (!IO.isOverrideEnabled())
    }

}
