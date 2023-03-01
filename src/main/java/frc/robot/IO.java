package frc.robot;

// import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Swerve.SwerveSettings;

public final class IO {
    private static Joystick joy0 = new Joystick(Constants.IO.Joy0.joy0Id);
    private static Joystick joy1 = new Joystick(Constants.IO.Joy1.joy1Id);
    // private static SlewRateLimiter forwardAxisSlewRateLimiter = new SlewRateLimiter(Constants.forwardRateLimiter);
    // private static SlewRateLimiter strafeAxisSlewRateLimiter = new SlewRateLimiter(Constants.strafeRateLimiter);
    
    private static Joystick key0 = new Joystick(2);
    private static Joystick key1 = new Joystick(3);
    private static Joystick key2 = new Joystick(4);

    public static boolean getButton() {
        return joy0.getRawButton(2);
    }

    // public static double handoffOverride() {
    //     return joy0.getRawAxis(4);
    // }

    // public static double intakeOverride() {
    //     return joy0.getRawAxis(0);
    // }

    public static double elevatorFineControl(){
        return -joy1.getRawAxis(Constants.IO.Joy1.elevatorOverrideAxis);
    }

    public static double armFineControl(){
        return joy1.getRawAxis(Constants.IO.Joy1.armOverrideAxis);
    }
    public static boolean resetGyroAngle(){
        return joy0.getRawButtonPressed(8);
    }

    private static boolean override = false;

    public static void setOverride(boolean _override) {
        override = _override;
    }

    public static boolean isOverrideEnabled() {
        return override;
    }
    public static boolean elevatorManualDown(){
        return joy1.getRawButtonPressed(Constants.IO.Joy1.elevatorDownButton) || key2.getRawButtonPressed(1);
    }
    public static boolean elevatorManualUp(){
        return joy1.getRawButtonPressed(Constants.IO.Joy1.elevatorUpButton) || key2.getRawButtonPressed(2);
    }
    public static boolean elevatorManualRetract(){
        return key2.getRawButton(9);
    }
    public static boolean clawDropPiece(){
        return key2.getRawButton(3) || joy0.getRawButton(1);
        //joy1.getRawAxis(Constants.IO.Joy1.clawDropPieceAxis)>0.2 ||
        // return joy1.getRawAxis(Constants.IO.Joy1.clawDropPieceAxis)>0.2;
    }

    public static double armOverride() {
        return joy1.getRawAxis(Constants.IO.Joy1.armOverrideAxis);
    }
      
    public static double elevatorOverride(){
        return joy1.getRawAxis(Constants.IO.Joy1.elevatorOverrideAxis);
    }

    public static boolean intakeSequence(){
        return key2.getRawButton(4);
        // return joy1.getRawAxis(Constants.IO.Joy1.deployRunIntakeAxis) < 0.2 || key2.getRawButtonPressed(4);
    }
    public static boolean runIntakeIn(){
        return key2.getRawButton(7);
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

            rotAxis = thrustEnabled() ? rotAxis : rotAxis*Constants.Drivebase.speed;

            if (Math.abs(rotAxis) < Math.abs(deadband)) return 0.0;
            
            // return deadband * Math.signum(rotAxis) + ((rotAxis - deadband) / (1.0 - deadband));

            if (Math.abs(rotAxis) < 0.15) {
                return 0.0;
            } else {
                return SwerveSettings.SwerveConstants.maxAngularVelocity * (rotAxis - (Math.signum(rotAxis) * 0.15)) / (1 - 0.15);
            }
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
        public static double xBump(){
            return joy0.getRawAxis(Constants.IO.Joy0.strafeRawAxis);
        }
        public static Translation2d getSwerveTranslation(){

            double forwardRawAxis = joy0.getRawAxis(Constants.IO.Joy0.forwardRawAxis);
            double strafeRawAxis = joy0.getRawAxis(Constants.IO.Joy0.strafeRawAxis);
            // double forwardAxis = forwardAxisSlewRateLimiter.calculate(forwardRawAxis);
            // // double strafeAxis = strafeAxisSlewRateLimiter.calculate(strafeRawAxis);

            Translation2d tAxes = new Translation2d(thrustEnabled() ? forwardRawAxis : forwardRawAxis*Constants.Drivebase.speed, thrustEnabled() ? strafeRawAxis : strafeRawAxis*Constants.Drivebase.speed);


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

        public static boolean autoHeadingEnabled(){
            return joy0.getRawButton(Constants.IO.Joy0.autoHeadingButton);
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
    public static Translation2d keyInputSubstationPosition = Constants.isBlue()? Constants.FieldPositions.AutoAlignPositions.blueLeftDoubleSubstation: Constants.FieldPositions.AutoAlignPositions.redLeftDoubleSubstation;
    public static boolean isConeNodePosition = true;

    public static enum GridArmPosition{
        Retract,
        ConePrep,
        ConeReady,
        CubePrep,
        CubeReady,
        SingleSubstation,
        DoubleSubstation,
        Hybrid,
        Up
    }
    public static GridArmPosition gridArmPosition = GridArmPosition.Retract;
    
    public static enum GridRowPosition{
        Retract,
        Low,
        Mid,
        High,
        SingleSubstation,
        DoubleSubstation
    }
    private static int gridColumnPosition = 0;
    private static int newGridColumnPosition = 0;
    public static GridRowPosition gridRowPosition = GridRowPosition.Low;

    public static void keyInputRowPosition(){
        if(key0.getRawButton(1) || key0.getRawButton(4)|| key0.getRawButton(7) || key0.getRawButton(10)|| key0.getRawButton(13)
        || key1.getRawButton(1) || key1.getRawButton(4) || key1.getRawButton(7) || key1.getRawButton(10)){
                gridRowPosition = GridRowPosition.Retract;

        }
        else if( key0.getRawButton(2) || key0.getRawButton(5) || key0.getRawButton(8) || key0.getRawButton(11) || key0.getRawButton(14)
                || key1.getRawButton(2) || key1.getRawButton(5) ||key1.getRawButton(8) || key1.getRawButton(11)){
                gridRowPosition = GridRowPosition.Retract;

        }
        else if( key0.getRawButton(3) || key0.getRawButton(6) || key0.getRawButton(9) || key0.getRawButton(12) || key0.getRawButton(15)
                || key1.getRawButton(3) || key1.getRawButton(6) ||key1.getRawButton(9) || key1.getRawButton(12)){
                gridRowPosition = GridRowPosition.High;
        }
    }

    public static void keyInputOdometryMapping() {
        //1
        if (key0.getRawButton(1)){
            keyInputOdometryPosition = Constants.isBlue()? Constants.FieldPositions.AutoAlignPositions.blue0: Constants.FieldPositions.AutoAlignPositions.red0;
            isConeNodePosition = true;
            gridArmPosition = GridArmPosition.Hybrid;
            newGridColumnPosition = 0;
        }
        if (key0.getRawButton(2) || key0.getRawButton(3)){
            keyInputOdometryPosition = Constants.isBlue()? Constants.FieldPositions.AutoAlignPositions.blue0: Constants.FieldPositions.AutoAlignPositions.red0;
            isConeNodePosition = true;
            gridArmPosition = GridArmPosition.ConePrep;
            newGridColumnPosition = 0;
        }
        //2
        if(key0.getRawButton(4)){
            keyInputOdometryPosition = Constants.isBlue()? Constants.FieldPositions.AutoAlignPositions.blue1: Constants.FieldPositions.AutoAlignPositions.red1;
            isConeNodePosition = true;
            gridArmPosition = GridArmPosition.Hybrid;
            newGridColumnPosition = 1;     
        }

        if (key0.getRawButton(5) || key0.getRawButton(6)){
            keyInputOdometryPosition = Constants.isBlue()? Constants.FieldPositions.AutoAlignPositions.blue1: Constants.FieldPositions.AutoAlignPositions.red1;
            isConeNodePosition = false;
            gridArmPosition = GridArmPosition.CubePrep;
            newGridColumnPosition = 1;
        }

        //3
        if(key0.getRawButton(7)){ 
            keyInputOdometryPosition = Constants.isBlue()? Constants.FieldPositions.AutoAlignPositions.blue2: Constants.FieldPositions.AutoAlignPositions.red2;
            isConeNodePosition = true;
            gridArmPosition = GridArmPosition.Hybrid;
            newGridColumnPosition = 2;   
        } 
        if (key0.getRawButton(8) || key0.getRawButton(9)){
            keyInputOdometryPosition = Constants.isBlue()? Constants.FieldPositions.AutoAlignPositions.blue2: Constants.FieldPositions.AutoAlignPositions.red2;
            isConeNodePosition = true;
            gridArmPosition = GridArmPosition.ConePrep;
            newGridColumnPosition = 2;
        }
        //4
        if(key0.getRawButton(10)){ 
            keyInputOdometryPosition = Constants.isBlue()? Constants.FieldPositions.AutoAlignPositions.blue3: Constants.FieldPositions.AutoAlignPositions.red3;
            isConeNodePosition = true;
            gridArmPosition = GridArmPosition.Hybrid;
            newGridColumnPosition = 3;    
        } 
        if (key0.getRawButton(11) || key0.getRawButton(12)){
            keyInputOdometryPosition = Constants.isBlue()? Constants.FieldPositions.AutoAlignPositions.blue3: Constants.FieldPositions.AutoAlignPositions.red3;
            isConeNodePosition = true;
            gridArmPosition = GridArmPosition.ConePrep;
            newGridColumnPosition = 3;
        }
        //5
        if(key0.getRawButton(13)){ 
            keyInputOdometryPosition = Constants.isBlue()? Constants.FieldPositions.AutoAlignPositions.blue4: Constants.FieldPositions.AutoAlignPositions.red4;
            isConeNodePosition = true;
            gridArmPosition = GridArmPosition.Hybrid;
            newGridColumnPosition = 4;    
        } 
        if (key0.getRawButton(14) || key0.getRawButton(15)){
            keyInputOdometryPosition = Constants.isBlue()? Constants.FieldPositions.AutoAlignPositions.blue4: Constants.FieldPositions.AutoAlignPositions.red4;
            isConeNodePosition = false;
            gridArmPosition = GridArmPosition.CubePrep;
            newGridColumnPosition = 4;
        }
        //6
        if(key1.getRawButton(1)){
            keyInputOdometryPosition = Constants.isBlue()? Constants.FieldPositions.AutoAlignPositions.blue5: Constants.FieldPositions.AutoAlignPositions.red5;
            isConeNodePosition = true;
            gridArmPosition = GridArmPosition.Hybrid;
            newGridColumnPosition = 5;   
        }
        if (key1.getRawButton(2) || key1.getRawButton(3)){
            keyInputOdometryPosition = Constants.isBlue()? Constants.FieldPositions.AutoAlignPositions.blue5: Constants.FieldPositions.AutoAlignPositions.red5;
            isConeNodePosition = true;
            gridArmPosition = GridArmPosition.ConePrep;
            newGridColumnPosition = 5;
        }
        //7
        if(key1.getRawButton(4)){
            keyInputOdometryPosition = Constants.isBlue()? Constants.FieldPositions.AutoAlignPositions.blue6: Constants.FieldPositions.AutoAlignPositions.red6;
            isConeNodePosition = true;
            gridArmPosition = GridArmPosition.Hybrid;
            newGridColumnPosition = 6;
        }
        if (key1.getRawButton(5) || key1.getRawButton(6)){
            keyInputOdometryPosition = Constants.isBlue()? Constants.FieldPositions.AutoAlignPositions.blue6: Constants.FieldPositions.AutoAlignPositions.red6;
            isConeNodePosition = true;
            gridArmPosition = GridArmPosition.ConePrep;
            newGridColumnPosition = 6;
        }
        //8
        if (key1.getRawButton(7)){
            keyInputOdometryPosition = Constants.isBlue()? Constants.FieldPositions.AutoAlignPositions.blue7: Constants.FieldPositions.AutoAlignPositions.red7;
            isConeNodePosition = true;
            gridArmPosition = GridArmPosition.Hybrid;
            newGridColumnPosition = 7;   
        }
        if (key1.getRawButton(8) || key1.getRawButton(9)){
            keyInputOdometryPosition = Constants.isBlue()? Constants.FieldPositions.AutoAlignPositions.blue7: Constants.FieldPositions.AutoAlignPositions.red7;
            isConeNodePosition = false;
            gridArmPosition = GridArmPosition.CubePrep;
            newGridColumnPosition = 7;
        }
        //9
        if(key1.getRawButton(10)){
            keyInputOdometryPosition = Constants.isBlue()? Constants.FieldPositions.AutoAlignPositions.blue8: Constants.FieldPositions.AutoAlignPositions.red8;
            isConeNodePosition = true;
            gridArmPosition = GridArmPosition.Hybrid;
            newGridColumnPosition = 8;    
        }
        if (key1.getRawButton(11) || key1.getRawButton(12)){
            keyInputOdometryPosition = Constants.isBlue()? Constants.FieldPositions.AutoAlignPositions.blue8: Constants.FieldPositions.AutoAlignPositions.red8;
            isConeNodePosition = true;
            gridArmPosition = GridArmPosition.ConePrep;
            newGridColumnPosition = 8;
        }



        // gridArmPosition = isConeNodePosition?GridArmPosition.ConeReady:GridArmPosition.CubeReady;

        if (!(newGridColumnPosition == gridColumnPosition)){
            AutoAlign.gridAlignState = AutoAlign.GridAlignState.AlignedToOdometry;
            gridColumnPosition = newGridColumnPosition;
        }
        
    }

    public static void keyInputSubstationPosition(){
        if (key2.getRawButton(5)){
            // if (Constants.isBlue()){
            //     keyInputSubstationPosition = Constants.FieldPositions.AutoAlignPositions.blueLeftDoubleSubstation;
            // } else if (Constants.isRed()){
            //     keyInputSubstationPosition = Constants.FieldPositions.AutoAlignPositions.redLeftDoubleSubstation;
            // }
            AutoAlign.substationAlignState = AutoAlign.SubstationAlignState.AlignedToOdometry;
            // gridArmPosition = GridArmPosition.DoubleSubstation;
            // gridRowPosition = GridRowPosition.DoubleSubstation;
            keyInputSubstationPosition = Constants.isBlue()?Constants.FieldPositions.AutoAlignPositions.blueLeftDoubleSubstation:
                                                            Constants.FieldPositions.AutoAlignPositions.redLeftDoubleSubstation;
        }else if (key2.getRawButton(6)){
            // if (Constants.isBlue()){
            //     keyInputSubstationPosition = Constants.FieldPositions.AutoAlignPositions.blueRightDoubleSubstation;
            // } else if (Constants.isRed()){
            //     keyInputSubstationPosition = Constants.FieldPositions.AutoAlignPositions.redRightDoubleSubstation;
            // }
            AutoAlign.substationAlignState = AutoAlign.SubstationAlignState.AlignedToOdometry;
            // gridArmPosition = GridArmPosition.DoubleSubstation;
            // gridRowPosition = GridRowPosition.DoubleSubstation;
            keyInputSubstationPosition = Constants.isBlue()?Constants.FieldPositions.AutoAlignPositions.blueRightDoubleSubstation:
                                                            Constants.FieldPositions.AutoAlignPositions.redRightDoubleSubstation;
        }else if (key2.getRawButton(8)){
            AutoAlign.substationAlignState = AutoAlign.SubstationAlignState.AlignedToOdometry;
            keyInputSubstationPosition = Constants.isBlue()?Constants.FieldPositions.AutoAlignPositions.blueSingleSubstation:
                                                          Constants.FieldPositions.AutoAlignPositions.redSingleSubstation;
        }
    }
    public static void displayInformation(){
        SmartDashboard.putString("gridRowPosition" , IO.gridRowPosition.toString());
        SmartDashboard.putString("gridArmPosition: " , IO.gridArmPosition.toString());
        SmartDashboard.putString("IO.keyInputOdometryPosition", IO.keyInputOdometryPosition.toString());
        SmartDashboard.putString("IO.keyInputSubstationPosition", IO.keyInputSubstationPosition.toString());

        // SmartDashboard.putNumber("IO.keyInputSubstationPosition X", IO.keyInputSubstationPosition.getX());

    }
}
