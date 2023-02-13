package frc.robot;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import frc.robot.Sensors.AprilTagCameraWrapper;
import frc.robot.Sensors.LimelightCameraWrapper;

public class AutoAlign {
    private static PIDController aprilTagAlignXPID = new PIDController(Constants.AutoAlign.aprilTagAlignXkP, Constants.AutoAlign.aprilTagAlignXkI, Constants.AutoAlign.aprilTagAlignXkD); 
    private static PIDController limelightAlignXPID = new PIDController(Constants.AutoAlign.limelightAlignXkP, Constants.AutoAlign.limelightAlignXkI, Constants.AutoAlign.limelightAlignXkD);

    private static PIDController odometryAlignXPID = new PIDController(Constants.AutoAlign.odometryAlignXkP, Constants.AutoAlign.odometryAlignXkI, Constants.AutoAlign.odometryAlignXkD);
    private static PIDController odometryAlignYPID = new PIDController(Constants.AutoAlign.odometryAlignYkP, Constants.AutoAlign.odometryAlignYkI, Constants.AutoAlign.odometryAlignYkD);


    Drivebase drive = new Drivebase();

    public static boolean alignOdometry(Translation2d goalTranslation){
        System.out.println("Goal POSE: " + goalTranslation);
        Pose2d pose = Drivebase.getPose();
        double poseX = pose.getX();
        double poseY = pose.getY();
        double goalPoseX = goalTranslation.getX();
        double goalPoseY = goalTranslation.getY();
        Double movementX = odometryAlignXPID.calculate(poseX, goalPoseX);
        Double movementY = odometryAlignYPID.calculate(poseY, goalPoseY);

        Translation2d translation = new Translation2d(Constants.isBlue()?-movementY:movementY, Constants.isBlue()?-movementX:movementX);

        Drivebase.driveFieldRelativeHeading(translation, 180);

        if (Math.abs(goalPoseX - poseX) < Constants.AutoAlign.alignTolerance && Math.abs(goalPoseY - poseY) < Constants.AutoAlign.alignTolerance) {
            return true;
        }
        else {
            return false;
        }
    }

    public static boolean alignAprilTag(){
        if(AprilTagCameraWrapper.hasTargets()){
            double movementY = aprilTagAlignXPID.calculate(AprilTagCameraWrapper.getHorizontalOffset(), 0);
            Drivebase.driveFieldRelativeHeading(new Translation2d(-movementY, 0), 180);
        }
        gridAlignY = Drivebase.getPose().getY();

        return AprilTagCameraWrapper.isAlignedToCubeNode();
    }
    public static boolean alignTape(){
        if (LimelightCameraWrapper.hasTargets()){
            double movementY = limelightAlignXPID.calculate(LimelightCameraWrapper.getHorizontalOffset(), 0);
            Drivebase.driveFieldRelativeHeading(new Translation2d(-movementY, 0), 180);
        }

        return LimelightCameraWrapper.isAlignedToConeNode();
    }
    public static void alignToPiece(){
        // double heading = (GamepieceCamera.getHorizontaloffset() * Constants.AutoAlign.kHorizontalOffsetToPidgeonFactor) + Gyro.getHeading();
        // Drivebase.driveFieldRelativeHeading(new Translation2d(0, 0), heading);
    }

    public static boolean moveIntoPosition() {
        System.out.println("keyInputOdometryPosition: " + IO.keyInputOdometryPosition);

        return alignOdometry(new Translation2d(Constants.isBlue()? Constants.FieldPositions.atGridBlueX: Constants.FieldPositions.atGridRedX, 
                            gridAlignY));
        //the move forward function
    }
    public static boolean isInCommunity(){
        if (((Constants.isBlue() && Drivebase.getPose().getX() < Constants.FieldPositions.inBlueCommunityX) ||
            (Constants.isRed() && Drivebase.getPose().getX() > Constants.FieldPositions.inRedCommunityX)) &&
            Drivebase.getPose().getY() < Constants.FieldPositions.inCommunityY){
            return true;
        }
        return false;
    }
    public static boolean isInLoadingZong(){
        if (((Constants.isBlue() && Drivebase.getPose().getX() < Constants.FieldPositions.inBlueCommunityX) ||
            (Constants.isRed() && Drivebase.getPose().getX() > Constants.FieldPositions.inRedCommunityX)) &&
            Drivebase.getPose().getY() > Constants.FieldPositions.inCommunityY){
            return true;
        }
        return false;
    }
    public static enum GridAlignState {
        AlignedToOdometry,
        AlignedToNode,
        InPosition
    }
    public static GridAlignState gridAlignState = GridAlignState.AlignedToOdometry;
    public static double gridAlignY;
    public static boolean moveToGridPosition(){
        if(isInCommunity()){
            switch(gridAlignState) {
                case AlignedToOdometry:
                    // if(alignOdometry(IO.keyInputOdometryPosition)) {
                    if(alignOdometry(Constants.FieldPositions.AutoAlignPositions.blue1)) {
                        gridAlignState = GridAlignState.AlignedToNode;
                    }
                    break;
                case AlignedToNode:
                    if(IO.isConeNodePosition) {
                        if(alignTape()) {
                            gridAlignY = Drivebase.getPose().getY();
                            gridAlignState = GridAlignState.InPosition;
                        }
                    } else {
                        if(alignAprilTag()) {
                            gridAlignY = Drivebase.getPose().getY();
                            gridAlignState = GridAlignState.InPosition;
                        }
                    }
                    break;
                case InPosition:
                    return moveIntoPosition();
            }
        }
        return false;
    }
    public static boolean moveToGridPositionOdometry(){
        if(isInCommunity()){
            return alignOdometry(new Translation2d(Constants.isBlue()?Constants.FieldPositions.atGridBlueX:Constants.FieldPositions.atGridRedX, IO.keyInputOdometryPosition.getY()));
        }
        return false;
    }
}