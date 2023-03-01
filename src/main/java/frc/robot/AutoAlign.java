package frc.robot;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Sensors.AprilTagCameraWrapper;
import frc.robot.Sensors.LimelightCameraWrapper;

public class AutoAlign {
    private static PIDController aprilTagAlignXPID = new PIDController(Constants.AutoAlign.aprilTagAlignXkP, Constants.AutoAlign.aprilTagAlignXkI, Constants.AutoAlign.aprilTagAlignXkD); 
    private static PIDController limelightAlignXPID = new PIDController(Constants.AutoAlign.limelightAlignXkP, Constants.AutoAlign.limelightAlignXkI, Constants.AutoAlign.limelightAlignXkD);

    private static PIDController odometryAlignXPID = new PIDController(Constants.AutoAlign.odometryAlignXkP, Constants.AutoAlign.odometryAlignXkI, Constants.AutoAlign.odometryAlignXkD);
    private static PIDController odometryAlignYPID = new PIDController(Constants.AutoAlign.odometryAlignYkP, Constants.AutoAlign.odometryAlignYkI, Constants.AutoAlign.odometryAlignYkD);


    Drivebase drive = new Drivebase();

    public static boolean alignOdometry(Translation2d goalTranslation, double heading){

        SmartDashboard.putNumber("goal transaltion X: ", goalTranslation.getX());
        SmartDashboard.putNumber("goal transaltion Y: ", goalTranslation.getY());

        Pose2d pose = Drivebase.getPose();
        double poseX = pose.getX();
        double poseY = pose.getY();
        double goalPoseX = goalTranslation.getX();
        double goalPoseY = goalTranslation.getY();
        Double movementX = odometryAlignXPID.calculate(poseX, goalPoseX);
        Double movementY = odometryAlignYPID.calculate(poseY, goalPoseY);

        Translation2d translation = new Translation2d(Constants.isBlue()?-movementY:movementY, Constants.isBlue()?-movementX:movementX);

        Drivebase.driveFieldRelativeHeading(translation, heading);

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
            Drivebase.driveFieldRelativeHeading(new Translation2d(-movementY, 0), -180);
        }
        gridAlignY = Drivebase.getPose().getY();

        return AprilTagCameraWrapper.isAlignedToCubeNode();
    }
    public static boolean alignTape(){
        if (LimelightCameraWrapper.hasTargets()){
            double movementY = limelightAlignXPID.calculate(LimelightCameraWrapper.getHorizontalOffset(), 0);
            Drivebase.driveFieldRelativeHeading(new Translation2d(-movementY, 0), -180);
        }

        return LimelightCameraWrapper.isAlignedToConeNode();
    }
    public static void alignToPiece(){
        // double heading = (GamepieceCamera.getHorizontaloffset() * Constants.AutoAlign.kHorizontalOffsetToPidgeonFactor) + Gyro.getHeading();
        // Drivebase.driveFieldRelativeHeading(new Translation2d(0, 0), heading);
    }

    //the move forward function
    public static boolean moveIntoPosition() {
        return alignOdometry(new Translation2d(Constants.isBlue()? Constants.FieldPositions.atGridBlueX: Constants.FieldPositions.atGridRedX, 
                            gridAlignY), -180);
    }
    public static boolean isInCommunity(){
        if (((Constants.isBlue() && (Drivebase.getPose().getX() < Constants.FieldPositions.inBlueCommunityX)) ||
            (Constants.isRed() && (Drivebase.getPose().getX() > Constants.FieldPositions.inRedCommunityX))) &&
            (Drivebase.getPose().getY() < Constants.FieldPositions.inCommunityY)){
            return true;
        }
        return false;
    }
    public static boolean isInLoadingZone(){
        if (((Constants.isBlue() && (Drivebase.getPose().getX() > Constants.FieldPositions.inBlueLoadingZoneX)) ||
            (Constants.isRed() && (Drivebase.getPose().getX() < Constants.FieldPositions.inRedLoadingZoneX))) &&
            (Drivebase.getPose().getY() > Constants.FieldPositions.inLoadingZoneY)){
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
                    // if(alignOdometry(Constants.FieldPositions.AutoAlignPositions.blue1, -180)) {
                    if(alignOdometry(IO.keyInputOdometryPosition, -180)) {
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
            return alignOdometry(new Translation2d(Constants.isBlue()?Constants.FieldPositions.atGridBlueX:Constants.FieldPositions.atGridRedX, 
                                IO.keyInputOdometryPosition.getY()), -180);
        }
        return false;
    }
    public static double alignRotation = -180;
    public static Translation2d alignTranslation = new Translation2d(Constants.isBlue()?Constants.FieldPositions.atGridBlueX:Constants.FieldPositions.atGridRedX, IO.keyInputOdometryPosition.getY());
    public static boolean moveToGridPositionOdometryTwoStep(){
        if(isInCommunity()){
            switch(gridAlignState) {
                case AlignedToOdometry:
                    // if(alignOdometry(Constants.FieldPositions.AutoAlignPositions.red6, -180)) {
                    if(alignOdometry(IO.keyInputOdometryPosition, -180)) {
                        // gridAlignY = Drivebase.getPose().getY();
                        gridAlignState = GridAlignState.InPosition;
                        alignRotation = -180;
                        alignTranslation = new Translation2d(Constants.isBlue()?Constants.FieldPositions.atGridBlueX:Constants.FieldPositions.atGridRedX, IO.keyInputOdometryPosition.getY());
                    }
                break;
                case AlignedToNode:
                    return false;
                case InPosition:
                    // return alignOdometry(new Translation2d(Constants.isBlue()?Constants.FieldPositions.atGridBlueX:Constants.FieldPositions.atGridRedX, 
                    //             IO.keyInputOdometryPosition.getY()), -180);
                    alignRotation = alignRotation + IO.Drivebase.getSwerveRotation() *0.1;
                    // alignTranslation = new Translation2d(alignTranslation.getX() + (Constants.isBlue()?-IO.Drivebase.xBump() *0.1:IO.Drivebase.xBump() *0.1),
                    //                                         alignTranslation.getY());
                                        // +(Constants.isBlue()?-IO.Drivebase.getSwerveTranslation().getX() *0.5:IO.Drivebase.getSwerveTranslation().getX() *0.5)
                    if (!IO.isConeNodePosition){
                        alignOdometry(IO.keyInputOdometryPosition, alignRotation);
                        return true;
                    }
                    return alignOdometry(alignTranslation, alignRotation);
            }
        }
        return false;
    }
    public static enum SubstationAlignState {
        AlignedToOdometry,
        InPosition
    }
    public static SubstationAlignState substationAlignState = SubstationAlignState.AlignedToOdometry;
    public static boolean moveToSubstationPosition(){
        if(isInLoadingZone()){
            switch(substationAlignState) {
                case AlignedToOdometry:
                    if (IO.keyInputSubstationPosition == Constants.FieldPositions.AutoAlignPositions.blueSingleSubstation || 
                        IO.keyInputSubstationPosition == Constants.FieldPositions.AutoAlignPositions.redSingleSubstation){
                        if(alignOdometry(IO.keyInputSubstationPosition, 90) && GamepieceManager.runExtention()) {
                            return true;
                        }
                    }
                    else if(alignOdometry(IO.keyInputSubstationPosition, 0) && GamepieceManager.runExtention()) {
                        substationAlignState = SubstationAlignState.InPosition;
                    }
                    break;
                case InPosition:
                    return alignOdometry(new Translation2d(Constants.isBlue()?Constants.FieldPositions.atSubstationBlueX:Constants.FieldPositions.atSubstationRedX, 
                                            IO.keyInputSubstationPosition.getY()), 0);
            }
        }
        return false;
        
    }
    public static void displayInformation(){
        SmartDashboard.putBoolean("Is in Community?", isInCommunity());
        SmartDashboard.putBoolean("Is in Loading Zone?", isInLoadingZone());

        SmartDashboard.putString("AutoAlign.gridAlignState:", gridAlignState.toString());
        SmartDashboard.putString("AutoAlign.substationAlignStateSave", substationAlignState.toString());


    }
}
