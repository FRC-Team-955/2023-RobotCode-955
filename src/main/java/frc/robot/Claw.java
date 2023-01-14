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
    static PIDController pidController;
    //makes the motors and pid controller
    public Claw(){
        motorOne = new TalonSRX(1);
        motorTwo = new TalonSRX(2);
        pidController = new PIDController(0, 0, 0);
    }
    
    //sucks in the game piece 
    public static void intakeGamePiece(){
        motorOne.set(TalonSRXControlMode.PercentOutput, 0.3); // note from owen: add the 0.3 to settings
        motorTwo.set(TalonSRXControlMode.PercentOutput, -0.3);
    }

    //spits out the game piece
    public static void outputGamePiece(){
        motorOne.set(TalonSRXControlMode.PercentOutput, -0.3);
        motorTwo.set(TalonSRXControlMode.PercentOutput, 0.3);
    }
    
    //PID and stuf
    public static void useIntake(double percentOutput){
        pidController.setSetpoint(100); // note from owen: why are we using pid here?
        motorOne.move(MathUtil.clamp(pidController.calculate(motorOne.getSelectedSensorPosition()), -1, 1));
    } 

    public static void stopIntake(){

    }

    public static void useOutput(double percentOutput){

    }

    public static void stopOutput(){

    }
}
