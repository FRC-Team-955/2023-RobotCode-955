package frc.robot.Sensors;

import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.math.geometry.Translation2d;
import frc.robot.Constants;

//import edu.wpi.first.math.geometry.Translation2d;



public class LimelightCameraWrapper {
    public static PhotonCamera limelight =  new PhotonCamera("gloworm"); 
    public static PhotonPipelineResult result = new PhotonPipelineResult();
    public static PhotonTrackedTarget target = new PhotonTrackedTarget(); 

    public static boolean hasTargets(){
        return limelight.getLatestResult().hasTargets();
    }
    
    public static double getHorizontalOffset(){
        result = limelight.getLatestResult();
        if (hasTargets()){
            return result.getBestTarget().getPitch();
        }
        return 42069;
    }
    public static double getVerticalOffset(){
        result = limelight.getLatestResult();
        if (hasTargets()){
            return result.getBestTarget().getYaw();
        }
        return 42069;
    }

    public static boolean isAlignedToConeNode(){
        return Math.abs(getHorizontalOffset()) < Constants.LimelightCamera.coneAlignTolerance;
    }
    public static boolean isAlignedToGamePiece(){
        return Math.abs(getHorizontalOffset() - getVerticalOffset() * Constants.LimelightCamera.gamePieceVerticalToHorizontalSlope) < Constants.LimelightCamera.gamePieceTolerance;
    }
    public static Translation2d getOdometryPosition(Translation2d gamePiece){
        return new Translation2d(gamePiece.getX() + (Constants.isBlue()?-getVerticalOffset()*Constants.LimelightCamera.verticalOffsetToDistance:getVerticalOffset()*Constants.LimelightCamera.verticalOffsetToDistance),gamePiece.getY());
    }
    public static void setPipeline(int pipelineIndex){
        limelight.setPipelineIndex(pipelineIndex);
    }
}
