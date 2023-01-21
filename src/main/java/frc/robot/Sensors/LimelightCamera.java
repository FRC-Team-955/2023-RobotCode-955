package frc.robot.Sensors;

import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

//import edu.wpi.first.math.geometry.Translation2d;



public class LimelightCamera {
    public static PhotonCamera limeLight =  new PhotonCamera("gloworm"); 
    public static PhotonPipelineResult result = new PhotonPipelineResult();
    public static PhotonTrackedTarget target = new PhotonTrackedTarget(); 


    public static boolean hasTargets() {
        return result.hasTargets();
    }

    boolean getHorizontalOffset(){
        result = limeLight.getLatestResult();
        if(hasTargets()){
            target = result.getBestTarget();
            return true;
        }
        else{
        return false;
        }
} 
}