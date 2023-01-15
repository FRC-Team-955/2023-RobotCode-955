package frc.robot.Sesnsors;

import com.ctre.phoenix.sensors.Pigeon2;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.MathUtil; 

public class Gyro {
    Pigeon2 pigeon;
    public Gyro(int id) {
        pigeon = new Pigeon2(id);
    }; 

     /**
     * @return acummulated rotaton -inf - inf
     */
    public double getAngle() {
        return pigeon.getYaw(); 
    }; 
     /**
     * @return acummulated rotaton -179 - 180
     */
    public double getYaw() {
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
    public double getHeading() {
        return MathUtil.inputModulus(getAngle(), 0.0, 359.0);
        // Alternative
        // double angle = pigeon.getYaw();
        // return angle % 360;
    };

     /**
     * @return acummulated rotaton -179 - 180
     */
    public Rotation2d getYawR2D(){
        return Rotation2d.fromDegrees(getYaw());
    }

     /**
     * @return acummulated rotaton -inf - inf
     */
    public Rotation2d getAngleR2D(){
        return Rotation2d.fromDegrees(getAngle());
    }
    
     /**
     * @return acummulated rotaton 0 - 359
     */
    public Rotation2d getHeadingR2D(){
        return Rotation2d.fromDegrees(getHeading());
    }

    public void reset(){
        pigeon.setYaw(0);
    }


    public void set(double offset){
        pigeon.setYaw(offset);
    }
};
