package frc.robot.Sensors;

import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

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
            return result.getBestTarget().getYaw();
        }
        return 42069;
    }

    public static boolean isAlignedToConeNode(){
        return Math.abs(getHorizontalOffset()) < Constants.LimelightCamera.kAlignTolerance;
    }
}
