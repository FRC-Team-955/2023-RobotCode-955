package frc.robot;

import com.ctre.phoenix.ErrorCode;
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
    static TalonSRX intakeFoldMotor = new
    //THIS IS CAN SPARK MAX, THE INTAKE FOLD THING
    TalonSRX(Constants.Intake.intakeFoldMotorNum);
    static double intakeFoldMotorEncoderValue = intakeFoldMotor.getSelectedSensorPosition(); //this is here because 2 functions need it at the same time
    // static PIDController pidController;
    //makes the motors and pid controller
    public Intake(){
        intakeMotorOne = new TalonSRX(Constants.Intake.motorOneNum);
        intakeMotorTwo = new TalonSRX(Constants.Intake.motorTwoNum);
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
        intakeMotorOne.set(TalonSRXControlMode.PercentOutput, Constants.Intake.motorOutput); // note from owen: add the 0.3 to settings
        intakeMotorTwo.set(TalonSRXControlMode.PercentOutput, -Constants.Intake.motorOutput); //done
        }
    }

    static public boolean senseObj() { // boolean if using the if else in the function
        System.out.println(colorSensor.getProximity());
        if(colorSensor.getProximity() < 1000) { //CHANGE THE 1000 not actual number
            intakeMotorOne.set(TalonSRXControlMode.PercentOutput, Constants.Intake.motorStop);
            intakeMotorTwo.set(TalonSRXControlMode.PercentOutput, Constants.Intake.motorStop);
            return true;
        }
        else {
            return false;
        }
}

    //spits out the game piece
    public static void reverseEthanWheels(){
        intakeMotorOne.set(TalonSRXControlMode.PercentOutput, -Constants.Intake.motorOutput);
        intakeMotorTwo.set(TalonSRXControlMode.PercentOutput, Constants.Intake.motorOutput);
    }
    //THIS IS CAN SPARK MAX USE ABSOLUTE ENCODER!!! <-----IMPORTANT IMPORTANT IMPORTANT (for future ethan kim, others ignore)
    public static void foldInIntake(){
        double intakeFoldMotorEncoderValue = intakeFoldMotor.getSelectedSensorPosition();
        intakeFoldMotor = new TalonSRX(Constants.Intake.intakeFoldMotorNum);

        if (intakeFoldMotorEncoderValue > 87 ^ intakeFoldMotorEncoderValue < 93) {
            intakeFoldMotor.set(TalonSRXControlMode.PercentOutput, Constants.Intake.intakeFoldMotorOutput); //idk if it's negative or not so it's positive for now
        }
    }

    public static void foldOutIntake(){
        double intakeFoldMotorEncoderValue = intakeFoldMotor.getSelectedSensorPosition();
        intakeFoldMotor = new TalonSRX(Constants.Intake.intakeFoldMotorNum);

        if (intakeFoldMotorEncoderValue > -3 ^ intakeFoldMotorEncoderValue < 3){
            intakeFoldMotor.set(TalonSRXControlMode.PercentOutput, -Constants.Intake.intakeFoldMotorNum); // idk if this is negative or not too.
        }
    }

    public static boolean isIntakeFolded(){
        return(intakeFoldMotorEncoderValue > 87 ^ intakeFoldMotorEncoderValue < 93);
    }

    public static boolean isIntakeUnFolded(){
        return(intakeFoldMotorEncoderValue > -3 ^ intakeFoldMotorEncoderValue < 3);
    }
}
