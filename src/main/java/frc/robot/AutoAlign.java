package frc.robot;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import frc.robot.Sensors.AprilTagCameraWrapper;
import frc.robot.Sensors.LimelightCameraWrapper;

public class AutoAlign {
    private static PIDController PIDOdometeryAlignX = new PIDController(0.7, 0, 0);
    private static PIDController PIDOdometeryAlignY = new PIDController(0.7, 0, 0);

    Drivebase drive = new Drivebase();

    public static boolean alignOdometry(Translation2d goalTranslation){
        Pose2d pose = Drivebase.getPose();
        double poseX = pose.getX();
        double poseY = pose.getY();
        double goalPoseX = goalTranslation.getX();
        double goalPoseY = goalTranslation.getY();
    
        Double movementX = PIDOdometeryAlignX.calculate(poseX, goalPoseX);
        Double movementY = PIDOdometeryAlignX.calculate(poseY, goalPoseY);
        Translation2d translation = new Translation2d(Constants.isBlue()?movementY:-movementY, Constants.isBlue()?movementX:-movementX); 
        Drivebase.driveFieldRelativeHeading(translation, 180);

        if (Math.abs(goalPoseX - poseX) < 0.1 && Math.abs(goalPoseY - poseY) < 0.1) {
            return true;
        }
        else {
            return false;
        }
    }

    public static boolean alignAprilTag(){
        if(AprilTagCameraWrapper.hasTargets()){
            double movementY = PIDOdometeryAlignX.calculate(AprilTagCameraWrapper.getHorizontalOffset(), 0);
            Drivebase.driveFieldRelativeHeading(new Translation2d(-movementY, 0), 180);
        }

        if (AprilTagCameraWrapper.isAlignedToCubeNode()) {
            return true;
        }
        else {
            return false;
        }
    }
    public static boolean alignTape(){
        if (LimelightCameraWrapper.hasTargets()){
            double movementY = PIDOdometeryAlignX.calculate(LimelightCameraWrapper.getHorizontalOffset(), 0);
            Drivebase.driveFieldRelativeHeading(new Translation2d(-movementY, 0), 180);
        }

        if (LimelightCameraWrapper.isAlignedToConeNode()){
            return true;
        }
        else {
            return false;
        }
    }
    public static void alignToPiece(){
        // double heading = (GamepieceCamera.getHorizontaloffset() * Constants.AutoAlign.kHorizontalOffsetToPidgeonFactor) + Gyro.getHeading();
        // Drivebase.driveFieldRelativeHeading(new Translation2d(0, 0), heading);
    }

    public static boolean moveIntoPosition() {
        return alignOdometry(new Translation2d(Constants.isBlue()? Constants.FieldPositions.atGridBlueX: Constants.FieldPositions.atGridRedX, 
                            gridAlignY));
        //the move forward function
    }
    public static boolean isInCommunity(){
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
        //REMEMBER TO RESET THE STATE BACK TO AlignedToOdometry AT SOME POINT
        if(isInCommunity()){
            if(gridAlignState == GridAlignState.AlignedToOdometry){
                if(alignOdometry(IO.keyInputOdometryPosition)){
                    gridAlignState = GridAlignState.AlignedToNode;
                }
            }else if (gridAlignState == GridAlignState.AlignedToNode){
                if(IO.isConeNodePosition){
                    if(alignTape()){
                        gridAlignY = Drivebase.getPose().getY();
                        gridAlignState = GridAlignState.InPosition;
                    }
                }else{
                    if(alignAprilTag()){
                        gridAlignY = Drivebase.getPose().getY();
                        gridAlignState = GridAlignState.InPosition;
                    }
                }
            }else if (gridAlignState == GridAlignState.InPosition){
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