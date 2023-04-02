package frc.robot.Sensors;

import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

import frc.robot.Constants;

//import edu.wpi.first.math.geometry.Translation2d;



public class LimelightCameraWrapper {
    public static PhotonCamera limelight =  new PhotonCamera("OV5647"); 
    public static PhotonPipelineResult result = new PhotonPipelineResult();
    public static PhotonTrackedTarget target = new PhotonTrackedTarget(); 

    public static boolean hasTargets(){
        return limelight.getLatestResult().hasTargets();
    }
    
    public static double getHorizontalOffset(){
        result = limelight.getLatestResult();
        if (result.hasTargets()){
            return result.getBestTarget().getPitch();
        }
        return 42069;
    }
    public static double getVerticalOffset(){
        result = limelight.getLatestResult();
        if (result.hasTargets()){
            return result.getBestTarget().getYaw();
        }
        return 42069;
    }

    public static boolean isAlignedToConeNode(){
        return Math.abs(getHorizontalOffset()) < Constants.LimelightCamera.coneAlignTolerance;
    }
    public static boolean isAlignedToGamePiece(){
        return Math.abs(getHorizontalOffset() - Constants.LimelightCamera.gamePieceVerticalToHorizontalA * Math.pow(Constants.LimelightCamera.gamePieceVerticalToHorizontalB, getVerticalOffset())) < Constants.LimelightCamera.gamePieceTolerance;
    }

    public static double getDistanceToGamePiece(){
        // if doesn't see target
        if(!hasTargets()){
            return Constants.Auto.stopBeforeGamepieceOffset;
        }

        return Constants.LimelightCamera.gamePieceVerticalToDistanceB + Constants.LimelightCamera.gamePieceVerticalToDistanceA * Math.log( getVerticalOffset());
    }

    public static void setPipeline(int pipelineIndex){
        limelight.setPipelineIndex(pipelineIndex);
    }
}
