package frc.robot.Swerve;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import java.util.ArrayList;
import edu.wpi.first.wpilibj.SPI;

import com.ctre.phoenix.sensors.Pigeon2;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.HolonomicDriveController;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.IO;
import frc.robot.Sensors.Pigeon;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryUtil;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;

public class SwerveDrive {
    // status variable for being enabled
    public boolean mIsEnabled = false;

    public SwerveDriveOdometry swerveOdometry;
    public SwerveMod[] SwerveMods;
    public double headingSetPoint;
    private PIDController controller = new PIDController(0.7,0,0);

    private PIDController xController = new PIDController(0.7,0,0);
    private PIDController yController = new PIDController(0.7,0,0);
    private PIDController thetaController = new PIDController(0.05,0,0);
    private ProfiledPIDController thetaController2 = new ProfiledPIDController(0.5,0,0, new TrapezoidProfile.Constraints(40, 180));
    public HolonomicDriveController autoController = new HolonomicDriveController(xController, yController, thetaController2);
    public Trajectory trajectory = new Trajectory();
    public Trajectory turningTrajectory = new Trajectory();
    public Timer timer = new Timer();
    public String File = "pathplanner/generatedJSON/CorrectPath.path"; 

    Pigeon pigeon = new Pigeon(0);

    // chassis velocity status
    ChassisSpeeds chassisVelocity = new ChassisSpeeds(); // not used, commented out in updateSwerveOdometry()
    
    
    // boolean to lock Swerve wheels
    public boolean locked = false;

    public SwerveDrive() {       
        pigeon.set(0); 
        
        SwerveMods = new SwerveMod[] {
            //MODULE 0 AND 3 MIGHT BE SLIGHTLY OFF
            new SwerveMod(0, 4, 8, 9, 254.3 - 1.23 + 2.813),
            new SwerveMod(1, 1, 5, 12, 121.4 - 0.88),
            new SwerveMod(2, 3, 2, 11, 33.8 + 0.09),
            new SwerveMod(3, 6, 7, 10, 44.5  + 2.37 - 5.274),
        };

        SwerveModulePosition[] initPoses = new SwerveModulePosition[4];
        for(SwerveMod mod : SwerveMods){
            initPoses[mod.moduleNumber] = mod.getState();
        }
        
        swerveOdometry = new SwerveDriveOdometry(SwerveSettings.SwerveConstants.swerveKinematics, pigeon.getYawR2D(), initPoses, new Pose2d(0.0,0.0,pigeon.getYawR2D()));


    }

    public void drive(Translation2d translation, double rotation, boolean fieldRelative, boolean isOpenLoop, boolean useFixedHeading, double heading) {
        if(IO.rotationOverrideEnabled()){
            rotation = IO.getSwerveRotation();
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
                                        controller.calculate(pigeon.getAngle(), headingSetPoint), 
                                        pigeon.getHeadingR2D() //might need to be 0-360
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
        return swerveOdometry.getPoseMeters();
    }

    public void resetOdometry(Pose2d pose) {
        swerveOdometry.resetPosition(pigeon.getYawR2D(), getPoses(), pose);
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
        swerveOdometry.update(Rotation2d.fromDegrees(-pigeon.getHeading()), getPoses()); //maybe 0-360
        // chassisVelocity = SwerveSettings.SwerveConstants.swerveKinematics.toChassisSpeeds(
        //     SwerveMods[0].getState(),
        //     SwerveMods[1].getState(),
        //     SwerveMods[2].getState(),
        //     SwerveMods[3].getState()
        // );
    }

    // public void loadTrajectory(String name){
    //     String trajectoryJSON = "pathplanner/generatedJSON/" + name;
    //     Path deployDirectory;
    //     try {
    //         deployDirectory = Filesystem.getDeployDirectory().toPath().resolve(trajectoryJSON);
    //         trajectory = TrajectoryUtil.fromPathweaverJson(deployDirectory);
    //     } catch (IOException ex) {
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

    // public float getHeading(){
    //     float raw_yaw = ahrs.getYaw() - (float)offset; // Returns yaw as -180 to +180.
    //     // float raw_yaw = m_ahrs.getHeading(); // Returns yaw as -180 to +180.
    //     float calc_yaw = raw_yaw;
    
    //     if (0.0 > raw_yaw ) { // yaw is negative
    //       calc_yaw += 360.0;
    //     }
    //     return calc_yaw;
    //   }

    //   public void setHeading(){
    //       headingSetPoint = ahrs.getAngle();
    //   }
}
