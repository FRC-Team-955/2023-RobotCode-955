package frc.robot;

import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.Timer;
// import edu.wpi.first.wpilibj.DriverStation;
// import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj.XboxController;
import com.revrobotics.ColorSensorV3;

public class Intake {
    static TalonSRX motorOne;
    static TalonSRX motorTwo;
    static TalonSRX motorThree;
    static TalonSRX motorFour;
    static ColorSensorV3 proximityDetector;
    // static PIDController pidController;
    //makes the motors and pid controller
    public Intake(){
        motorOne = new TalonSRX(Constants.Claw.motorOneNum);
        motorTwo = new TalonSRX(Constants.Claw.motorTwoNum);
        proximityDetector = new ColorSensorV3(null);
        // pidController = new PIDController(0, 0, 0); //NOT USED
    }
   
    static Timer timer = new Timer();
    
    public static void startIntake(){
        timer.reset();
        timer.start();
    }
    //sucks in the game piece and stop
    public static void runEthanWheels(){
        if(timer.get() < 4){
        motorOne.set(TalonSRXControlMode.PercentOutput, Constants.Claw.motorOutput); // note from owen: add the 0.3 to settings
        motorTwo.set(TalonSRXControlMode.PercentOutput, -Constants.Claw.motorOutput);// done

        proximityDetector.senseObj();
        if proximityDetector.senseObj == true{
            motorOne.set(TalonSRXControlMode.PercentOutput, 0);
            motorTwo.set(TalonSRXControlMode.PercentOutput, 0);
        }
        else{
            
        }
    }
}

    //spits out the game piece
    public static void reverseEthanWheels(){
        motorOne.set(TalonSRXControlMode.PercentOutput, -Constants.Claw.motorOutput);
        motorTwo.set(TalonSRXControlMode.PercentOutput, Constants.Claw.motorOutput);
    }

    public static void foldInIntake(){

    }

    public static void foldOutIntake(){

    }
    
}
