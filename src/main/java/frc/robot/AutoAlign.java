package frc.robot;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Sensors.AprilTagCameraWrapper;
import frc.robot.Sensors.LimelightCameraWrapper;

public class AutoAlign {
    private static PIDController aprilTagAlignXPID = new PIDController(Constants.AutoAlign.aprilTagAlignXkP, Constants.AutoAlign.aprilTagAlignXkI, Constants.AutoAlign.aprilTagAlignXkD); 
    private static PIDController limelightAlignXPID = new PIDController(Constants.AutoAlign.limelightAlignXkP, Constants.AutoAlign.limelightAlignXkI, Constants.AutoAlign.limelightAlignXkD);
    private static PIDController gamePieceAutoAlignXPID = new PIDController(Constants.AutoAlign.gamePieceAutoAlignXkP, Constants.AutoAlign.gamePieceAutoAlignXkI, Constants.AutoAlign.gamePieceAutoAlignXkD);
    private static PIDController gamePieceTelopAlignXPID = new PIDController(Constants.AutoAlign.gamePieceTelopAlignXkP, Constants.AutoAlign.gamePieceTelopAlignXkI, Constants.AutoAlign.gamePieceTelopAlignXkD);

    private static PIDController odometryAlignXPID = new PIDController(Constants.AutoAlign.odometryAlignXkP, Constants.AutoAlign.odometryAlignXkI, Constants.AutoAlign.odometryAlignXkD);
    private static PIDController odometryAlignYPID = new PIDController(Constants.AutoAlign.odometryAlignYkP, Constants.AutoAlign.odometryAlignYkI, Constants.AutoAlign.odometryAlignYkD);
    private static PIDController translationAlignXPID = new PIDController(Constants.AutoAlign.translationAlignXkP, Constants.AutoAlign.translationAlignXkI, Constants.AutoAlign.translationAlignXkD);
    private static PIDController translationAlignYPID = new PIDController(Constants.AutoAlign.translationAlignYkP, Constants.AutoAlign.translationAlignYkI, Constants.AutoAlign.translationAlignYkD);


    Drivebase drive = new Drivebase();

    public static boolean alignOdometrykP(Translation2d goalTranslation, double heading, double XkP, double YkP, double alignTolerance, boolean isOpenLoopDrive){
        Pose2d pose = Drivebase.getPose();
        double poseX = pose.getX();
        double poseY = pose.getY();
        double goalPoseX = goalTranslation.getX();
        double goalPoseY = goalTranslation.getY();
        odometryAlignXPID.setP(XkP);
        odometryAlignYPID.setP(YkP);
        double movementX = odometryAlignXPID.calculate(poseX, goalPoseX);
        double movementY = odometryAlignYPID.calculate(poseY, goalPoseY);

        Translation2d translation = new Translation2d(Constants.isBlue()?-movementY:movementY, Constants.isBlue()?-movementX:movementX);

        Drivebase.driveFieldRelativeHeading(translation, heading , isOpenLoopDrive);

        return (Math.abs(goalPoseX - poseX) < alignTolerance && Math.abs(goalPoseY - poseY) < alignTolerance);
     
    }
    public static boolean alignOdometry(Translation2d goalTranslation, double heading){
        return alignOdometrykP(goalTranslation, heading, Constants.AutoAlign.odometryAlignXkP, Constants.AutoAlign.odometryAlignYkP, Constants.AutoAlign.alignTolerance, true);
    }
    public static boolean alignTranslation(Translation2d goalTranslation){
        Pose2d pose = Drivebase.getPose();
        double poseX = pose.getX();
        double poseY = pose.getY();
        double goalPoseX = goalTranslation.getX();
        double goalPoseY = goalTranslation.getY();
        double movementX = translationAlignXPID.calculate(poseX, goalPoseX);
        double movementY = translationAlignYPID.calculate(poseY, goalPoseY);

        Translation2d translation = new Translation2d(Constants.isBlue()?-movementY:movementY, Constants.isBlue()?-movementX:movementX);

        Drivebase.driveFieldRelativeRotation(translation, IO.Drivebase.getSwerveRotation() *0.05, true, true);

        return (Math.abs(goalPoseX - poseX) < Constants.AutoAlign.alignTranslationX && Math.abs(goalPoseY - poseY) < Constants.AutoAlign.alignTranslationX);
    }

    public static boolean alignAprilTag(){
        if(AprilTagCameraWrapper.hasTargets()){
            double movementY = aprilTagAlignXPID.calculate(AprilTagCameraWrapper.getHorizontalOffset(), 0);
            Drivebase.driveFieldRelativeHeading(new Translation2d(movementY, 0), -180,true);
        }
        gridAlignY = Drivebase.getPose().getY();

        return AprilTagCameraWrapper.isAlignedToCubeNode();
    }
    public static boolean alignTape(){
        // LimelightCameraWrapper.setPipeline(0);
        
        if (LimelightCameraWrapper.hasTargets()){
            double movementY = limelightAlignXPID.calculate(LimelightCameraWrapper.getHorizontalOffset(), Constants.AutoAlign.alignedGamePiecePitch);
            Drivebase.driveFieldRelativeHeading(new Translation2d(movementY, 0), -180,true);
        }

        return LimelightCameraWrapper.isAlignedToConeNode();
    }
    public static boolean alignToPiece(boolean isAuto){
        LimelightCameraWrapper.setPipeline(0);
        if(LimelightCameraWrapper.hasTargets()){
            double movementY = isAuto?gamePieceAutoAlignXPID.calculate(LimelightCameraWrapper.getHorizontalOffset(),Constants.LimelightCamera.gamePieceVerticalToHorizontalA * Math.pow(Constants.LimelightCamera.gamePieceVerticalToHorizontalB, LimelightCameraWrapper.getVerticalOffset())):
                                    gamePieceTelopAlignXPID.calculate(LimelightCameraWrapper.getHorizontalOffset(),Constants.LimelightCamera.gamePieceVerticalToHorizontalA * Math.pow(Constants.LimelightCamera.gamePieceVerticalToHorizontalB, LimelightCameraWrapper.getVerticalOffset()));
            Drivebase.driveRobotRelativeRotation(new Translation2d(-movementY,0 ), 0);
        }
        return LimelightCameraWrapper.isAlignedToGamePiece();
    }
    public static void forwardToPiece(boolean isAuto){
        if(LimelightCameraWrapper.hasTargets()){
            double movementY = isAuto?gamePieceAutoAlignXPID.calculate(LimelightCameraWrapper.getHorizontalOffset(),Constants.LimelightCamera.gamePieceVerticalToHorizontalA * Math.pow(Constants.LimelightCamera.gamePieceVerticalToHorizontalB, LimelightCameraWrapper.getVerticalOffset())):
                                    gamePieceTelopAlignXPID.calculate(LimelightCameraWrapper.getHorizontalOffset(),Constants.LimelightCamera.gamePieceVerticalToHorizontalA * Math.pow(Constants.LimelightCamera.gamePieceVerticalToHorizontalB, LimelightCameraWrapper.getVerticalOffset()));
            Drivebase.driveRobotRelativeRotation(new Translation2d(-movementY,-1.5), 0);
        }else{
            Drivebase.driveRobotRelativeRotation(new Translation2d(0,-1.5), 0);
        }
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
                        }
                    }
                    //cone and cube nodes should go to normal offset
                    else{
                        if(alignOdometry(IO.keyInputOdometryPosition, -180)) {
                            gridAlignState = GridAlignState.InPosition;

                        }
                    }
                    //Set rotation to -180 here so that you can adjust it manunally later if needed
                    alignRotation = -180;
                    alignTranslationY = 0;
                break;
                case InPosition:
                    alignRotation = alignRotation + IO.Drivebase.getSwerveRotation() *0.05;
                    alignTranslationY = alignTranslationY + IO.Drivebase.getSwerveTranslation().getX() * (Constants.isBlue()?-0.003:0.003);
                    switch(IO.gridNodeType){
                        //If Hybrid, don't move from noHit position
                        case Hybrid:
                            alignOdometry(IO.keyInputOdometryPosition.plus(new Translation2d(Constants.isBlue()?Constants.Auto.noHitGridOffset:-Constants.Auto.noHitGridOffset,0)), alignRotation);
                            return true;
                        //If Cube, don't move from normal position
                        case Cube:
                            alignOdometry(IO.keyInputOdometryPosition.plus(new Translation2d(Constants.isBlue()?Constants.FieldPositions.noHitCubeOffset:-Constants.FieldPositions.noHitCubeOffset, 0)), alignRotation);
                            return true;
                        case ConeFar:
                            switch(IO.gridRow){
                                //Not possible
                                case Hybrid:
                                    break;
                                //If Cone at Mid, don't move from normal position
                                case Mid:
                                    alignOdometry(new Translation2d(Constants.isBlue()?Constants.FieldPositions.nearGridBlueX:Constants.FieldPositions.nearGridRedX, IO.keyInputOdometryPosition.getY() + alignTranslationY), alignRotation);
                                    return true;
                                //If Cone at High, move forward
                                case High:
                                    // return alignOdometry(new Translation2d(Constants.isBlue()?Constants.FieldPositions.atGridBlueX:Constants.FieldPositions.atGridRedX, IO.keyInputOdometryPosition.getY()), alignRotation);
                                    return alignOdometry(new Translation2d(Constants.isBlue()?Constants.FieldPositions.nearGridBlueX:Constants.FieldPositions.nearGridRedX, IO.keyInputOdometryPosition.getY() + alignTranslationY), alignRotation);
                            }
                            break;
                        case ConeClose:
                            switch(IO.gridRow){
                                //Not possible
                                case Hybrid:
                                    break;
                                //If Cone at Mid, don't move from normal position
                                case Mid:
                                    return alignTranslation(new Translation2d(Constants.isBlue()?Constants.FieldPositions.atGridBlueX:Constants.FieldPositions.atGridRedX, IO.keyInputOdometryPosition.getY()  + alignTranslationY));
                                //If Cone at High, move forward
                                case High:
                                    // return alignOdometry(new Translation2d(Constants.isBlue()?Constants.FieldPositions.atGridBlueX:Constants.FieldPositions.atGridRedX, IO.keyInputOdometryPosition.getY()), alignRotation);
                                    return alignTranslation(new Translation2d(Constants.isBlue()?Constants.FieldPositions.atGridBlueX:Constants.FieldPositions.atGridRedX, IO.keyInputOdometryPosition.getY()  + alignTranslationY));
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
                    else if(alignOdometrykP(IO.keyInputSubstationPosition, 0 ,3 , 3, 0.08, true) && GamepieceManager.runExtention()) {
                        substationAlignState = SubstationAlignState.InPosition;
                    }
                    break;
                case InPosition:
                    return alignOdometrykP(new Translation2d(Constants.isBlue()?Constants.FieldPositions.atSubstationBlueX:Constants.FieldPositions.atSubstationRedX, 
                                            IO.keyInputSubstationPosition.getY()), 0, 3, 3, 0.05, true);
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
