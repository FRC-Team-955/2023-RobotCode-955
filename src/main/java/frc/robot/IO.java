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
    
    private static Joystick key0 = new Joystick(2);
    private static Joystick key1 = new Joystick(3);
    private static Joystick key2 = new Joystick(4);

    // public static boolean getTestingButton() {
    //     return joy0.getRawButton(7);
    // }
    // public static boolean getTestingButtonTwo() {
    //     return joy0.getRawButton(8);
    // }

    // public static double getTestingTrigger() {
    //     return joy0.getRawAxis(3);
    // }

    public static double elevatorFineControl(){
        return -joy1.getRawAxis(Constants.IO.Joy1.elevatorOverrideAxis);
    }

    public static double armFineControl(){
        return joy1.getRawAxis(Constants.IO.Joy1.armOverrideAxis);
    }
    public static boolean resetAngle(){
        return joy0.getRawButtonPressed(Constants.IO.Joy0.resetAngleButton);
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
    public static boolean manualCubeRetract(){
        return key2.getRawButton(Constants.IO.Key2.manualCubeRetractButton);
    }
    public static boolean clawDropPiece(){
        return key2.getRawButton(Constants.IO.Key2.clawDropPieceButton) || joy0.getRawButton(1);
        //joy1.getRawAxis(Constants.IO.Joy1.clawDropPieceAxis)>0.2 ||
        // return joy1.getRawAxis(Constants.IO.Joy1.clawDropPieceAxis)>0.2;
    }
    
    public static double armOverride() {
        return joy1.getRawAxis(Constants.IO.Joy1.armOverrideAxis);
    }
      
    public static double elevatorOverride(){
        return joy1.getRawAxis(Constants.IO.Joy1.elevatorOverrideAxis);
    }

    public static boolean intakeSequenceCone(){
        return key2.getRawButton(Constants.IO.Key2.intakeSequenceConeButton);
        // return joy1.getRawAxis(Constants.IO.Joy1.deployRunIntakeAxis) < 0.2 || key2.getRawButtonPressed(4);
    }
    public static boolean intakeSequenceCube(){
        return key2.getRawButton(Constants.IO.Key2.intakeSequenceCubeButton);
    }
    public static boolean doubleSubstationExtention(){
        return key2.getRawButton(Constants.IO.Key2.doubleSubstationExtension);
    }
    public static boolean runIntakeIn(){
        return key2.getRawButton(Constants.IO.Key2.runIntakeInButton);
    }
    public static class Drivebase{
        public static boolean isAutoAlignActive() {
            return joy0.getRawAxis(Constants.IO.Joy0.autoAlignAxis) > 0.2;
        }

        public static boolean isAutoBalanceActive(){
            return joy0.getRawButtonPressed(Constants.IO.Joy0.autoBalanceButton);
        }

        public static boolean isPowerSaving(){
            return key2.getRawButton(Constants.IO.Key2.isPowerSavingButton);
        }
  
        public static double getSwerveRotation(){
            //What is axis number reffering too?
            double rotAxis = joy0.getRawAxis(Constants.IO.Joy0.rotAxis);
            double deadband = 0.15;

            // rotAxis = isThrustActive() ? rotAxis : rotAxis*Constants.Drivebase.speed;

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
        public static Translation2d getSwerveTranslation(){

            double forwardRawAxis = joy0.getRawAxis(Constants.IO.Joy0.forwardRawAxis);
            double strafeRawAxis = joy0.getRawAxis(Constants.IO.Joy0.strafeRawAxis);

            Translation2d tAxes = new Translation2d(isThrustActive() ? forwardRawAxis*Constants.Drivebase.slowSpeed : forwardRawAxis, isThrustActive() ? strafeRawAxis*Constants.Drivebase.slowSpeed : strafeRawAxis);


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

        public static boolean isAutoHeadingActive(){
            return joy0.getRawButton(Constants.IO.Joy0.autoHeadingButton);
        }
        public static boolean isRobotRelativeActive(){
            return joy0.getRawButton(Constants.IO.Joy0.robotRelativeButton);
        }
        public static boolean isThrustActive(){
            return joy0.getRawAxis(Constants.IO.Joy0.thrustAxis) > 0.2;
        }
    }
    public static void rumble0(double strength) {
        joy0.setRumble(RumbleType.kBothRumble, strength);
    }
    public static void rumble1(double strength) {
        joy1.setRumble(RumbleType.kBothRumble, strength);
    }

    public static Translation2d keyInputOdometryPosition = Constants.isBlue()? Constants.FieldPositions.AutoAlignPositions.blueGrid0: Constants.FieldPositions.AutoAlignPositions.redGrid0;
    public static Translation2d keyInputSubstationPosition = Constants.isBlue()? Constants.FieldPositions.AutoAlignPositions.blueLeftDoubleSubstation: Constants.FieldPositions.AutoAlignPositions.redLeftDoubleSubstation;

    public static enum GridArmPosition{
        Retract,
        ConePrepHigh,
        ConeReadyHigh,
        ConePrepMid,
        ConeReadyMid,
        CubePrep,
        CubeReady,
        SingleSubstation,
        DoubleSubstation,
        Hybrid,
        CubeIntake,
        CubeRetract,
        ConeIntake,
        Up, 
        NewHybrid
    }
    public static GridArmPosition gridArmPosition = GridArmPosition.Retract;
    
    public static enum GridRowPosition{
        UpRetract,
        Retract,
        Low,
        MidCone,
        High,
        SingleSubstation,
        DoubleSubstation,
        CubeIntake, 
        CubeRetract,
        ConeIntake
    }
    public static enum GridNodeType{
        Cone,
        Cube,
        Hybrid
    }
    public static GridNodeType gridNodeType = GridNodeType.Cone;

    public static enum GridRow{
        Hybrid,
        Mid,
        High
    }
    public static GridRow gridRow = GridRow.High;
    
    private static int gridColumnPosition = 0;
    private static int newGridColumnPosition = 0;
    public static GridRowPosition gridRowPosition = GridRowPosition.Retract;

    public static void keyInputRowPosition(){
        if(key0.getRawButton(1) || key0.getRawButton(4)|| key0.getRawButton(7) || key0.getRawButton(10)|| key0.getRawButton(13)
        || key1.getRawButton(1) || key1.getRawButton(4) || key1.getRawButton(7) || key1.getRawButton(10)){
                gridRow = GridRow.Hybrid;
        }
        else if( key0.getRawButton(2) || key0.getRawButton(5) || key0.getRawButton(8) || key0.getRawButton(11) || key0.getRawButton(14)
                || key1.getRawButton(2) || key1.getRawButton(5) ||key1.getRawButton(8) || key1.getRawButton(11)){
                gridRow = GridRow.Mid;
        }
        else if( key0.getRawButton(3) || key0.getRawButton(6) || key0.getRawButton(9) || key0.getRawButton(12) || key0.getRawButton(15)
                || key1.getRawButton(3) || key1.getRawButton(6) ||key1.getRawButton(9) || key1.getRawButton(12)){
                gridRow = GridRow.High;
        }
    }

    public static void keyInputOdometryMapping() {
        //1
        if (key0.getRawButton(1)){
            keyInputOdometryPosition = Constants.isBlue()? Constants.FieldPositions.AutoAlignPositions.blueGrid0: Constants.FieldPositions.AutoAlignPositions.redGrid0;
            gridNodeType = GridNodeType.Hybrid;
            newGridColumnPosition = 0;
        }
        if (key0.getRawButton(2) || key0.getRawButton(3)){
            keyInputOdometryPosition = Constants.isBlue()? Constants.FieldPositions.AutoAlignPositions.blueGrid0: Constants.FieldPositions.AutoAlignPositions.redGrid0;
            gridNodeType = GridNodeType.Cone;
            newGridColumnPosition = 0;
        }
        //2
        if(key0.getRawButton(4)){
            keyInputOdometryPosition = Constants.isBlue()? Constants.FieldPositions.AutoAlignPositions.blueGrid1: Constants.FieldPositions.AutoAlignPositions.redGrid1;
            gridNodeType = GridNodeType.Hybrid;
            newGridColumnPosition = 1;     
        }
        if (key0.getRawButton(5) || key0.getRawButton(6)){
            keyInputOdometryPosition = Constants.isBlue()? Constants.FieldPositions.AutoAlignPositions.blueGrid1: Constants.FieldPositions.AutoAlignPositions.redGrid1;
            gridNodeType = GridNodeType.Cube;
            newGridColumnPosition = 1;
        }
        //3
        if(key0.getRawButton(7)){ 
            keyInputOdometryPosition = Constants.isBlue()? Constants.FieldPositions.AutoAlignPositions.blueGrid2: Constants.FieldPositions.AutoAlignPositions.redGrid2;
            gridNodeType = GridNodeType.Hybrid;
            newGridColumnPosition = 2;   
        } 
        if (key0.getRawButton(8) || key0.getRawButton(9)){
            keyInputOdometryPosition = Constants.isBlue()? Constants.FieldPositions.AutoAlignPositions.blueGrid2: Constants.FieldPositions.AutoAlignPositions.redGrid2;
            gridNodeType = GridNodeType.Cone;
            newGridColumnPosition = 2;
        }
        //4
        if(key0.getRawButton(10)){ 
            keyInputOdometryPosition = Constants.isBlue()? Constants.FieldPositions.AutoAlignPositions.blueGrid3: Constants.FieldPositions.AutoAlignPositions.redGrid3;
            gridNodeType = GridNodeType.Hybrid;
            newGridColumnPosition = 3;    
        } 
        if (key0.getRawButton(11) || key0.getRawButton(12)){
            keyInputOdometryPosition = Constants.isBlue()? Constants.FieldPositions.AutoAlignPositions.blueGrid3: Constants.FieldPositions.AutoAlignPositions.redGrid3;
            gridNodeType = GridNodeType.Cone;
            newGridColumnPosition = 3;
        }
        //5
        if(key0.getRawButton(13)){ 
            keyInputOdometryPosition = Constants.isBlue()? Constants.FieldPositions.AutoAlignPositions.blueGrid4: Constants.FieldPositions.AutoAlignPositions.redGrid4;
            gridNodeType = GridNodeType.Hybrid;
            newGridColumnPosition = 4;    
        } 
        if (key0.getRawButton(14) || key0.getRawButton(15)){
            keyInputOdometryPosition = Constants.isBlue()? Constants.FieldPositions.AutoAlignPositions.blueGrid4: Constants.FieldPositions.AutoAlignPositions.redGrid4;
            gridNodeType = GridNodeType.Cube;
            newGridColumnPosition = 4;
        }
        //6
        if(key1.getRawButton(1)){
            keyInputOdometryPosition = Constants.isBlue()? Constants.FieldPositions.AutoAlignPositions.blueGrid5: Constants.FieldPositions.AutoAlignPositions.redGrid5;
            gridNodeType = GridNodeType.Hybrid;
            newGridColumnPosition = 5;   
        }
        if (key1.getRawButton(2) || key1.getRawButton(3)){
            keyInputOdometryPosition = Constants.isBlue()? Constants.FieldPositions.AutoAlignPositions.blueGrid5: Constants.FieldPositions.AutoAlignPositions.redGrid5;
            gridNodeType = GridNodeType.Cone;
            newGridColumnPosition = 5;
        }
        //7
        if(key1.getRawButton(4)){
            keyInputOdometryPosition = Constants.isBlue()? Constants.FieldPositions.AutoAlignPositions.blueGrid6: Constants.FieldPositions.AutoAlignPositions.redGrid6;
            gridNodeType = GridNodeType.Hybrid;
            newGridColumnPosition = 6;
        }
        if (key1.getRawButton(5) || key1.getRawButton(6)){
            keyInputOdometryPosition = Constants.isBlue()? Constants.FieldPositions.AutoAlignPositions.blueGrid6: Constants.FieldPositions.AutoAlignPositions.redGrid6;
            gridNodeType = GridNodeType.Cone;
            newGridColumnPosition = 6;
        }
        //8
        if (key1.getRawButton(7)){
            keyInputOdometryPosition = Constants.isBlue()? Constants.FieldPositions.AutoAlignPositions.blueGrid7: Constants.FieldPositions.AutoAlignPositions.redGrid7;   
            gridNodeType = GridNodeType.Hybrid;
            newGridColumnPosition = 7;   
        }
        if (key1.getRawButton(8) || key1.getRawButton(9)){
            keyInputOdometryPosition = Constants.isBlue()? Constants.FieldPositions.AutoAlignPositions.blueGrid7: Constants.FieldPositions.AutoAlignPositions.redGrid7; 
            gridNodeType = GridNodeType.Cube;
            newGridColumnPosition = 7;
        }
        //9
        if(key1.getRawButton(10)){
            keyInputOdometryPosition = Constants.isBlue()? Constants.FieldPositions.AutoAlignPositions.blueGrid8: Constants.FieldPositions.AutoAlignPositions.redGrid8; 
            gridNodeType = GridNodeType.Hybrid;
            newGridColumnPosition = 8;    
        }
        if (key1.getRawButton(11) || key1.getRawButton(12)){
            keyInputOdometryPosition = Constants.isBlue()? Constants.FieldPositions.AutoAlignPositions.blueGrid8: Constants.FieldPositions.AutoAlignPositions.redGrid8;
            gridNodeType = GridNodeType.Cone;
            newGridColumnPosition = 8;
        }
        // gridArmPosition = isConeNodePosition?GridArmPosition.ConeReady:GridArmPosition.CubeReady;
        if (!(newGridColumnPosition == gridColumnPosition)){
            AutoAlign.gridAlignState = AutoAlign.GridAlignState.AlignedToOdometry;
            gridColumnPosition = newGridColumnPosition;
        }
        
    }
    public static void keyGridPosition(){
        switch(gridNodeType){
            case Hybrid:
                gridArmPosition = GridArmPosition.NewHybrid;
                gridRowPosition = GridRowPosition.Retract;
                break;
            case Cube:
                switch(gridRow){
                    //Not possible
                    case Hybrid:
                        gridArmPosition = GridArmPosition.NewHybrid;
                        gridRowPosition = GridRowPosition.Retract;
                        break;
                    case Mid:
                        gridArmPosition = GridArmPosition.CubePrep;
                        gridRowPosition = GridRowPosition.Retract;
                        break;
                    case High:
                        gridArmPosition = GridArmPosition.CubePrep;
                        gridRowPosition = GridRowPosition.High;
                        break;
                }
                break;
            case Cone:
                switch(gridRow){
                    //Not possible
                    case Hybrid:
                        gridArmPosition = GridArmPosition.NewHybrid;
                        gridRowPosition = GridRowPosition.Retract;
                        break;
                    case Mid:
                        gridArmPosition = GridArmPosition.ConePrepMid;
                        gridRowPosition = GridRowPosition.MidCone;
                        break;
                    case High:
                        gridArmPosition = GridArmPosition.ConePrepHigh;
                        gridRowPosition = GridRowPosition.High;
                        break;
                }
                break;
        }
    }

    public static void keyInputSubstationPosition(){
        if (key2.getRawButton(5)){
            AutoAlign.substationAlignState = AutoAlign.SubstationAlignState.AlignedToOdometry;
            keyInputSubstationPosition = Constants.isBlue()?Constants.FieldPositions.AutoAlignPositions.blueLeftDoubleSubstation:
                                                            Constants.FieldPositions.AutoAlignPositions.redLeftDoubleSubstation;
        }else if (key2.getRawButton(6)){
            AutoAlign.substationAlignState = AutoAlign.SubstationAlignState.AlignedToOdometry;
            keyInputSubstationPosition = Constants.isBlue()?Constants.FieldPositions.AutoAlignPositions.blueRightDoubleSubstation:
                                                            Constants.FieldPositions.AutoAlignPositions.redRightDoubleSubstation;
        }else if (key2.getRawButton(8)){
            AutoAlign.substationAlignState = AutoAlign.SubstationAlignState.AlignedToOdometry;
            keyInputSubstationPosition = Constants.isBlue()?Constants.FieldPositions.AutoAlignPositions.blueSingleSubstation:
                                                          Constants.FieldPositions.AutoAlignPositions.redSingleSubstation;
        }
    }
    public static void displayInformation() {
        SmartDashboard.putString("RowPosition" , IO.gridRowPosition.toString());
        SmartDashboard.putString("ArmPosition" , IO.gridArmPosition.toString());
    }
    public static void notifyError(String error) {
        SmartDashboard.putString("Error", error);
    }
}
