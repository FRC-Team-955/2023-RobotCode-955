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
    private static PIDController gamePieceAlignXPID = new PIDController(Constants.AutoAlign.limelightAlignXkP, Constants.AutoAlign.limelightAlignXkI, Constants.AutoAlign.limelightAlignXkD);

    private static PIDController odometryAlignXPID = new PIDController(Constants.AutoAlign.odometryAlignXkP, Constants.AutoAlign.odometryAlignXkI, Constants.AutoAlign.odometryAlignXkD);
    private static PIDController odometryAlignYPID = new PIDController(Constants.AutoAlign.odometryAlignYkP, Constants.AutoAlign.odometryAlignYkI, Constants.AutoAlign.odometryAlignYkD);


    Drivebase drive = new Drivebase();

    public static boolean alignOdometry(Translation2d goalTranslation, double heading){
        Pose2d pose = Drivebase.getPose();
        double poseX = pose.getX();
        double poseY = pose.getY();
        double goalPoseX = goalTranslation.getX();
        double goalPoseY = goalTranslation.getY();
        Double movementX = odometryAlignXPID.calculate(poseX, goalPoseX);
        Double movementY = odometryAlignYPID.calculate(poseY, goalPoseY);

        Translation2d translation = new Translation2d(Constants.isBlue()?-movementY:movementY, Constants.isBlue()?-movementX:movementX);

        Drivebase.driveFieldRelativeHeading(translation, heading);

        return (Math.abs(goalPoseX - poseX) < Constants.AutoAlign.alignTolerance && Math.abs(goalPoseY - poseY) < Constants.AutoAlign.alignTolerance);
     
    }
    public static boolean alignTranslation(Translation2d goalTranslation){
        Pose2d pose = Drivebase.getPose();
        double poseX = pose.getX();
        double poseY = pose.getY();
        double goalPoseX = goalTranslation.getX();
        double goalPoseY = goalTranslation.getY();
        Double movementX = odometryAlignXPID.calculate(poseX, goalPoseX);
        Double movementY = odometryAlignYPID.calculate(poseY, goalPoseY);

        Translation2d translation = new Translation2d(Constants.isBlue()?-movementY:movementY, Constants.isBlue()?-movementX:movementX);

        Drivebase.driveFieldRelativeRotation(translation, IO.Drivebase.getSwerveRotation() *0.05, true);

        return (Math.abs(goalPoseX - poseX) < Constants.AutoAlign.alignTranslationX && Math.abs(goalPoseY - poseY) < Constants.AutoAlign.alignTranslationX);
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
    // public static void alignToPiece(){
    //     if(LimelightCameraWrapper.hasTargets()){
    //         double movementY = gamePieceAlignXPID.calculate(LimelightCameraWrapper.getHorizontalOffset(), Constants);
    //         Drivebase.driveFieldRelativeHeading(new Translation2d(-movementY, 0), -180);
    //     }
    //     // double heading = (GamepieceCamera.getHorizontaloffset() * Constants.AutoAlign.kHorizontalOffsetToPidgeonFactor) + Gyro.getHeading();
    //     // Drivebase.driveFieldRelativeHeading(new Translation2d(0, 0), heading);
    // }
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
    public static boolean isInCorrectLoadingZone(){
        if(Constants.isBlue()){
            if (IO.keyInputSubstationPosition.getY() + IO.keyInputSubstationPosition.getX()*Constants.FieldPositions.AutoAlignPositions.oneStepDoubleSubstationSlope - Constants.FieldPositions.AutoAlignPositions.oneStepDoubleSubstationSlope * Drivebase.getPose().getX() > Drivebase.getPose().getY() && 
                IO.keyInputSubstationPosition.getY() - IO.keyInputSubstationPosition.getX()*Constants.FieldPositions.AutoAlignPositions.oneStepDoubleSubstationSlope + Constants.FieldPositions.AutoAlignPositions.oneStepDoubleSubstationSlope * Drivebase.getPose().getX() < Drivebase.getPose().getY() &&
                Drivebase.getPose().getX() > Constants.FieldPositions.inBlueLoadingZoneX){
                    return true;
            }

        }else{
            if (IO.keyInputSubstationPosition.getY() + IO.keyInputSubstationPosition.getX()*Constants.FieldPositions.AutoAlignPositions.oneStepDoubleSubstationSlope - Constants.FieldPositions.AutoAlignPositions.oneStepDoubleSubstationSlope * Drivebase.getPose().getX() < Drivebase.getPose().getY() && 
                IO.keyInputSubstationPosition.getY() - IO.keyInputSubstationPosition.getX()*Constants.FieldPositions.AutoAlignPositions.oneStepDoubleSubstationSlope + Constants.FieldPositions.AutoAlignPositions.oneStepDoubleSubstationSlope * Drivebase.getPose().getX() > Drivebase.getPose().getY() &&
                Drivebase.getPose().getX() < Constants.FieldPositions.inRedLoadingZoneX){
                    return true;
            }

        }
        return false;
    }
    public static enum GridAlignState {
        AlignedToOdometry,
        InPosition
    }
    public static GridAlignState gridAlignState = GridAlignState.AlignedToOdometry;
    public static double gridAlignY;
    
    public static double alignRotation = -180;
    public static double alignTranslationY = 0;
    public static boolean moveToGridPositionOdometryTwoStep(){
        if(isInCommunity()){
            switch(gridAlignState) {
                case AlignedToOdometry:
                    //if hybrid node then noHitGridOffset
                    if(IO.gridNodeType == IO.GridNodeType.Hybrid){
                        if(alignOdometry(IO.keyInputOdometryPosition.plus(new Translation2d(Constants.isBlue()?Constants.Auto.noHitGridOffset:-Constants.Auto.noHitGridOffset,0)), -180)){
                            gridAlignState = GridAlignState.InPosition;
                            //Set rotation to -180 here so that you can adjust it manunally later if needed
                            alignRotation = -180;
                            alignTranslationY = 0;
                        }
                    }
                    //cone and cube nodes should go to normal offset
                    else{
                        if(alignOdometry(IO.keyInputOdometryPosition, -180)) {
                            gridAlignState = GridAlignState.InPosition;
                            //Set rotation to -180 here so that you can adjust it manunally later if needed
                            alignRotation = -180;
                            alignTranslationY = 0;
                        }
                    }
                break;
                case InPosition:
                    alignRotation = alignRotation + IO.Drivebase.getSwerveRotation() *0.05;
                    alignTranslationY = alignTranslationY + IO.Drivebase.getSwerveTranslation().getX() * (Constants.isBlue()?Constants.Drivebase.turnRate:-Constants.Drivebase.turnRate);
                    switch(IO.gridNodeType){
                        //If Hybrid, don't move from noHit position
                        case Hybrid:
                            alignOdometry(IO.keyInputOdometryPosition.plus(new Translation2d(Constants.isBlue()?Constants.Auto.noHitGridOffset:-Constants.Auto.noHitGridOffset,0)), alignRotation);
                            return true;
                        //If Cube, don't move from normal position
                        case Cube:
                            alignOdometry(IO.keyInputOdometryPosition, alignRotation);
                            return true;
                        case Cone:
                            switch(IO.gridRow){
                                //Not possible
                                case Hybrid:
                                    break;
                                //If Cone at Mid, don't move from normal position
                                case Mid:
                                    alignOdometry(IO.keyInputOdometryPosition, alignRotation);
                                    return true;
                                //If Cone at High, move forward
                                case High:
                                    // return alignOdometry(new Translation2d(Constants.isBlue()?Constants.FieldPositions.atGridBlueX:Constants.FieldPositions.atGridRedX, IO.keyInputOdometryPosition.getY()), alignRotation);
                                    return alignTranslation(new Translation2d(Constants.isBlue()?Constants.FieldPositions.atGridBlueX:Constants.FieldPositions.atGridRedX, IO.keyInputOdometryPosition.getY()));
                            }
                            break;
                    }
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
                    // else if(isInCorrectLoadingZone()){
                    //     substationAlignState = SubstationAlignState.InPosition;
                    // }
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

        // SmartDashboard.putString("GridAlignState", gridAlignState.toString());
        // SmartDashboard.putString("SubstationAlignState", substationAlignState.toString());


    }
}
