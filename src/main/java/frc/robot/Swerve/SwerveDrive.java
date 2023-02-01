package frc.robot.Swerve;

import java.util.ArrayList;
import java.util.Optional;

import org.photonvision.EstimatedRobotPose;

import edu.wpi.first.math.controller.HolonomicDriveController;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;

// import edu.wpi.first.math.kinematics.SwerveDriveOdometry;

import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.util.datalog.DoubleLogEntry;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.Constants;
import frc.robot.Drivebase;
import frc.robot.IO;
import frc.robot.Sensors.AprilTagCameraWrapper;
import frc.robot.Sensors.Gyro;

public class SwerveDrive {
    // status variable for being enabled
    public boolean mIsEnabled = false;

    // public SwerveDriveOdometry swerveOdometry;
    public final SwerveDrivePoseEstimator poseEstimator;

    public SwerveMod[] SwerveMods;
    public double headingSetPoint;
    private PIDController controller = new PIDController(0.07,0,0);

    private PIDController xController = new PIDController(0.7,0,0);
    private PIDController yController = new PIDController(0.7,0,0);
    private PIDController thetaController = new PIDController(0.05,0,0);
    private ProfiledPIDController thetaControllerFake = new ProfiledPIDController(0.5,0,0, new TrapezoidProfile.Constraints(40, 180));
    public HolonomicDriveController autoController = new HolonomicDriveController(xController, yController, thetaControllerFake);
    public Trajectory trajectory = new Trajectory();
    public Trajectory turningTrajectory = new Trajectory();
    public Timer timer = new Timer();
    public String File = "pathplanner/generatedJSON/CorrectPath.path"; 

    // chassis velocity status
    ChassisSpeeds chassisVelocity = new ChassisSpeeds(); // not used, commented out in updateSwerveOdometry()
    
    // boolean to lock Swerve wheels
    public boolean locked = false;

    public SwerveDrive() {       
        Gyro.set(0); 
        
        SwerveMods = new SwerveMod[] {
            //MODULE 0 AND 3 MIGHT BE SLIGHTLY OFF
            new SwerveMod(0, 4, 8, 9, 253.775, "mod0"),
            new SwerveMod(1, 3, 2, 11, 123.886, "mod1"),
            new SwerveMod(2, 6, 7, 10, 309.223, "mod2"),
            new SwerveMod(3, 1, 5, 12, 250.524, "mod3"),
        };

        SwerveModulePosition[] initPoses = new SwerveModulePosition[4];
        for(SwerveMod mod : SwerveMods){
            initPoses[mod.moduleNumber] = mod.getState();
        }
        
        // swerveOdometry = new SwerveDriveOdometry(SwerveSettings.SwerveConstants.swerveKinematics, Gyro.getYawR2D(), initPoses, new Pose2d(0.0,0.0,Gyro.getYawR2D()));
        poseEstimator = new SwerveDrivePoseEstimator(SwerveSettings.SwerveConstants.swerveKinematics,Gyro.getYawR2D(), initPoses, new Pose2d(0.0,0.0,Gyro.getYawR2D()));
    }

    public void drive(Translation2d translation, double rotation, boolean fieldRelative, boolean isOpenLoop, boolean useFixedHeading, double heading) {
        if(IO.Drivebase.rotationOverrideEnabled()){
            rotation = IO.Drivebase.getSwerveRotation();
        }else if (useFixedHeading){
            headingSetPoint = heading;
        }else{
            headingSetPoint += rotation * 0.24;
        }
        
        SwerveModuleState[] swerveModuleStates = null;
        if (locked) {
            swerveModuleStates = new SwerveModuleState[]{
                new SwerveModuleState(0, Rotation2d.fromDegrees(0)),
                new SwerveModuleState(0, Rotation2d.fromDegrees(0)),
                new SwerveModuleState(0, Rotation2d.fromDegrees(0)),
                new SwerveModuleState(0, Rotation2d.fromDegrees(0))
            };
        } else {
            swerveModuleStates =
                SwerveSettings.SwerveConstants.swerveKinematics.toSwerveModuleStates(
                    fieldRelative ? ChassisSpeeds.fromFieldRelativeSpeeds(
                                        translation.getX(), 
                                        translation.getY(), 
                                        controller.calculate(Gyro.getAngle(), headingSetPoint), 
                                        Gyro.getHeadingR2D()
                                    )
                                    : new ChassisSpeeds(
                                        translation.getX(), 
                                        translation.getY(),
                                        rotation)
                                    );
        }
        SwerveDriveKinematics.desaturateWheelSpeeds(swerveModuleStates, SwerveSettings.SwerveConstants.maxSpeed);

        for (SwerveMod mod : SwerveMods) {
            mod.setDesiredState(swerveModuleStates[mod.moduleNumber]);
        }
    }

        
    public Pose2d getPose() {
        // return swerveOdometry.getPoseMeters();
        return poseEstimator.getEstimatedPosition();
    }

    public void resetOdometry(Pose2d pose) {
        // swerveOdometry.resetPosition(Gyro.getYawR2D(), getPoses(), pose);
        poseEstimator.resetPosition(Gyro.getYawR2D(), getPoses(), pose);
    }

    public void resetAnglesToAbsolute() {
        for (SwerveMod mod : SwerveMods) {
            mod.syncEncoders();
            mod.resetDrive();
        }
    }

    public SwerveModulePosition[] getPoses() {
        SwerveModulePosition[] poses = new SwerveModulePosition[4];
        for(SwerveMod mod : SwerveMods){
            poses[mod.moduleNumber] = mod.getState();
        }
        return poses;
    }

    public void updateSwerveOdometry(){
        // swerveOdometry.update(Rotation2d.fromDegrees(-Gyro.getHeading()), getPoses()); //maybe 0-360
        // chassisVelocity = SwerveSettings.SwerveConstants.swerveKinematics.toChassisSpeeds(
        //     SwerveMods[0].getState(),
        //     SwerveMods[1].getState(),
        //     SwerveMods[2].getState(),
        //     SwerveMods[3].getState()
        // );
        poseEstimator.update(Rotation2d.fromDegrees(-Gyro.getHeading()), getPoses());
        Optional<EstimatedRobotPose> result = AprilTagCameraWrapper.getEstimatedGlobalPose(getPose());
        //Filter when not level
        if (result.isPresent() && Gyro.getPitch() < Constants.AprilTagCamera.Filter.pitch && Gyro.getRoll() < Constants.AprilTagCamera.Filter.roll) {
            EstimatedRobotPose camPose = result.get();
            //Filter when pose is too far from current pose
            if (camPose.estimatedPose.toPose2d().getTranslation().getDistance(getPose().getTranslation()) <  Constants.AprilTagCamera.Filter.distance){
                poseEstimator.addVisionMeasurement(camPose.estimatedPose.toPose2d(), camPose.timestampSeconds);
            }
        }

    }

    public void logSwerve() {
        SwerveMods[0].logSwerveMod();
        SwerveMods[1].logSwerveMod();
        SwerveMods[2].logSwerveMod();
        SwerveMods[3].logSwerveMod();
    }

    public void generateTrajectory(){
        ArrayList<Translation2d> interiorWaypoints = new ArrayList<Translation2d>();
        if(Constants.isBlue()? getPose().getX() > 5.2 : getPose().getX() < 11){
            if(getPose().getY() >= 4 || getPose().getY() <= 5.25){
                if(Constants.isBlue()){
                    interiorWaypoints.add(Constants.FieldPositions.AutoAlignPositions.blueAvoidChargerUppper);
                }if(Constants.isRed()){
                    interiorWaypoints.add(Constants.FieldPositions.AutoAlignPositions.redAvoidChargerUppper);
                }
            }
            if(getPose().getY() > 5.25 || getPose().getY() <= 6.5){
                if(Constants.isBlue()){
                    interiorWaypoints.add(Constants.FieldPositions.AutoAlignPositions.blueAvoidChargerLower);
                }if(Constants.isRed()){
                    interiorWaypoints.add(Constants.FieldPositions.AutoAlignPositions.redAvoidChargerLower);
                }
            }
        }
        if(Constants.isBlue()? getPose().getX() > 4: getPose().getX() < 12.2){
            if (getPose().getY() <= 5.25){
                if (Constants.isBlue()){
                    interiorWaypoints.add(Constants.FieldPositions.AutoAlignPositions.blueOutCommunityUppper);
                }else if (Constants.isRed()){
                    interiorWaypoints.add(Constants.FieldPositions.AutoAlignPositions.redOutCommunityUppper);
                }
            }        
            else if (getPose().getY() > 5.25){
                if (Constants.isBlue()){
                    interiorWaypoints.add(Constants.FieldPositions.AutoAlignPositions.blueInCommunityLower);
                }else if (Constants.isRed()){
                    interiorWaypoints.add(Constants.FieldPositions.AutoAlignPositions.redInCommunityLower);
                }
            } 
        }
        if(Constants.isBlue()? getPose().getX() > 2.2 : getPose().getX() < 13.8){
            if (getPose().getY() <= 5.25){
                if (Constants.isBlue()){
                    interiorWaypoints.add(Constants.FieldPositions.AutoAlignPositions.blueInCommunityUppper);
                }else if (Constants.isRed()){
                    interiorWaypoints.add(Constants.FieldPositions.AutoAlignPositions.redInCommunityUppper);
                }
            }        
            else if (getPose().getY() > 5.25){
                if (Constants.isBlue()){
                    interiorWaypoints.add(Constants.FieldPositions.AutoAlignPositions.blueInCommunityLower);
                }else if (Constants.isRed()){
                    interiorWaypoints.add(Constants.FieldPositions.AutoAlignPositions.redInCommunityLower);
                }
            } 
        }
        TrajectoryConfig config = new TrajectoryConfig(SwerveSettings.SwerveConstants.maxSpeed, SwerveSettings.SwerveConstants.maxAcceleration);
        // config.setReversed(false);
        trajectory = TrajectoryGenerator.generateTrajectory(getPose(), interiorWaypoints, new Pose2d(interiorWaypoints.get(interiorWaypoints.size()-1) , new Rotation2d()), config);
        // trajectory = TrajectoryGenerator.generateTrajectory(getPose(), interiorWaypoints, new Pose2d(IO.keyInputOdometryPosition, new Rotation2d()), config);

    }  
    // public void loadTrajectory(String name){
    //     String trajectoryJSON = "pathplanner/generatedJSON/" + name;
    //     Path deployDirectory;
    //     try {
    //         deployDirectory = Filesystem.getDeployDirectory().toPath().resolve(trajectoryJSON);
    //         trajectory = TrajectoryUtil.fromPathweaverJson(deployDirectory);
    //     } catch (IO.DrivebaseException ex) {
    //         DriverStation.reportError("Unable to open trajectory: " + trajectoryJSON, ex.getStackTrace());
    //     }

    //     // Json = deployDirectory.toString();


    //     ahrs.setAngleAdjustment(0);
    //     // ahrs.setAngleAdjustment(0);
    //      //                                                       The robot fields angle (in pathweaver rotation)
    //     // System.out.println("trajectory: " +trajectory.getInitialPose().getRotation());
    //     //Rotation2d.fromDegrees(90)
    //     swerveOdometry.resetPosition(trajectory.getInitialPose(), trajectory.getInitialPose().getRotation());
    //     timer.reset();
    //     timer.start();
    // }

    // public boolean followTrajectory(double holonomicRotation){
     
    //     updateSwerveOdometry();
    //     Trajectory.State goal = trajectory.sample(timer.get());
    //                 //                                                          rotation in Path Weaver
    //     ChassisSpeeds adjustedSpeeds = autoController.calculate(getPose(), goal, Rotation2d.fromDegrees(0));


    //     adjustedSpeeds.vyMetersPerSecond *=-1;

    //     ChassisSpeeds adjustedSpeeds2 = new ChassisSpeeds(adjustedSpeeds.vxMetersPerSecond,adjustedSpeeds.vyMetersPerSecond, thetaController.calculate(ahrs.getAngle(), holonomicRotation));

    //     SwerveModuleState[] swerveModuleStates = SwerveSettings.SwerveConstants.swerveKinematics.toSwerveModuleStates(adjustedSpeeds2);

    //     SwerveDriveKinematics.desaturateWheelSpeeds(swerveModuleStates, SwerveSettings.SwerveConstants.maxSpeed);

    //     for (SwerveMod mod : SwerveMods) {
    //         mod.setDesiredState(swerveModuleStates[mod.moduleNumber]);
    //     }
    //     System.out.println("Time: " +timer.get());
    //     // if (timer.get() > trajectory.getTotalTimeSeconds()){
    //     //     return true;
    //     // }
    //     if (timer.get() > trajectory.getTotalTimeSeconds()){
    //         return true;
    //     }
    //     return false;
    // }
}
