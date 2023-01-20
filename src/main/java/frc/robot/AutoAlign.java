package frc.robot;
import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import frc.robot.Constants.Limelight;
import frc.robot.Sensors.LimelightCamera;


public class AutoAlign {
  // gets horizontal and vertical offsets from target
    // PIDController PIDControllerDriveBaseSpeed = new PIDController(1, 0, 0);
    PIDController PIDOdometeryAlignX = new PIDController(1, 0, 0);
    PIDController PIDOdometeryAlignY = new PIDController(1, 0, 0);
    private PhotonCamera camera = new PhotonCamera("camera");
    PhotonPipelineResult result = new PhotonPipelineResult();
    PhotonTrackedTarget target = new PhotonTrackedTarget();

    // LimelightCamera Limelight = new LimelightCamera();
    
    PIDController PIDControllerDriveBaseSpeed = new PIDController(1, 0, 0);
    private PhotonCamera limeLight =  new PhotonCamera("gloworm");
    PhotonPipelineResult limeResult = new PhotonPipelineResult();
    PhotonTrackedTarget limeTarget = new PhotonTrackedTarget();
    
    April

    PIDController gridPID = new PIDController(1, 0, 0);
    public double desiredGridPosition = 1000;

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
            return target.getYaw();
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
        Drivebase.driveFieldRelative()
        //turn until pidgeon heading is same as offset        
    }

    void driverControl(){
        if(isAlligned()){
            drive.driverobotoriented(0.5 * Joystick.getSwerveTranslation(), 0.1 *Joystick.getSwerveRotation());
        }
    }

    void moveToAprilTag(Pose2d goalPose){
        Pose2d pose = Drivebase.getPose();
        double poseX = pose.getX();
        double poseY = pose.getY();
        double goalPoseX = goalPose.getX();
        double goalPoseY = goalPose.getY();

    
        Double movementX = PIDOdometeryAlignX.calculate(poseX, goalPoseX);
        Double movementY = PIDOdometeryAlignX.calculate(poseY, goalPoseY);
        Translation2d translation = new Translation2d(movementX, movementY); 
        Drivebase.driveFieldRelativeHeading(translation, 180);
    }

    boolean isAprilTagAlign() {

        return True;
    }


    public static boolean isLimeLightTarget(){
        if(LimelightCamera.hasTargets()){
            return true;
        }
        return false;
     }

    boolean getLimeLightTarget(){
        limeResult = limeLight.getLatestResult();
        if(isLimeLightTarget()){
            limeTarget = limeResult.getBestTarget();
            return true;
        }
        return false;
    }
    
    double getLimeLightHorizontalOffset(){
        if(getLimeLightTarget()){
            //return target.getYaw() + pidgeonheading;
        }
        return 42069;
    }

    double getLimeLightVerticalOffset(){
        if(getLimeLightTarget()){
            return limeTarget.getPitch();
        }
        return 42069; //cringe
    }

    boolean isGridAlligned(){
        if(getLimeLightHorizontalOffset() < Constants.Limelight.kAlignDistance && getLimeLightHorizontalOffset() > -Constants.Limelight.kAlignDistance){
            return true;
        }
        return false;
    }

    void alignToGridOdometery(){
        double output = MathUtil.clamp(gridPID.calculate(Drivebase.getPose().getY(), desiredGridPosition), -1, 1);
        Drivebase.driveFieldRelativeHeading(new Translation2d(IO.getSwerveTranslation().getX()*.5, IO.getSwerveTranslation().getY() * .5 + output), 180);     
    }

    void alignToCubeNode() { // april tag
        if (!isGridAlligned()) {

        }
    }

    void alignToConeNode() { // limelight

    }

    void moveToGrid(){
        if(isGridAlligned()){
            Drivebase.driveRobotRelative(new Translation2d(IO.getSwerveTranslation().getX()*.5, 
                                                            IO.getSwerveTranslation().getY() * .5), 
                                                            180);
        } else {
            alignToCubeNode(); 
        }
    }
}


