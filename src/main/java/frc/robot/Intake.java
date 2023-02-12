package frc.robot;

import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.Timer;
// import edu.wpi.first.wpilibj.DriverStation;
// import edu.wpi.first.wpilibj2.command.button.JoystickButton;

import com.revrobotics.CANSparkMax;
import com.revrobotics.ColorSensorV3;
import com.revrobotics.SparkMaxAbsoluteEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.SparkMaxAbsoluteEncoder.Type;

public class Intake {;
    static TalonSRX intakeMotorOne = new TalonSRX(Constants.Intake.motorOneNum);
    static TalonSRX intakeMotorTwo = new TalonSRX(Constants.Intake.motorTwoNum);
    static ColorSensorV3 colorSensor = new ColorSensorV3(Port.kOnboard);
    static CANSparkMax intakeFoldMotor = new CANSparkMax(Constants.Intake.intakeFoldMotorNum, MotorType.kBrushless);
    //THIS IS CAN SPARK MAX, THE INTAKE FOLD THING
    static SparkMaxAbsoluteEncoder intakeFoldMotorEncoderValue = intakeFoldMotor.getAbsoluteEncoder(Type.kDutyCycle); //this is here because 2 functions need it at the same time

    public static void setup(){
        intakeFoldMotor.setIdleMode(IdleMode.kBrake);
    }
   
    static Timer timer = new Timer();
    
    public static void startIntake(){
        timer.reset();
        timer.start();
    }
    //sucks in the game piece and stop
    public static void runEthanWheels(){
        if(timer.get() < 4){
            intakeMotorOne.set(TalonSRXControlMode.PercentOutput, Constants.Intake.motorOutput); // note from owen: add the 0.3 to settings
            intakeMotorTwo.set(TalonSRXControlMode.PercentOutput, -Constants.Intake.motorOutput); //done
        }
        else if(timer.get() > 3){
            intakeMotorOne.set(TalonSRXControlMode.PercentOutput, Constants.Intake.intakeMotorStop);
            intakeMotorTwo.set(TalonSRXControlMode.PercentOutput, -Constants.Intake.intakeMotorStop);
        }
    }

    static public boolean senseObj() { // boolean if using the if else in the function
        System.out.println(colorSensor.getProximity());
        if(colorSensor.getProximity() < 1000) { //CHANGE THE 1000 not actual number
            intakeMotorOne.set(TalonSRXControlMode.PercentOutput, Constants.Intake.intakeMotorStop);
            intakeMotorTwo.set(TalonSRXControlMode.PercentOutput, Constants.Intake.intakeMotorStop);
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

    //THIS IS CAN SPARK MAX USE ABSOLUTE ENCODER!!! <-----IMPORTANT (for future ethan kim, others ignore)
    public static void foldInIntake(){
        if (intakeFoldMotorEncoderValue.getPosition() < 88) {
            intakeFoldMotor.set(Constants.Intake.intakeFoldMotorOutput); //idk if it's negative or not so it's positive for now
        }
        else{
            intakeFoldMotor.set(Constants.Intake.intakeMotorStop); // idk if this is negative or not too.
        }
        //fold in ethan wheels
        //CHANGE ALL ENCODER VALUES WHEN TESTING!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //This is for when ethan wheel 1 is too much to one direction
        if (intakeFoldMotorEncoderValue.getPosition() > 80 && intakeMotorOne.getSelectedSensorPosition() < 90){ //idk what this 90 value is, change later
            intakeMotorOne.set(TalonSRXControlMode.PercentOutput, Constants.Intake.ethanWheelFoldSlow); //CHANGE THIS TO - OR not - NOT DECIDED YET
        }
        //This is for when ethan wheel 1 is too much to other direction
        else if (intakeFoldMotorEncoderValue.getPosition() > 80 && intakeMotorOne.getSelectedSensorPosition() > 90){ //idk what this 90 value is, change later
            intakeMotorOne.set(TalonSRXControlMode.PercentOutput, -Constants.Intake.ethanWheelFoldSlow); //CHANGE THIS TO - OR not - NOT DECIDED YET
        }
        //This is for when ehtan wheel 1 needs to stop
        else if (intakeMotorOne.getSelectedSensorPosition() > 88 && intakeMotorOne.getSelectedSensorPosition() < 92 && intakeMotorOne.getSelectedSensorPosition() < 88){ //idk what this 90 value is, change later
            intakeMotorOne.set(TalonSRXControlMode.PercentOutput, Constants.Intake.intakeMotorStop);
        }
        //This is for when ethan wheel 2 is too much to one direction
        if (intakeFoldMotorEncoderValue.getPosition() > 80 && intakeMotorTwo.getSelectedSensorPosition() < 90){ //idk what this 90 value is, change later
            intakeMotorTwo.set(TalonSRXControlMode.PercentOutput, Constants.Intake.ethanWheelFoldSlow); //CHANGE THIS TO - OR not - NOT DECIDED YET
        }
        //This is for when ethan wheel 2 is too much to other direction
        else if (intakeFoldMotorEncoderValue.getPosition() > 80 && intakeMotorTwo.getSelectedSensorPosition() > 90){ //idk what this 90 value is, change later
            intakeMotorTwo.set(TalonSRXControlMode.PercentOutput, -Constants.Intake.ethanWheelFoldSlow); //CHANGE THIS TO - OR not - NOT DECIDED YET
        }
        //This is for when ehtan wheel 2 needs to stop
        else if (intakeMotorTwo.getSelectedSensorPosition() > 88 && intakeMotorOne.getSelectedSensorPosition() < 92){ //idk what this 90 value is, change later
            intakeMotorTwo.set(TalonSRXControlMode.PercentOutput, Constants.Intake.intakeMotorStop); //CHANGE THIS TO - OR not - NOT KNOWN YET
        }
    }
            
    public static void foldOutIntake(){
        SparkMaxAbsoluteEncoder intakeFoldMotorEncoderValue = intakeFoldMotor.getAbsoluteEncoder(Type.kDutyCycle);
        intakeFoldMotor = new CANSparkMax(Constants.Intake.intakeFoldMotorNum, MotorType.kBrushless);

        if (intakeFoldMotorEncoderValue.getPosition() > 2){
            intakeFoldMotor.set(-Constants.Intake.intakeFoldMotorOutput); // idk if this is negative or not too.
        }
        else{
            intakeFoldMotor.set(Constants.Intake.intakeMotorStop);
        }
    }
      
    public static boolean isIntakeFolded(){
        if (intakeFoldMotorEncoderValue.getPosition() < 87){
            return true;
        }
        else {
            return false;
        }
    }

    public static boolean isIntakeUnFolded(){
        if (intakeFoldMotorEncoderValue.getPosition() > 3) {
            return true;
        }
        else {
            return false;
        }
    }
}
