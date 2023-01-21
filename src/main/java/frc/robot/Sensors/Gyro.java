package frc.robot.Sensors;

import com.ctre.phoenix.sensors.Pigeon2; 
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.MathUtil; 

public class Gyro {
    static Pigeon2 pigeon = new Pigeon2(0);

     /**
     * @return acummulated rotaton -inf - inf
     */
    public static double getAngle() {
        return pigeon.getYaw(); 
    }; 
     /**
     * @return acummulated rotaton -179 - 180
     */

    public static double getPitch() {
        return pigeon.getPitch(); 
    };

    public static double getRoll() {
        return pigeon.getRoll();
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
        pigeon.setYaw(0);
    }


    public static void set(double offset){
        pigeon.setYaw(offset);
    }
};
