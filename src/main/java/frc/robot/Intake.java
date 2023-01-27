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
    static TalonSRX intakeMotorOne;
    static TalonSRX intakeMotorTwo;
    static ColorSensorV3 colorSensor;
    static TalonSRX intakeFoldMotor;
    // static PIDController pidController;
    //makes the motors and pid controller
    public Intake(){
        intakeMotorOne = new TalonSRX(Constants.Claw.motorOneNum);
        intakeMotorTwo = new TalonSRX(Constants.Claw.motorTwoNum);
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
        intakeMotorOne.set(TalonSRXControlMode.PercentOutput, Constants.Claw.motorOutput); // note from owen: add the 0.3 to settings
        intakeMotorTwo.set(TalonSRXControlMode.PercentOutput, -Constants.Claw.motorOutput); //done
        }
    }

    static public boolean senseObj() { // boolean if using the if else in the function
        System.out.println(colorSensor.getProximity());
        if(colorSensor.getProximity() < 1000) { //CHANGE THE 1000 not actual number
            intakeMotorOne.set(TalonSRXControlMode.PercentOutput, Constants.Claw.motorStop);
            intakeMotorTwo.set(TalonSRXControlMode.PercentOutput, Constants.Claw.motorStop);
            return true;
        }
        else {
            return false;
        }
}

    //spits out the game piece
    public static void reverseEthanWheels(){
        intakeMotorOne.set(TalonSRXControlMode.PercentOutput, -Constants.Claw.motorOutput);
        intakeMotorTwo.set(TalonSRXControlMode.PercentOutput, Constants.Claw.motorOutput);
    }

    public static void foldInIntake(){
        intakeFoldMotor.setSelectedSensorPosition(0);
        if 
    }

    public static void foldOutIntake(){

    }

}
