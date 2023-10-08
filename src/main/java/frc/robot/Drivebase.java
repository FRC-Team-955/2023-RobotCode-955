package frc.robot;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Sensors.Gyro;
import frc.robot.Subsystems.Claw;
import frc.robot.Swerve.SwerveDrive;


public class Drivebase {
    
    public static PIDController autoBalancePID = new PIDController(Constants.Drivebase.autoBalancekP, Constants.Drivebase.autoBalancekI, Constants.Drivebase.autoBalancekD);
    public static double currentAngle = 34; // get from gyroscope
    

    public static SwerveDrive drive = new SwerveDrive();

    public static void updateSwerveOdometry(){
        drive.updateSwerveOdometry();
    }
    
    public static void updateSwerveOdometryNoVision(){
        drive.updateSwerveOdometryNoVision();
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
        driveFieldRelativeHeading(IO.Drivebase.getSwerveTranslation(), heading, true);
    }

    public static Translation2d drivingAdjustment(){
        return new Translation2d(IO.Drivebase.shiftRight()?0.4:(IO.Drivebase.shiftLeft()?-0.4:0), GamepieceManager.moveBack?-1.2:0);
    }
    public static double headingSetPointSave = SwerveDrive.headingSetPoint;

    public static void drive() {
        if (IO.Drivebase.isAutoHeadingActive()) {
            Drivebase.driveFieldRelative();
        } else if(IO.Drivebase.isRobotRelativeActive()){
            Gyro.set(90);
            SwerveDrive.headingSetPoint = -180;
            Drivebase.driveRobotRelativeRotation(IO.Drivebase.getSwerveTranslation(), IO.Drivebase.getSwerveRotation());
        }else if (IO.Drivebase.robotForwardMove()){
            Drivebase.driveRobotRelativeRotation(new Translation2d(0,-1.5), 0);
            Claw.intakeGamePiece();
        }
        else {
            headingSetPointSave = SwerveDrive.headingSetPoint;
            Drivebase.driveFieldRelativeRotation(IO.Drivebase.getSwerveTranslation().plus(drivingAdjustment()), IO.Drivebase.getSwerveRotation(), false , true);
        }
    }

    public static void driveFieldRelativeRotation(Translation2d translation, double rotation, boolean isOpenLoopHeading, boolean isOpenLoopDrive){
        drive.drive(translation, rotation, true, isOpenLoopHeading, isOpenLoopDrive, false, 0);
    }

    public static void driveFieldRelativeHeading(Translation2d translation, double heading, boolean isOpenLoopDrive){

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

        drive.drive(translation, 0, true, false, false, true, heading);
    }

    public static void driveRobotRelativeRotation(Translation2d translation, double rotation){
        drive.drive(new Translation2d(translation.getY(), -translation.getX()), rotation, false, false,false, false, 0);
    }

    public static void autoBalance() {

       double output = MathUtil.clamp(autoBalancePID.calculate(Gyro.getRoll(), 0), -0.5,0.5);

    //    driveFieldRelativeRotation(new Translation2d(0, -output), 0);
       
       if(isBalanced()){
        // Claw.outputGamePieceFast();
        // driveRobotRelativeRotation(new Translation2d(output,0 ), 0);

        driveFieldRelativeRotation(new Translation2d(0, 0), 0, false, false);
       }else{
            driveRobotRelativeRotation(new Translation2d(0,-output ), 0);
       }
    }
    
    private static double lastPitch = Gyro.getPitch();
    
    public static boolean isBalanced() {
        if (7 > Gyro.getRoll() && Gyro.getRoll() > -7) {
            return true;
        } else {
            return false;
        }
    }

    public static Pose2d getPose() {
       return drive.getPose();
    }
    public static Pose2d getEstimatedVisionPose(){
        return drive.getEstimatedVisionPose();
    }
    public static void displayInformation(){
        SmartDashboard.putBoolean("Adding data",drive.displayInformation());
    }
}

