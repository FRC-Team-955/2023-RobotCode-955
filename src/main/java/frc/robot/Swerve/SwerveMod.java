package frc.robot.Swerve;

import com.ctre.phoenix.sensors.AbsoluteSensorRange;
import com.ctre.phoenix.sensors.CANCoder;
import com.ctre.phoenix.sensors.CANCoderConfiguration;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.util.datalog.DataLog;
import edu.wpi.first.util.datalog.DoubleLogEntry;
import edu.wpi.first.wpilibj.DataLogManager;



public class SwerveMod{
    public int moduleNumber;

    public double angleOffset;

    private CANSparkMax angleMotor;
    private CANCoder angleEncoder;
    private SparkMaxPIDController anglePID;

    private CANSparkMax driveMotor;
    private SparkMaxPIDController drivePID;

    private RelativeEncoder driveEncoder;
    private RelativeEncoder turningEncoder;     

    private double drivekP;
    private double drivekI;
    private double drivekD;

    private DoubleLogEntry driveMotorLog;
    private DoubleLogEntry driveEncoderLog;
    private DoubleLogEntry angleMotorLog;
    private DoubleLogEntry turningEncoderLog;

    SimpleMotorFeedforward feedforward = new SimpleMotorFeedforward(SwerveSettings.SwerveConstants.driveKS, SwerveSettings.SwerveConstants.driveKV, SwerveSettings.SwerveConstants.driveKA);

    public SwerveMod(int moduleNumber, int driveMotorID,  int angleMotorID,int cancoderID, double angleOffset, String key){
        this.moduleNumber = moduleNumber;
        this.angleOffset = angleOffset;

        // Angle Motor
        angleMotor = new CANSparkMax(angleMotorID, MotorType.kBrushless);
        angleMotor.setInverted(SwerveSettings.SwerveConstants.angleMotorInvert);
        angleMotor.setIdleMode(SwerveSettings.SwerveConstants.angleIdleMode);

        // Drive Motor
        driveMotor = new CANSparkMax(driveMotorID, MotorType.kBrushless);
        driveMotor.setInverted(SwerveSettings.SwerveConstants.driveMotorInvert);
        driveMotor.setIdleMode(SwerveSettings.SwerveConstants.driveIdleMode);   

        // Absolute Encoders
        angleEncoder = new CANCoder(cancoderID);
        CANCoderConfiguration config = new CANCoderConfiguration();
        config.absoluteSensorRange = AbsoluteSensorRange.Unsigned_0_to_360;
        config.sensorDirection = SwerveSettings.SwerveConstants.canCoderInvert;
        angleEncoder.configAllSettings(config);      

        

        //Relative Encoders
        driveEncoder = driveMotor.getEncoder();
        turningEncoder = angleMotor.getEncoder();

        turningEncoder.setPositionConversionFactor(16.845);   //360.0 / 12.8

        // driveEncoder returns RPM by default. Use setVelocityConversionFactor() to
        // convert that to meters per second.
        driveEncoder.setVelocityConversionFactor((1.0217 * 0.04284 * 1.098*(7.9544/7.29234))/ 60.0);
        driveEncoder.setPositionConversionFactor(0.04284 * 1.0217 * 1.098*(7.9544/7.29234)); //(0.098 * Math.PI) / 6.75
        driveEncoder.setPosition(0);

        // Angle PID
        anglePID = angleMotor.getPIDController();
        anglePID.setP(SwerveSettings.SwerveConstants.angleKP);
        anglePID.setI(SwerveSettings.SwerveConstants.angleKI);
        anglePID.setD(SwerveSettings.SwerveConstants.angleKD);

        // // Drive Encoder
        // driveEncoder = driveMotor.getAlternateEncoder(42);
        // driveEncoder = driveMotor.getEncoder();
        

        // Drive PID
        drivePID = driveMotor.getPIDController();
        
        // Uh just FYI I think these are all 0 but it works so ¯\_(ツ)_/¯
        drivePID.setP(drivekP);
        drivePID.setI(drivekI);
        drivePID.setD(drivekD);

        // Setup Datalogging
        DataLog log = DataLogManager.getLog();
        driveMotorLog = new DoubleLogEntry(log, "/swerve/driveMotor/"+key);
        driveEncoderLog = new DoubleLogEntry(log, "/swerve/driveEncoder/"+key);
        angleMotorLog = new DoubleLogEntry(log, "/swerve/angleMotor/"+key);
        turningEncoderLog = new DoubleLogEntry(log, "/swerve/turningEncoder/"+key);
    }

    public double deltaAdjustedAngle(double targetAngle, double currentAngle) {
        return ((targetAngle - currentAngle + 180) % 360 + 360) % 360 - 180;
    }

    public void logSwerveMod() {
        driveMotorLog.append(driveMotor.getOutputCurrent());
        driveEncoderLog.append(driveEncoder.getPosition());
        angleMotorLog.append(angleMotor.getOutputCurrent());
        turningEncoderLog.append(turningEncoder.getPosition());
    }

    public void setDesiredState(SwerveModuleState state) {
        Rotation2d curAngle = Rotation2d.fromDegrees(turningEncoder.getPosition());
        double delta = deltaAdjustedAngle(state.angle.getDegrees(), curAngle.getDegrees());
        //To figure out offset for absolute encoders
        // System.out.println(moduleNumber + ": " + angleEncoder.getAbsolutePosition());

        // Calculate the drive motor output from the drive PID controller.
        double driveOutput = state.speedMetersPerSecond;

        if (Math.abs(delta) > 90) {
            driveOutput *= -1;
            delta -= Math.signum(delta) * 180;
        }

        adjustedAngle = Rotation2d.fromDegrees(delta + curAngle.getDegrees());

        anglePID.setReference(
            adjustedAngle.getDegrees(),
            ControlType.kPosition
        );        

        drivePID.setReference(driveOutput, ControlType.kVelocity, 0, 2.96 * driveOutput);
    }
    
    public void setOpenLoopState(SwerveModuleState state) {
        Rotation2d curAngle = Rotation2d.fromDegrees(turningEncoder.getPosition());

        double delta = deltaAdjustedAngle(state.angle.getDegrees(), curAngle.getDegrees());

        // Calculate the drive motor output from the drive PID controller.
        double driveOutput = state.speedMetersPerSecond;

        if (Math.abs(delta) > 90) {
            driveOutput *= -1;
            delta -= Math.signum(delta) * 180;
        }

        adjustedAngle = Rotation2d.fromDegrees(delta + curAngle.getDegrees());

        anglePID.setReference(
            adjustedAngle.getDegrees(),
            ControlType.kPosition
        );        

        driveMotor.setVoltage(2.96 * driveOutput);
    }


    public void syncEncoders(){
        turningEncoder.setPosition(angleEncoder.getAbsolutePosition()-angleOffset);
    }

    public void resetToAbsolute(){
        // Reset the cumulative rotation counts of the SparkMax motors
        turningEncoder.setPosition(0.0);
        angleEncoder.setPosition(0.0);
    }

    public void resetDrive(){
        driveEncoder.setPosition(0.0);
    }

    public Rotation2d getCanCoder(){
        return Rotation2d.fromDegrees(angleEncoder.getAbsolutePosition());
    }


    public SwerveModulePosition getState(){
        double m2 = -(turningEncoder.getPosition() % 360 + 360) % 360;
        return new SwerveModulePosition(driveEncoder.getPosition(), new Rotation2d(m2 * Math.PI / 180));
    }

    public Rotation2d adjustedAngle = new Rotation2d();     

}
