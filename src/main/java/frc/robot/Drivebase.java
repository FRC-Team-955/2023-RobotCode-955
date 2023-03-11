package frc.robot;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Sensors.Gyro;
import frc.robot.Swerve.SwerveDrive;


public class Drivebase {
    
    public static PIDController autoBalancePID = new PIDController(Constants.Drivebase.autoBalancekP, Constants.Drivebase.autoBalancekI, Constants.Drivebase.autoBalancekD);
    public static double currentAngle = 34; // get from gyroscope
    

    public static SwerveDrive drive = new SwerveDrive();

    public static void updateSwerveOdometry(){
        drive.updateSwerveOdometry();
    }
    public static void setSwerveOdometry(Pose2d pose){
        drive.resetOdometry(pose);
    }

    public static void resetAnglesToAbsolute() {
        drive.resetAnglesToAbsolute();
    }

    public static void driveFieldRelative(){
        Pose2d pose = drive.getPose();

        double heading = 0;
        if (Constants.isBlue() && pose.getX() < Constants.FieldPositions.centerLine){
            heading = 180;
        }else if(Constants.isRed() && pose.getX() > Constants.FieldPositions.centerLine){
            heading = 180;
        }
        driveFieldRelativeHeading(IO.Drivebase.getSwerveTranslation(), heading);
    }
    public static double headingSetPointSave = SwerveDrive.headingSetPoint;

    public static void drive() {
        if (IO.Drivebase.isAutoHeadingActive()) {
            Drivebase.driveFieldRelative();
        } else if(IO.Drivebase.isRobotRelativeActive()){
            Drivebase.driveRobotRelativeRotation(IO.Drivebase.getSwerveTranslation(), IO.Drivebase.getSwerveRotation());
        }else {
            headingSetPointSave = SwerveDrive.headingSetPoint;
            Drivebase.driveFieldRelativeRotation(IO.Drivebase.getSwerveTranslation(), IO.Drivebase.getSwerveRotation());
        }
    }

    public static void driveFieldRelativeRotation(Translation2d translation, double rotation){
        drive.drive(translation, rotation, true, false, false, 0);
    }
    public static void driveFieldRelativeHeading(Translation2d translation, double heading){

        double setpoint = Drivebase.headingSetPointSave % 360;
        if (setpoint<0){
            setpoint = setpoint+360;
        }
        double diff= setpoint - heading;
        if (diff>180){
            diff = diff - 360;
        }else if (diff<-180){
            diff = diff + 360;
        }
        //Positive is clockwise, negitive is counterclockwises
        double headingOffset = 0;
        heading = Drivebase.headingSetPointSave - diff + headingOffset;

        drive.drive(translation, 0, true, false, true, heading);
    }

    public static void driveRobotRelativeRotation(Translation2d translation, double rotation){
        drive.drive(translation, rotation, false, false, false, 0);
    }
    public static void driveRobotRelativeHeading(Translation2d translation, double heading){

    }

    public static void logData() {
        drive.logSwerve();
    }

    public static void autoBalance() {

       double output = MathUtil.clamp(autoBalancePID.calculate(Gyro.getRoll(), 0), -0.2 ,0.2);

        driveRobotRelativeRotation(new Translation2d(output,0 ), 0);
    //    driveFieldRelativeRotation(new Translation2d(0, -output), 0);
       
       if(isBalanced()){
        // driveRobotRelativeRotation(new Translation2d(output,0 ), 0);

        driveFieldRelativeRotation(new Translation2d(0, 0), 0);
       }
    }
    
    private static double lastPitch = Gyro.getPitch();
    
    public static void autoBalanceBangBang() {
        double newPitch = Gyro.getPitch();
        double heading = 0;
        if (heading > 90)
            heading = 0;
        else if (heading  >= 90 && heading < 180 || heading >= 180 && heading < 270) {
            heading = 180;
        }
        else {
            heading = 0;
        }
        
        if (Gyro.getPitch() > 2.5 || Gyro.getPitch() < 2.5) {
            if (Math.abs(newPitch - lastPitch) > 0.5) {
                driveFieldRelativeHeading(Constants.Drivebase.autoBalanceStop, heading);
            }
            else {
                if (Gyro.getPitch() > 2.5) {
                    driveFieldRelativeHeading(Constants.Drivebase.autoBalanceForward, heading);
                }
                else if (Gyro.getPitch() < 2.5) {
                    driveFieldRelativeHeading(Constants.Drivebase.autoBalanceBackward, heading);
    
                }
            }
        }
        else {
            driveFieldRelativeHeading(Constants.Drivebase.autoBalanceStop, heading);
        }
        lastPitch = newPitch;
    }
    
    public static boolean isBalanced() {
        if (4 > Gyro.getRoll() && Gyro.getRoll() > -4) {
            return true;
        } else {
            return false;
        }
    }

    public static Pose2d getPose() {
       return drive.getPose();
    }
    public static void displayInformation(){
        SmartDashboard.putBoolean("Is balanced", Drivebase.isBalanced());
    }
}

