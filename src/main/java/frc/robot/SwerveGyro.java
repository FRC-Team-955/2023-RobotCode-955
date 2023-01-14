package frc.robot;

import com.ctre.phoenix.sensors.BasePigeon;
import com.ctre.phoenix.sensors.Pigeon2;
import com.ctre.phoenix.sensors.WPI_Pigeon2; 
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.MathUtil; 

public class SwerveGyro {
Pigeon2 pigeon;
Rotation2d rotation2d; 
double degree = rotation2d.getDegrees();

public SwerveGyro(int Pigeon_ID)
{
pigeon = new Pigeon2(Pigeon_ID);
rotation2d = new Rotation2d(); 
}; 

double GetAngle()
{
return pigeon.getYaw(); 
}; 

double GetYaw()
{
return MathUtil.inputModulus(degree, -180.0, 179.0);
};

double GetHeading()
{
return MathUtil.inputModulus(degree, 0.0, 359.0);
};

};
