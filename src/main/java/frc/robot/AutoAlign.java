package frc.robot;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import frc.robot.Sensors.AprilTagCameraWrapper;
import frc.robot.Sensors.LimelightCameraWrapper;

public class AutoAlign {
    PIDController PIDOdometeryAlignX = new PIDController(1, 0, 0);
    PIDController PIDOdometeryAlignY = new PIDController(1, 0, 0);

    Drivebase drive = new Drivebase();

    public boolean alignOdometry(Pose2d goalPose){
        Pose2d pose = Drivebase.getPose();
        double poseX = pose.getX();
        double poseY = pose.getY();
        double goalPoseX = goalPose.getX();
        double goalPoseY = goalPose.getY();
    
        Double movementX = PIDOdometeryAlignX.calculate(poseX, goalPoseX);
        Double movementY = PIDOdometeryAlignX.calculate(poseY, goalPoseY);
        Translation2d translation = new Translation2d(movementX, movementY); 
        Drivebase.driveFieldRelativeHeading(translation, 180);

        if (Math.abs(goalPoseX - poseX) < 0.1 && Math.abs(goalPoseY - poseY) < 0.1) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean alignAprilTag(){
        double horizontalOffset = AprilTagCameraWrapper.getHorizontalOffset();
    
        Double movementY = PIDOdometeryAlignX.calculate(horizontalOffset, 0);
        Translation2d translation = new Translation2d(0, movementY); 
        Drivebase.driveFieldRelativeHeading(translation, 180);

        if (LimelightCameraWrapper.isAlignedToConeNode()) {
            return true;
        }
        else {
            return false;
        }
    }

    public void moveMeter() {
        //the move forward function
    }
    void moveToGridPosition(){
        //the code to do the entire align sequence
    }
}