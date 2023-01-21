package frc.robot.Sensors;

import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonPipelineResult;

// import edu.wpi.first.apriltag.AprilTagFieldLayout;


public final class AprilTagCamera {
    //AprilTagFieldLayout aprilTagFieldLayout = new AprilTagFieldLayout(k2023ChargedUp.m_resourceFile);
    public static PhotonCamera camera = new PhotonCamera("photonvision"); 
    

    public static double getHorizontaloffset(){
        PhotonPipelineResult result = camera.getLatestResult(); //CHANGE DOUBLE NOT SUPPOSED TO BE DOUBLE
        return result.getBestTarget().getYaw();
    }
    public static double getVerticalOffset(){
        PhotonPipelineResult result = camera.getLatestResult(); //CHANGE DOUBLE NOT SUPPOSED TO BE DOUBLE
        return result.getBestTarget().getPitch();
    }
}
