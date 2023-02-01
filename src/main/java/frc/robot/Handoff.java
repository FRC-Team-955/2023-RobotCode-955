package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.util.datalog.DataLog;
import edu.wpi.first.util.datalog.DoubleLogEntry;
import edu.wpi.first.wpilibj.DataLogManager;

public class Handoff {
    static TalonSRX handoffMotorOne;
    static DoubleLogEntry motorLog;

    public Handoff(){
        //handoff only has one motor
        handoffMotorOne = new TalonSRX(Constants.Handoff.handoffMotorOneNum);
        DataLog log = DataLogManager.getLog();
        motorLog = new DoubleLogEntry(log, "/handoff/motor");
    }

    //sucks in the game piece and stops
    public static void intakeGamePiece(){
        handoffMotorOne.set(TalonSRXControlMode.PercentOutput, Constants.Intake.motorOutput);
    }

    //spits out the game piece
    public static void outputGamePiece(){
        handoffMotorOne.set(TalonSRXControlMode.PercentOutput, -Constants.Intake.motorOutput);
    }

    public static void stopMotor() {
        handoffMotorOne.set(ControlMode.PercentOutput, 0);
    }
}