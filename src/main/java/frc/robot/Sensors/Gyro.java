package frc.robot.Sensors;
import com.ctre.phoenix.sensors.Pigeon2;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.MathUtil; 

public class Gyro {
    //add gryo id to constants
    static Pigeon2 primary = new Pigeon2(30, "electrical_problem");

    //static Pigeon2 backup = new Pigeon2(0, null);



     /**
     * @return acummulated rotaton -inf - inf
     */
    public static double getAngle() {
        // if ((primary.getFaults(null) == ErrorCode.OK && backup.getFaults(null) == ErrorCode.OK)) {
        //     return (-backup.getYaw() - primary.getYaw()) / 2;
        // }
        // else if (primary.getFaults(null) == ErrorCode.OK) {
            return -primary.getYaw();
        // }
        // else if (primary.getFaults(null) == ErrorCode.OK) {
        //     return -backup.getYaw();
        // }
        // else return 0;
        //return -pigeon.getYaw(); 
    }; 
     /**
     * @return acummulated rotaton -179 - 180
     */

    public static double getPitch() {
        // if ((primary.getFaults(null) == ErrorCode.OK && backup.getFaults(null) == ErrorCode.OK)) {
        //     return (-backup.getPitch() - primary.getPitch()) / 2;
        // }
        // else if (primary.getFaults(null) == ErrorCode.OK) {
            return -primary.getPitch();
        // }
        // else if (primary.getFaults(null) == ErrorCode.OK) {
        //     return -backup.getPitch();
        // }
        // else return 0;
    };

    public static double getRoll() {
        // if ((primary.getFaults(null) == ErrorCode.OK && backup.getFaults(null) == ErrorCode.OK)) {
        //     return (-backup.getRoll() - primary.getRoll()) / 2;
        // }
        // else if (primary.getFaults(null) == ErrorCode.OK) {
            return -primary.getRoll();
        // }
        // else if (primary.getFaults(null) == ErrorCode.OK) {
        //     return -backup.getRoll();
        // }
        // else return 0;
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
};
