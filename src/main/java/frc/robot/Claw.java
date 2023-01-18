package frc.robot;

import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
// import edu.wpi.first.wpilibj.DriverStation;
// import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj.XboxController;

public class Claw {
    static TalonSRX motorOne;
    static TalonSRX motorTwo;
    static TalonSRX motorThree;
    static TalonSRX motorFour;
    static PIDController pidController;
    //makes the motors and pid controller
    public Claw(){
        motorOne = new TalonSRX(Constants.Claw.motorOneNum);
        motorTwo = new TalonSRX(Constants.Claw.motorTwoNum);
        motorThree = new TalonSRX(Constants.Claw.motorThreeNum);
        motorFour = new TalonSRX(Constants.Claw.motorFourNum);
        // pidController = new PIDController(0, 0, 0); //NOT USED
    }
    
    //sucks in the game piece 
    public static void intakeGamePiece(){
        motorOne.set(TalonSRXControlMode.PercentOutput, Constants.Claw.motorOutput); // note from owen: add the 0.3 to settings
        motorTwo.set(TalonSRXControlMode.PercentOutput, -Constants.Claw.motorOutput);// done
        motorThree.set(TalonSRXControlMode.PercentOutput, Constants.Claw.motorOutput);
        motorFour.set(TalonSRXControlMode.PercentOutput, -Constants.Claw.motorOutput);
    }

    //spits out the game piece
    public static void outputGamePiece(){
        motorOne.set(TalonSRXControlMode.PercentOutput, -Constants.Claw.motorOutput);
        motorTwo.set(TalonSRXControlMode.PercentOutput, Constants.Claw.motorOutput);
        motorThree.set(TalonSRXControlMode.PercentOutput, -Constants.Claw.motorOutput);
        motorFour.set(TalonSRXControlMode.PercentOutput, Constants.Claw.motorOutput);
    }
    
    //PID and stuf
    public static void useIntake(double percentOutput){
        // pidController.setSetpoint(100); // note from owen: why are we using pid here? ethan: idk it was abandoned
        // motorOne.move(MathUtil.clamp(pidController.calculate(motorOne.getSelectedSensorPosition()), -1, 1));
    } 

    public static void stopIntake(){

    }

    public static void useOutput(double percentOutput){

    }

    public static void stopOutput(){

    }
}
