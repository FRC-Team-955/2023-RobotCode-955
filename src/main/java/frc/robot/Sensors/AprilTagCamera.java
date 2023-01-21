package frc.robot.Sensors;
import edu.wpi.first.apriltag.AprilTagFieldLayout;

public final class AprilTagCamera {
    //AprilTagFieldLayout aprilTagFieldLayout = new AprilTagFieldLayout(k2023ChargedUp.m_resourceFile);
    PhotonCamera camera = new PhotonCamera("photonvision"); 
    

    public static void getHorizontaloffset(){
        double result = camera.getLatestResult(); //CHANGE DOUBLE NOT SUPPOSED TO BE DOUBLE
        horizontalOffset = calculate(result.getBestTarget().getYaw(), 0);
    }
}
