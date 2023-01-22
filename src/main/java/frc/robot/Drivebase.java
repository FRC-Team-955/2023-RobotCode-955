package frc.robot;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import frc.robot.Swerve.SwerveDrive;
import frc.robot.Sensors.Gyro;


public class Drivebase {
    
    public static PIDController pid = new PIDController(Constants.kP, Constants.kI, Constants.kD);
    public static double currentAngle = 34; // get from gyroscope

    private static SwerveDrive drive = new SwerveDrive();

    public static void driveFieldRelative(){
        Pose2d pose = drive.getPose();
        double heading = 0;
        if (Constants.isBlue() && pose.getX() < Constants.FieldPositions.centerLine){
            heading = 180;
        }else if(Constants.isRed() && pose.getX() > Constants.FieldPositions.centerLine){
            heading = 180;
        }
        driveFieldRelativeHeading(IO.getSwerveTranslation(), heading);
    }

    public static void driveFieldRelativeRotation(Translation2d translation, double rotation){
        drive.drive(translation, rotation, true, false, false, 0);
    }

    public static void driveFieldRelativeHeading(Translation2d translation, double heading){
        drive.drive(translation, 0, true, false, true, heading);
    }

    public static void driveRobotRelative(){

    }
    public static void driveRobotRelative(Translation2d translation, double rotation){
        drive.drive(translation, rotation, false, false, false, 0);
    }

    public static void autoBalance() {

       double output = MathUtil.clamp(pid.calculate(Gyro.getPitch(), 0), -1 ,1);

       driveFieldRelativeRotation(new Translation2d(output, 0), 0);
       
       if(isBalanced()){
        driveFieldRelativeRotation(new Translation2d(0, 0), output);
       }
    }
    
    public static boolean isBalanced() {
        if (2.5 > Gyro.getPitch() && Gyro.getPitch() > -2.5) {
            return true;
        } else {
            return false;
        }
    }

    public static Pose2d getPose() {
       return drive.getPose();
    }
}

