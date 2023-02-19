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
        motor.configContinuousCurrentLimit(30, 10);
    }

    //sucks in the game piece and stops
    public static void intakeGamePiece(){
        motor.set(TalonSRXControlMode.PercentOutput, Constants.Claw.run);
    }

    //spits out the game piece
    public static void outputGamePiece(){
        motor.set(TalonSRXControlMode.PercentOutput, -Constants.Claw.run);
    }

    public static void stopishMotor() {
        motor.set(ControlMode.PercentOutput, Constants.Claw.stopish);
    }
}