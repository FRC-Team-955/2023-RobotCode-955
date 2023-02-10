package frc.robot;

import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class Handoff {
    static TalonSRX handoffMotorOne;
    
    public Handoff(){
        //handoff only has one motor
        handoffMotorOne = new TalonSRX(Constants.Handoff.handoffMotorOneNum);
    }

    public static void holdGamePiece(){
        handoffMotorOne.set(TalonSRXControlMode.PercentOutput, Constants.Handoff.handoffGamepieceHoldMotorOutput);
    }

    //sucks in the game piece and stops
    public static void intakeGamePiece(){
        handoffMotorOne.set(TalonSRXControlMode.PercentOutput, Constants.Handoff.handoffMotorOutput);
}

    //spits out the game piece
    public static void outputGamePiece(){
        handoffMotorOne.set(TalonSRXControlMode.PercentOutput, -Constants.Handoff.handoffMotorOutput);
    }
}
