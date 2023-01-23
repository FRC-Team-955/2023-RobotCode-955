package frc.robot.Sensors;

import org.photonvision.EstimatedRobotPose;
import org.photonvision.PhotonCamera;
import org.photonvision.PhotonPoseEstimator;
import org.photonvision.PhotonPoseEstimator.PoseStrategy;
import org.photonvision.targeting.PhotonPipelineResult;
import edu.wpi.first.apriltag.AprilTagFieldLayout;
// import org.photonvision.RobotPoseEstimator;
// import org.photonvision.RobotPoseEstimator.PoseStrategy;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.apriltag.AprilTag;
// import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.math.Pair;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.Constants;

// import frc.robot.Constants.FieldConstants;
// import frc.robot.Constants.VisionConstants;
import java.util.ArrayList;
import java.util.Optional;
// import org.photonvision.PhotonCamera;

public class AprilTagCameraWrapper {

    public static AprilTagFieldLayout aprilTagFieldLayout;
    public static PhotonCamera camera;
    public static PhotonPoseEstimator photonPoseEstimator;

    private static PhotonPipelineResult result;

    public AprilTagCameraWrapper(){
        final AprilTag tag01 = new AprilTag(01, new Pose3d(new Pose2d(0.0, 0.0, Rotation2d.fromDegrees(0.0))));
        ArrayList<AprilTag> atList = new ArrayList<AprilTag>();
        atList.add(tag01);
        aprilTagFieldLayout = new AprilTagFieldLayout(atList, 10, 10);
        // aprilTagFieldLayout = new AprilTagFieldLayout(AprilTagFieldLayout.loadFromResource(AprilTagFields.k2023ChargedUp.m_resourceFile));
        camera = new PhotonCamera("GlobalShutterCamera");
        Transform3d robotToCam = new Transform3d(new Translation3d(Constants.FiducalCamera.xPosition, Constants.FiducalCamera.yPosition, Constants.FiducalCamera.zPosition), new Rotation3d(0,0,0));
        photonPoseEstimator = new PhotonPoseEstimator(aprilTagFieldLayout, PoseStrategy.CLOSEST_TO_REFERENCE_POSE, camera, robotToCam);

    }
    public static Optional<EstimatedRobotPose> getEstimatedGlobalPose(Pose2d prevEstimatedRobotPose) {
        photonPoseEstimator.setReferencePose(prevEstimatedRobotPose);
        return photonPoseEstimator.update();
    }    

    public static double getHorizontalOffset(){
        result = camera.getLatestResult();
        if( result.hasTargets()){
            // System.out.println("in target: " + result.getBestTarget().getYaw());
            return result.getBestTarget().getYaw();
        }
        // System.out.println("no target");

        return 42069;
    }
    public static double getVerticalOffset(){
        result = camera.getLatestResult(); //CHANGE DOUBLE NOT SUPPOSED TO BE DOUBLE
        if( result.hasTargets()){
            // System.out.println("in target: " + result.getBestTarget().getYaw());
            return result.getBestTarget().getPitch();
        }
        return 42069;
    }


}
