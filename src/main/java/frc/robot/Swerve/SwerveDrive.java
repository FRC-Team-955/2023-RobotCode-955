package frc.robot.Swerve;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Optional;

import org.opencv.core.Mat;
import org.photonvision.EstimatedRobotPose;

import edu.wpi.first.math.Matrix;
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
import edu.wpi.first.math.trajectory.TrajectoryUtil;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.Sensors.AprilTagCameraWrapper;
import frc.robot.Sensors.Gyro;
import frc.robot.Subsystems.Arm;
import frc.robot.Subsystems.Elevator;

public class SwerveDrive {
    // status variable for being enabled
    public boolean mIsEnabled = false;

    // public SwerveDriveOdometry swerveOdometry;
    public final SwerveDrivePoseEstimator poseEstimator;

    public SwerveMod[] SwerveMods;
    public static double headingSetPoint=-180;
    private static double kP =0.1;
    private PIDController controller = new PIDController(kP,0,0);

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
        Gyro.set(90); 
        
        SwerveMods = new SwerveMod[] {
            //MODULE 0 AND 3 MIGHT BE SLIGHTLY OFF
            // The original offset for mod0: 253.775
            new SwerveMod(0, 8, 7, 9, 253.775+2.373, "mod0"),
            new SwerveMod(1, 2, 1, 11, 123.886, "mod1"),
            new SwerveMod(2, 4, 3, 10, 309.223, "mod2"),
            new SwerveMod(3, 6, 5, 12, 250.524-24.52-17.92, "mod3"),
        };

        SwerveModulePosition[] initPoses = new SwerveModulePosition[4];
        for(SwerveMod mod : SwerveMods){
            initPoses[mod.moduleNumber] = mod.getState();
        }
        poseEstimator = new SwerveDrivePoseEstimator(SwerveSettings.SwerveConstants.swerveKinematics,Gyro.getYawR2D(), initPoses, new Pose2d(0.0,0.0,Gyro.getYawR2D()));
    }
    public void drive(Translation2d translation, double rotation, boolean fieldRelative, boolean isOpenLoop, boolean useFixedHeading, double heading) {
        // if(IO.Drivebase.autoHeadingEnabled()){
        // if(false){
        //     rotation = IO.Drivebase.getSwerveRotation();
        // }else 
        if (useFixedHeading){
            headingSetPoint = heading;

        }else{
            headingSetPoint += rotation * Constants.Drivebase.turnRate;
            /* commented out cause errors :(
            if (armRetracted && elevatorRetracted) {
                headingSetPoint += rotation * Constants.Drivebase.turnRate;
            }
            if (armRetracted == false || elevatorRetracted == false) {
                headingSetPoint += rotation * (Constants.Drivebase.turnRate / 2);
            }
            */
        }

        // System.out.println("Gryo.getAngle(): "+ Gyro.getAngle());
        
        SwerveModuleState[] swerveModuleStates = null;
        if (false) {
            swerveModuleStates = new SwerveModuleState[]{
                new SwerveModuleState(0.0, Rotation2d.fromDegrees(0)),
                new SwerveModuleState(0.0, Rotation2d.fromDegrees(0)),
                new SwerveModuleState(0.0, Rotation2d.fromDegrees(0)),
                new SwerveModuleState(0.0, Rotation2d.fromDegrees(0))
            };
        } else {
            double extentiondistance = 2*Math.cos(Math.toRadians(Arm.getOffsetPosition())+ 2* Math.cos(Math.toRadians(42))*Elevator.encoder.getPosition()/30);
            // controller.setP((1-(extentiondistance/3.5)*0.5)*kP);
            swerveModuleStates =
                SwerveSettings.SwerveConstants.swerveKinematics.toSwerveModuleStates(
                    fieldRelative ? ChassisSpeeds.fromFieldRelativeSpeeds(
                                        translation.getX(), 
                                        translation.getY(), 
                                        isOpenLoop?rotation * Constants.Drivebase.turnRate:controller.calculate(Gyro.getAngle()-90, headingSetPoint), 
                                        Gyro.getHeadingR2D()
                                    )
                                    : new ChassisSpeeds(
                                        translation.getX(), 
                                        translation.getY(),
                                        rotation * Constants.Drivebase.turnRate)
                                    );
        }
        SwerveDriveKinematics.desaturateWheelSpeeds(swerveModuleStates, SwerveSettings.SwerveConstants.maxSpeed);

        for (SwerveMod mod : SwerveMods) {
            mod.setDesiredState(swerveModuleStates[mod.moduleNumber], isOpenLoop);
        }
    }

        
    public Pose2d getPose() {
        return poseEstimator.getEstimatedPosition();
    }

    public void resetOdometry(Pose2d pose) {
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
        if (Constants.isBlue()){
            poseEstimator.update(Rotation2d.fromDegrees(-Gyro.getHeading()+90), getPoses());
        }else{
            poseEstimator.update(Rotation2d.fromDegrees(-Gyro.getHeading()-90), getPoses());
        }
        Optional<EstimatedRobotPose> result = AprilTagCameraWrapper.getEstimatedGlobalPose(poseEstimator.getEstimatedPosition());
        if (result.isPresent()) {
            EstimatedRobotPose camPose = result.get();
            // System.out.println("X: " + camPose.estimatedPose.toPose2d().getX() + " Y: "+camPose.estimatedPose.toPose2d().getY());
            // if (camPose.estimatedPose.toPose2d().getTranslation().getDistance(getPose().getTranslation()) <  Constants.AprilTagCamera.Filter.distance){
            // if(Math.abs(AprilTagCameraWrapper.getHorizontalOffset()) < 12){
                if(Math.abs(Gyro.getPitch()) < Constants.AprilTagCamera.Filter.pitch && Math.abs(Gyro.getRoll()) < Constants.AprilTagCamera.Filter.roll){
                    poseEstimator.addVisionMeasurement(camPose.estimatedPose.toPose2d(), camPose.timestampSeconds);    
                }
            // }
            // }
        }
    }
    public void updateSwerveOdometryNoVision(){
        if (Constants.isBlue()){
            poseEstimator.update(Rotation2d.fromDegrees(-Gyro.getHeading()+90), getPoses());
        }else{
            poseEstimator.update(Rotation2d.fromDegrees(-Gyro.getHeading()-90), getPoses());
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
    
    public void loadTrajectory(String name){
        String trajectoryJSON = "pathplanner/generatedJSON/" + name;
        Path deployDirectory;
        try {
            deployDirectory = Filesystem.getDeployDirectory().toPath().resolve(trajectoryJSON);
            trajectory = TrajectoryUtil.fromPathweaverJson(deployDirectory);
        } catch (IOException ex) {
            DriverStation.reportError("Unable to open trajectory: " + trajectoryJSON, ex.getStackTrace());
        }

        // Json = deployDirectory.toString();


        Gyro.set(0);
        // ahrs.setAngleAdjustment(0);
         //                                                       The robot fields angle (in pathweaver rotation)
        // System.out.println("trajectory: " +trajectory.getInitialPose().getRotation());
        //Rotation2d.fromDegrees(-90)
        poseEstimator.resetPosition(trajectory.getInitialPose().getRotation(), getPoses(), trajectory.getInitialPose());
        // swerveOdometry.resetPosition(trajectory.getInitialPose().getRotation(), getPoses(), trajectory.getInitialPose());
        timer.reset();
        timer.start();
    }

    public boolean followTrajectory(double holonomicRotation){
     
        updateSwerveOdometry();
        Trajectory.State goal = trajectory.sample(timer.get());
                    //                                                          rotation in Path Weaver
        ChassisSpeeds adjustedSpeeds = autoController.calculate(getPose(), goal, Rotation2d.fromDegrees(0));


        adjustedSpeeds.vyMetersPerSecond *=-1;

        ChassisSpeeds adjustedSpeeds2 = new ChassisSpeeds(adjustedSpeeds.vxMetersPerSecond,adjustedSpeeds.vyMetersPerSecond, thetaController.calculate(Gyro.getAngle(), holonomicRotation));

        SwerveModuleState[] swerveModuleStates = SwerveSettings.SwerveConstants.swerveKinematics.toSwerveModuleStates(adjustedSpeeds2);

        SwerveDriveKinematics.desaturateWheelSpeeds(swerveModuleStates, SwerveSettings.SwerveConstants.maxSpeed);

        for (SwerveMod mod : SwerveMods) {
            mod.setDesiredState(swerveModuleStates[mod.moduleNumber], false);
        }
        // System.out.println("Time: " +timer.get());
        if (timer.get() > trajectory.getTotalTimeSeconds()){
            return true;
        }
        return false;
    }
    public boolean displayInformation(){
        Optional<EstimatedRobotPose> result = AprilTagCameraWrapper.getEstimatedGlobalPose(poseEstimator.getEstimatedPosition());
        return result.isPresent() && (Math.abs(Gyro.getPitch()) < Constants.AprilTagCamera.Filter.pitch && Math.abs(Gyro.getRoll()) < Constants.AprilTagCamera.Filter.roll);
    }
}
