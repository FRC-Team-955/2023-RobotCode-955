package frc.robot.Auto.Actions;

import org.opencv.core.Core;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.AutoAlign;
import frc.robot.Constants;
import frc.robot.Drivebase;
import frc.robot.Auto.AutoProfile.Team;
import frc.robot.Sensors.Gyro;

public class BalanceAction extends AutoAction {

    public boolean startOutOfCommunity;

    public boolean finishes;

    boolean prep = true;
    boolean correct = false;

    public boolean Act() {
        if (prep) {
            prep = !AutoAlign.alignOdometry((startOutOfCommunity) ? (
                (profile.team == Team.Red) ? Constants.FieldPositions.AutoAlignPositions.redFieldChargePrep : Constants.FieldPositions.AutoAlignPositions.blueFieldChargePrep) :
                ((profile.team == Team.Red) ? Constants.FieldPositions.AutoAlignPositions.red4 : Constants.FieldPositions.AutoAlignPositions.blue4), -180);
        }
        else {
            if (correct) {
                if (Constants.isBlue()?Drivebase.getPose().getX()>Constants.FieldPositions.AutoAlignPositions.chargeStationBlue.getX():
                    Drivebase.getPose().getX()<Constants.FieldPositions.AutoAlignPositions.chargeStationRed.getX()) {
                    correct = true;
                } else {
                    // Drivebase.driveFieldRelativeHeading(new Translation2d(0,-2), -180);
                    Drivebase.driveRobotRelativeRotation(new Translation2d(2,0), 0);
                }
            }
            else {
                SmartDashboard.putNumber("pitch", Gyro.getPitch());
                SmartDashboard.putNumber("roll", Gyro.getRoll());
                SmartDashboard.putBoolean("Is balanced", Drivebase.isBalanced());
                Drivebase.autoBalance();
                return (finishes && Math.abs(Gyro.getPitch()) < 2);
            }
        }

        return false;
    }
    
    public void Finish() {
        
    }

    public BalanceAction() {}
}
