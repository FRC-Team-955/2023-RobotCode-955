package frc.robot;

import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class Handoff {
    static TalonSRX handoffMotorOne;
    static TalonSRX handoffMotorTwo;
    
    public Handoff(){
        //handoff only has one motor
        handoffMotorOne = new TalonSRX(Constants.Handoff.handoffMotorOneNum);
    }

    //sucks in the game piece and stops
    public static void intakeGamePiece(){
        handoffMotorOne.set(TalonSRXControlMode.PercentOutput, Constants.Claw.motorOutput);
}

    //spits out the game piece
    public static void outputGamePiece(){
        handoffMotorOne.set(TalonSRXControlMode.PercentOutput, -Constants.Claw.motorOutput);
    }
}
