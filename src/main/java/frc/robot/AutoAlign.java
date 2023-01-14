package frc.robot;
import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.math.controller.PIDController;


public class AutoAlign {
  // gets horizontal and vertical offsets from target
    PIDController PIDControllerDriveBaseSpeed = new PIDController(1, 0, 0);
    private PhotonCamera camera = new PhotonCamera("camera");
    PhotonPipelineResult result = new PhotonPipelineResult();
    PhotonTrackedTarget target = new PhotonTrackedTarget();
    // NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
    // NetworkTableEntry tx = table.getEntry("tx");
    // NetworkTableEntry ty = table.getEntry("ty");
    // NetworkTableEntry tv = table.getEntry("tv");



    boolean isTarget(){
       if(result.hasTargets()){
           return true;
       }
       return false;
    }

    boolean getTarget(){
        result = camera.getLatestResult();
        if(isTarget()){
            target = result.getBestTarget();
            return true;
        }
        return false;
    }
    
    double getHorizontalOffset(){
        if(getTarget()){
            //return target.getYaw() + pidgeonheading;
        }
        return 42069;
    }

    double getVerticalOffset(){
        if(getTarget()){
            return target.getPitch();
        }
        return 42069; //cringe
    }

    boolean isAlligned(){
        if(getHorizontalOffset() < Constants.Limelight.kAlignDistance && getHorizontalOffset() > -Constants.Limelight.kAlignDistance){
            return true;
        }
        return false;
    }

    void Align(){
        drive.drivefieldoriented
        //turn until pidgeon heading is same as offset        
    }

    void driverControl(){
        if(isAlligned()){
            drive.driverobotoriented(0.5 * joystick.getSwerveTranslation, 0.1 *joystick.getSwerveRotation)
        }
    }

}
