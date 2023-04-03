package frc.robot.Sensors;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.sensors.*;
import com.ctre.phoenix.sensors.Pigeon2;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.SPI.Port;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.math.MathUtil; 

public class Gyro {
    //add gryo id to constants
    static Pigeon2 primary = new Pigeon2(30, "electrical_problem");

    static AHRS backup = new AHRS(Port.kMXP);

    //static Pigeon2 backup = new Pigeon2(0, null);
    public enum GyroStatus {
        GOOD,
        BACKUP,
        FAILIURE
    }

    public static GyroStatus gyroStatus;

     /**
     * @return acummulated rotaton -inf - inf
     */
    public static double getAngle() {
        if (primary.getFaults(null) == ErrorCode.OK) {
            gyroStatus = GyroStatus.GOOD;
            return -primary.getYaw();
        }
        else if (backup.isConnected()) {
            gyroStatus = GyroStatus.BACKUP;
            return (double) backup.getYaw();
        }
        else {
            gyroStatus = GyroStatus.FAILIURE;
            onFailiure();
            return 0;
        }
    }; 
     /**
     * @return acummulated rotaton -179 - 180
     */

    public static double getPitch() {
        if (primary.getFaults(null) == ErrorCode.OK) {
            gyroStatus = GyroStatus.GOOD;
            return -primary.getPitch();
        }
        else if (backup.isConnected()) {
            gyroStatus = GyroStatus.BACKUP;
            return (double) backup.getPitch();
        }
        else {
            gyroStatus = GyroStatus.FAILIURE;
            onFailiure();
            return 0;
        }
    };

    public static double getRoll() {
        if (primary.getFaults(null) == ErrorCode.OK) {
            gyroStatus = GyroStatus.GOOD;
            return -primary.getRoll();
        }
        else if (backup.isConnected()) {
            gyroStatus = GyroStatus.BACKUP;
            return (double) backup.getRoll();
        }
        else {
            gyroStatus = GyroStatus.FAILIURE;
            onFailiure();
            return 0;
        }
    };

    public static double getYaw() {
        return MathUtil.inputModulus(getAngle(), -180.0, 179.0);
        // Alternative
        // double angle = pigeon.getYaw();
        // angle = angle % 360;
        // if(angle > 180){
        //     angle -= 360;
        // }
        // return angle;
    };

     /**
     * @return acummulated rotaton 0 - 359
     */
    public static double getHeading() {
        return MathUtil.inputModulus(getAngle(), 0.0, 359.0);
        // Alternative
        // double angle = pigeon.getYaw();
        // return angle % 360;
    };
     /**
     * @return acummulated rotaton -179 - 180
     */
    public static Rotation2d getYawR2D(){
        return Rotation2d.fromDegrees(getYaw());
    }

     /**
     * @return acummulated rotaton -inf - inf
     */
    public static Rotation2d getAngleR2D(){
        return Rotation2d.fromDegrees(getAngle());
    }
    
     /**
     * @return acummulated rotaton 0 - 359
     */
    public static Rotation2d getHeadingR2D(){
        return Rotation2d.fromDegrees(getHeading());
    }

    public static Rotation2d getPitchR2D(){
        return Rotation2d.fromDegrees(getPitch()); 
    }

    public static void reset(){
        primary.setYaw(0);
        //backup.setYaw(0);
    }

    public static void set(double offset){
        primary.setYaw(offset);
        //backup.setYaw(offset);
    }
    public static void displayInformation(){
        // SmartDashboard.putNumber("roll", getRoll());
        // SmartDashboard.putNumber("pitch", getPitch());
    }

    public static void onFailiure() {

    }
};
