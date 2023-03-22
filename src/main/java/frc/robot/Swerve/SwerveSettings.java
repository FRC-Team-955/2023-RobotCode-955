package frc.robot.Swerve;

import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.math.kinematics.SwerveDriveKinematics;

public class SwerveSettings {
    public static final class SwerveConstants {
        /* Angle Motor PID Values */
        // public static final double angleKP = 0.025;
        public static final double angleKP = 0.01;
        public static final double angleKI = 0.0;
        public static final double angleKD = 0.005;
        public static final double angleKF = 0.0;

        /* Drive Motor PID Values */
        public static final double driveKP = 0.1;
        public static final double driveKI = 0.0;
        public static final double driveKD = 0.0;
        public static final double driveKF = 2.96;

        /* Drive Motor Characterization Values */
        public static final double driveKS = (0.32 / 12);
        public static final double driveKV = (1.51 / 12);
        public static final double driveKA = (0.27 / 12);

        public static  boolean canCoderInvert = true;
        public static double maxSpeed = 4.4196;
        public static double maxAngularVelocity = 10.0;
        public static double maxAcceleration = 1;


        public static boolean angleMotorInvert = false;
        public static IdleMode angleIdleMode = IdleMode.kBrake;
        public static boolean driveMotorInvert = false;
        public static IdleMode driveIdleMode = IdleMode.kBrake;

        // public static double wheelCircumference = 0.31919;
        public static double wheelDiameter = 0.1016;
        public static double driveGearRatio = 6.75;

        public static double angleGearRatio = 150/7;

        
        public static double trackWidth = 0.685;
        public static double trackLength = 0.545;
        //From Cad
        // public static double trackWidth = 0.679450;
        // public static double trackLength = 0.552450;
    
        
        public static final edu.wpi.first.math.geometry.Translation2d[] swerveModuleLocations = {
            new edu.wpi.first.math.geometry.Translation2d(trackWidth / 2.0, -trackLength / 2.0),
            new edu.wpi.first.math.geometry.Translation2d(trackWidth / 2.0, trackLength / 2.0),
            new edu.wpi.first.math.geometry.Translation2d(-trackWidth / 2.0, trackLength / 2.0),
            new edu.wpi.first.math.geometry.Translation2d(-trackWidth / 2.0, -trackLength / 2.0)
        };

        public static final SwerveDriveKinematics swerveKinematics = new SwerveDriveKinematics(swerveModuleLocations);

    }

}
