package frc.robot.Subsystems;

import frc.robot.Constants;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.util.datalog.DataLog;
import edu.wpi.first.util.datalog.DoubleLogEntry;
import edu.wpi.first.wpilibj.DataLogManager;

public class Claw {
    static TalonSRX motor;
    static DoubleLogEntry motorLog;

    public static void setup(){
        //claw only has one motor
        motor = new TalonSRX(Constants.Claw.motorID);
        DataLog log = DataLogManager.getLog();
        motorLog = new DoubleLogEntry(log, "/claw/motor");
        motor.configContinuousCurrentLimit(40, 10);
    }

    public static void logData() {
        motorLog.append(motor.getStatorCurrent());
    }

    //sucks in the game piece and stops
    public static void intakeGamePiece(){
        motor.set(TalonSRXControlMode.PercentOutput, Constants.Claw.run);
    }
    public static void intakeGamePiece(double speed){
        motor.set(TalonSRXControlMode.PercentOutput, -Math.abs(speed));

    }
    public static void intakeFineControl(double joyInput){
        motor.set(TalonSRXControlMode.PercentOutput, joyInput);
    }

    //spits out the game piece
    public static void outputGamePiece(){
        motor.set(TalonSRXControlMode.PercentOutput, Constants.Claw.drop);
    }
    public static void outputGamePieceFast(){
        motor.set(TalonSRXControlMode.PercentOutput, Constants.Claw.dropFast);
    }

    public static void stopishMotor() {
        motor.set(ControlMode.PercentOutput, Constants.Claw.stopish);
    }
    public static void stopMotor(){
        motor.set(ControlMode.PercentOutput, 0);
    }
}