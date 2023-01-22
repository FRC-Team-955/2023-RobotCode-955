package frc.robot.Sensors;

import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

//import edu.wpi.first.math.geometry.Translation2d;



public class LimelightCamera {
    public static PhotonCamera limelight =  new PhotonCamera("gloworm"); 
    public static PhotonPipelineResult result = new PhotonPipelineResult();
    public static PhotonTrackedTarget target = new PhotonTrackedTarget(); 


    public static boolean hasTargets() {
        result = limelight.getLatestResult();
        return result.hasTargets();
    }

    public static double getHorizontaloffset(){
        result = limelight.getLatestResult();
        return result.getBestTarget().getYaw();
    }
}