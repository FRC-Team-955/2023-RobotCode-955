package frc.robot.Sensors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import org.photonvision.EstimatedRobotPose;
import org.photonvision.PhotonCamera;
import org.photonvision.PhotonPoseEstimator;
import org.photonvision.PhotonPoseEstimator.PoseStrategy;
import org.photonvision.targeting.PhotonPipelineResult;

import edu.wpi.first.apriltag.AprilTag;
import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
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

    public static void setUp(){
        final AprilTag tag01 = new AprilTag(1, new Pose3d(new Translation3d(15.513558, 1.071626, 0.462788),new Rotation3d(0.0,0.0,180)));
        final AprilTag tag02 = new AprilTag(2, new Pose3d(new Translation3d(15.513558, 2.748026, 0.462788), new Rotation3d(0.0,0.0,180)));
        final AprilTag tag03 = new AprilTag(3, new Pose3d(new Translation3d(15.513558, 4.424426, 0.462788),new Rotation3d(0.0,0.0,180)));
        final AprilTag tag04 = new AprilTag(4, new Pose3d(new Translation3d(16.178784, 6.749796, 0.695452),new Rotation3d(0.0,0.0,180)));
        final AprilTag tag05 = new AprilTag(5, new Pose3d(new Translation3d(0.36195, 6.749796, 0.695452), new Rotation3d(0.0,0.0,0.0)));
        final AprilTag tag06 = new AprilTag(6, new Pose3d(new Translation3d(1.02743, 4.424426, 0.462788), new Rotation3d(0.0,0.0,0.0)));
        final AprilTag tag07 = new AprilTag(7, new Pose3d(new Translation3d(1.02743, 2.748026, 0.462788), new Rotation3d(0.0,0.0,0.0)));
        final AprilTag tag08 = new AprilTag(8, new Pose3d(new Translation3d(1.02743, 1.071626, 0.462788), new Rotation3d(0.0,0.0,0.0)));
        ArrayList<AprilTag> atList = new ArrayList<AprilTag>();
        atList.add(tag01);
        atList.add(tag02);
        atList.add(tag03);
        atList.add(tag04);
        atList.add(tag05);
        atList.add(tag06);
        atList.add(tag07);
        atList.add(tag08);
        try {
            aprilTagFieldLayout = AprilTagFieldLayout.loadFromResource(AprilTagFields.k2023ChargedUp.m_resourceFile);
        } catch (IOException e) {
            aprilTagFieldLayout = new AprilTagFieldLayout(atList, Constants.FieldPositions.fieldX, Constants.FieldPositions.fieldY);
            e.printStackTrace();
        }
        camera = new PhotonCamera("Global_Shutter_Camera");
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
        return Math.abs(getHorizontalOffset()) < Constants.LimelightCamera.alignTolerance;
    }

}
