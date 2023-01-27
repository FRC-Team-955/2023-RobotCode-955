package frc.robot.Sensors;

import java.util.ArrayList;
import java.util.Optional;

import org.photonvision.EstimatedRobotPose;
import org.photonvision.PhotonCamera;
import org.photonvision.PhotonPoseEstimator;
import org.photonvision.PhotonPoseEstimator.PoseStrategy;
import org.photonvision.targeting.PhotonPipelineResult;

import edu.wpi.first.apriltag.AprilTag;
import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
import frc.robot.Constants;

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
        Transform3d robotToCam = new Transform3d(new Translation3d(Constants.AprilTagCamera.xPosition, Constants.AprilTagCamera.yPosition, Constants.AprilTagCamera.zPosition), new Rotation3d(0,0,0));
        photonPoseEstimator = new PhotonPoseEstimator(aprilTagFieldLayout, PoseStrategy.CLOSEST_TO_REFERENCE_POSE, camera, robotToCam);

    }
    public static Optional<EstimatedRobotPose> getEstimatedGlobalPose(Pose2d prevEstimatedRobotPose) {
        photonPoseEstimator.setReferencePose(prevEstimatedRobotPose);
        return photonPoseEstimator.update();
    }
    public static boolean hasTargets(){
        return camera.getLatestResult().hasTargets();
    }

    public static double getHorizontalOffset(){
        result = camera.getLatestResult();
        if(hasTargets()){
            return result.getBestTarget().getYaw();
        }
        return 42069;
    }
    public static double getVerticalOffset(){
        result = camera.getLatestResult();
        if(hasTargets()){
            return result.getBestTarget().getPitch();
        }
        return 42069;
    }
    public static boolean isAlignedToCubeNode(){
        return Math.abs(getHorizontalOffset()) < Constants.LimelightCamera.kAlignTolerance;
    }

}
