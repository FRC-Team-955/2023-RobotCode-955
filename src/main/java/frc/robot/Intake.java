package frc.robot;

import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.I2C.Port;
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
    static ColorSensorV3 colorSensor;
    // static PIDController pidController;
    //makes the motors and pid controller
    public Intake(){
        motorOne = new TalonSRX(Constants.Claw.motorOneNum);
        motorTwo = new TalonSRX(Constants.Claw.motorTwoNum);
        // pidController = new PIDController(0, 0, 0); //NOT USED
    }
   
    static Timer timer = new Timer();
    
    public static void startIntake(){
        timer.reset();
        timer.start();
    }
    //sucks in the game piece and stop
    public static void runEthanWheels(){
        colorSensor = new ColorSensorV3(Port.kOnboard);
        
        if(timer.get() < 4){
        motorOne.set(TalonSRXControlMode.PercentOutput, Constants.Claw.motorOutput); // note from owen: add the 0.3 to settings
        motorTwo.set(TalonSRXControlMode.PercentOutput, -Constants.Claw.motorOutput); //done
    }


    static public boolean senseObj() { // boolean if using the if else in the function
        System.out.println(colorSensor.getProximity());
        if(colorSensor.getProximity() < 1000) { //CHANGE THE 1000 not actual number
            motorOne.set(TalonSRXControlMode.PercentOutput, Constants.Claw.motorStop);
            motorTwo.set(TalonSRXControlMode.PercentOutput, Constants.Claw.motorStop);
            return true;
        }
        else {
            return false;
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
