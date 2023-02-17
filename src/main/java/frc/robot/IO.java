package frc.robot;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.Joystick;
import frc.robot.Swerve.SwerveSettings;

public final class IO {
    private static Joystick joy0 = new Joystick(Constants.IO.Joy0.joy0Id);
    private static Joystick joy1 = new Joystick(Constants.IO.Joy1.joy1Id);
    private static SlewRateLimiter forwardAxisSlewRateLimiter = new SlewRateLimiter(Constants.forwardRateLimiter);
    private static SlewRateLimiter strafeAxisSlewRateLimiter = new SlewRateLimiter(Constants.strafeRateLimiter);
    
    private static Joystick key0 = new Joystick(2);
    private static Joystick key1 = new Joystick(3);
    private static Joystick key2 = new Joystick(4);

    private static boolean override = false;

    public static void setOverride(boolean _override) {
        override = _override;
    }

    public static boolean isOverrideEnabled() {
        return override;
    }

    public static boolean elevatorManualUp(){
        return joy1.getRawButtonPressed(Constants.IO.Joy1.elevatorUpButton) || key1.getRawButtonPressed(13);
    }
    public static boolean elevatorManualDown(){
        return joy1.getRawButtonPressed(Constants.IO.Joy1.elevatorDownButton) || key1.getRawButtonPressed(14);
    }
    public static boolean clawDropPiece(){
        return joy1.getRawButtonPressed(Constants.IO.Joy1.clawDropPieceButton) || key1.getRawButtonPressed(15);
    }

    public static double armOverride() {
        return joy1.getRawAxis(Constants.IO.Joy1.armOverrideAxis);
    }
      
    public static double elevatorOverride(){
        return joy1.getRawAxis(Constants.IO.Joy1.elevatorOverrideAxis);
    }

    public static boolean intakeDeployRun(){
        return joy1.getRawAxis(Constants.IO.Joy1.deployRunIntakeAxis) > 0.2 || key1.getRawButtonPressed(16);
    }

    public static class Drivebase{
        public static boolean isAutoAlignActive() {
            return joy0.getRawAxis(Constants.IO.Joy0.autoAlignAxis) > 0.2;
        }

        public static boolean isAutoBalanceActive(){
            return joy0.getRawButtonPressed(Constants.IO.Joy0.autoBalanceButton);
        }
  
        public static double getSwerveRotation(){
            //What is axis number reffering too?
            double rotAxis = joy0.getRawAxis(Constants.IO.Joy0.rotAxis);
            double deadband = 0.15;

            if (Math.abs(rotAxis) < Math.abs(deadband)) return 0.0;
            
            return deadband * Math.signum(rotAxis) + ((rotAxis - deadband) / (1.0 - deadband));

            // if (Math.abs(rotAxis) < 0.15) {
            //     return 0.0;
            // } else {
            //     return SwerveSettings.SwerveConstants.maxAngularVelocity * (rotAxis - (Math.signum(rotAxis) * 0.15)) / (1 - 0.15);
            // }
        }

        private static double norm(Translation2d tAxes) {
            return Math.hypot(tAxes.getX(), tAxes.getY());
        }

        private static Translation2d scale(double s, Translation2d tAxes) {
            return new Translation2d(tAxes.getX() * s, tAxes.getY() * s);
        }

        private static Translation2d fromPolar(Rotation2d direction, double magnitude){
            return new Translation2d(direction.getCos() * magnitude, direction.getSin() * magnitude);
        }

        public static Translation2d getSwerveTranslation(){
            double forwardRawAxis = joy0.getRawAxis(Constants.IO.Joy0.forwardRawAxis);
            double strafeRawAxis = joy0.getRawAxis(Constants.IO.Joy0.strafeRawAxis);
            // double forwardAxis = forwardAxisSlewRateLimiter.calculate(forwardRawAxis);
            // // double strafeAxis = strafeAxisSlewRateLimiter.calculate(strafeRawAxis);

        Translation2d tAxes = new Translation2d(thrustEnabled() ? forwardRawAxis : forwardRawAxis*0.7, thrustEnabled() ? strafeRawAxis : strafeRawAxis*0.7);

            if (Math.abs(norm(tAxes)) < 0.15) {
                return new Translation2d();
            } else {
                Rotation2d deadband_direction = new Rotation2d(tAxes.getX(), tAxes.getY());
                Translation2d deadband_vector = fromPolar(deadband_direction, 0.15);

                double scaled_x = tAxes.getX() - (deadband_vector.getX()) / (1 - deadband_vector.getX());
                double scaled_y = tAxes.getY() - (deadband_vector.getY()) / (1 - deadband_vector.getY());
                return scale(SwerveSettings.SwerveConstants.maxSpeed, new Translation2d(scaled_x, scaled_y));
            }
        }

        public static boolean rotationOverrideEnabled(){
            return joy0.getRawButton(Constants.IO.Joy0.rotationOverrideButton);
        }
        public static boolean thrustEnabled(){
            return joy0.getRawAxis(Constants.IO.Joy0.thrustAxis) > 0.2;
        }
    }
    public static void rumbleJoy0(){
        joy0.setRumble(RumbleType.kBothRumble, 0.5);
    }
    public static void rumbleJoy1(){
        joy1.setRumble(RumbleType.kBothRumble, 0.5);
    }

    public static Translation2d keyInputOdometryPosition = Constants.isBlue()? Constants.FieldPositions.AutoAlignPositions.blue0: Constants.FieldPositions.AutoAlignPositions.red0;
    public static Translation2d keyInputSubstationLocation;
    public static boolean isConeNodePosition = true;

    public static enum GridArmPosition{
        Retract,
        ConePrep,
        ConeReady,
        CubePrep,
        CubeReady,
        DoubleSubstation
    }
    public static GridArmPosition gridArmPosition = GridArmPosition.Retract;
    
    public static enum GridRowPosition{
        Retract,
        Low,
        Mid,
        High,
        DoubleSubstation
    }
    private static int gridColumnPosition = 0;
    private static int newGridColumnPosition = 0;
    public static GridRowPosition gridRowPosition = GridRowPosition.Low;

    public static void keyInputRowPosition(){

        if(key0.getRawButtonPressed(1) || key0.getRawButtonPressed(4)|| key0.getRawButtonPressed(7) || key0.getRawButtonPressed(10)|| key0.getRawButtonPressed(13)
        || key1.getRawButtonPressed(1) || key1.getRawButtonPressed(4) || key1.getRawButtonPressed(7) || key1.getRawButtonPressed(10)){
                gridRowPosition = GridRowPosition.Low;
        }
        else if( key0.getRawButtonPressed(2) || key0.getRawButtonPressed(5) || key0.getRawButtonPressed(8) || key0.getRawButtonPressed(11) || key0.getRawButtonPressed(14)
                || key1.getRawButtonPressed(2) || key1.getRawButtonPressed(5) ||key1.getRawButtonPressed(8) || key1.getRawButtonPressed(11)){
                gridRowPosition = GridRowPosition.Mid;
        }
        else if( key0.getRawButtonPressed(3) || key0.getRawButtonPressed(6) || key0.getRawButtonPressed(9) || key0.getRawButtonPressed(12) || key0.getRawButtonPressed(15)
                || key1.getRawButtonPressed(3) || key1.getRawButtonPressed(6) ||key1.getRawButtonPressed(9) || key1.getRawButtonPressed(12)){
                gridRowPosition = GridRowPosition.High;
        }
    }

    public static void keyInputOdometryMapping() {
        //1
        if (key0.getRawButtonPressed(1) || key0.getRawButtonPressed(2) || key0.getRawButtonPressed(3)){
            keyInputOdometryPosition = Constants.isBlue()? Constants.FieldPositions.AutoAlignPositions.blue0: Constants.FieldPositions.AutoAlignPositions.red0;
            isConeNodePosition = true;
            newGridColumnPosition = 0;
        }
        //2
        if (key0.getRawButtonPressed(4) || key0.getRawButtonPressed(5) || key0.getRawButtonPressed(6)){
            keyInputOdometryPosition = Constants.isBlue()? Constants.FieldPositions.AutoAlignPositions.blue1: Constants.FieldPositions.AutoAlignPositions.red1;
            isConeNodePosition = false;
            newGridColumnPosition = 1;
        }
        //3
        if (key0.getRawButtonPressed(7) || key0.getRawButtonPressed(8) || key0.getRawButtonPressed(9)){
            keyInputOdometryPosition = Constants.isBlue()? Constants.FieldPositions.AutoAlignPositions.blue2: Constants.FieldPositions.AutoAlignPositions.red3;
            isConeNodePosition = true;
            newGridColumnPosition = 2;
        }
        //4
        if (key0.getRawButtonPressed(10) || key0.getRawButtonPressed(11) || key0.getRawButtonPressed(12)){
            keyInputOdometryPosition = Constants.isBlue()? Constants.FieldPositions.AutoAlignPositions.blue3: Constants.FieldPositions.AutoAlignPositions.red3;;
            isConeNodePosition = true;
            newGridColumnPosition = 3;
        }
        //5
        if (key0.getRawButtonPressed(13) || key0.getRawButtonPressed(14) || key0.getRawButtonPressed(15)){
            keyInputOdometryPosition = Constants.isBlue()? Constants.FieldPositions.AutoAlignPositions.blue4: Constants.FieldPositions.AutoAlignPositions.red4;
            isConeNodePosition = false;
            newGridColumnPosition = 4;
        }
        //6
        if (key1.getRawButtonPressed(1) || key1.getRawButtonPressed(2) || key1.getRawButtonPressed(3)){
            keyInputOdometryPosition = Constants.isBlue()? Constants.FieldPositions.AutoAlignPositions.blue5: Constants.FieldPositions.AutoAlignPositions.red5;
            isConeNodePosition = true;
            newGridColumnPosition = 5;
        }
        //7
        if (key1.getRawButtonPressed(4) || key1.getRawButtonPressed(5) || key1.getRawButtonPressed(6)){
            keyInputOdometryPosition = Constants.isBlue()? Constants.FieldPositions.AutoAlignPositions.blue6: Constants.FieldPositions.AutoAlignPositions.red6;
            isConeNodePosition = true;
            newGridColumnPosition = 6;
        }
        //8
        if (key1.getRawButtonPressed(7) || key1.getRawButtonPressed(8) || key1.getRawButtonPressed(9)){
            keyInputOdometryPosition = Constants.isBlue()? Constants.FieldPositions.AutoAlignPositions.blue7: Constants.FieldPositions.AutoAlignPositions.red7;
            isConeNodePosition = false;
            newGridColumnPosition = 7;
        }
        //9
        if (key1.getRawButtonPressed(10) || key1.getRawButtonPressed(11) || key1.getRawButtonPressed(12)){
            keyInputOdometryPosition = Constants.isBlue()? Constants.FieldPositions.AutoAlignPositions.blue8: Constants.FieldPositions.AutoAlignPositions.red8;
            isConeNodePosition = true;
            newGridColumnPosition = 8;
        }
        if (!(newGridColumnPosition == gridColumnPosition)){
            AutoAlign.gridAlignState = AutoAlign.GridAlignState.AlignedToOdometry;
            gridColumnPosition = newGridColumnPosition;
        }
    }
    public static void keyInputSubstationLocation(){
        if (key2.getRawButtonPressed(4)){
            if (Constants.isBlue()){
                keyInputSubstationLocation = Constants.FieldPositions.AutoAlignPositions.blueLeftDoubleSubstation;
            } else if (Constants.isRed()){
                keyInputSubstationLocation = Constants.FieldPositions.AutoAlignPositions.redLeftDoubleSubstation;
            }
        }else if (key2.getRawButtonPressed(5)){
            if (Constants.isBlue()){
                keyInputSubstationLocation = Constants.FieldPositions.AutoAlignPositions.blueRightDoubleSubstation;
            } else if (Constants.isRed()){
                keyInputSubstationLocation = Constants.FieldPositions.AutoAlignPositions.redRightDoubleSubstation;
            }
        }
    }

}
