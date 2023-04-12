package frc.robot;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Auto.Auto;
import frc.robot.Sensors.LimelightCameraWrapper;
import frc.robot.Subsystems.Arm;
import frc.robot.Subsystems.Claw;
import frc.robot.Subsystems.Elevator;
import frc.robot.Subsystems.IntakeV2;

public class GamepieceManager {

    // public static boolean foldIntakeAuto(int position) {
    //     if (position == 0) {
    //         return IntakeV2.handOff();
    //     } else if (position == 1) {
    //         return IntakeV2.extend();
    //     } else if (position == -1) {
    //         return IntakeV2.retract();
    //     }
    //     return false;
    // }
    
    public static void moveClawAuto(int speed) {
        if (speed == -1) {
            Claw.outputGamePiece();
        } else if (speed == 1) {
            Claw.intakeGamePiece();
        } else {
            Claw.stopishMotor();
        }
    }

    private static enum loadStates{
        Intake,
        LoadPrep,
        Load,
        Loaded,
        Finish,
    }

    private static loadStates loadState = loadStates.Intake;

    public static void loadResetOverride(boolean reset) {
        loadState = loadStates.Intake;
    }
    public static Timer clawTimer = new Timer();

    // public static void loadGamepieceCone() {
    //     Arm.setArm();
    //     Elevator.setElevator();
    //     switch(loadState) {
    //         case Intake:
    //             Arm.setArm(IO.GridArmPosition.ConeIntake);
    //             Elevator.setElevator(IO.GridRowPosition.ConeIntake);
    //             IntakeV2.slowIntake();
    //             SmartDashboard.putBoolean("intake extended", IntakeV2.extendNoPid());
    //             if(IntakeV2.extendNoPid()) {
    //                 if(IntakeV2.intake()) {
    //                     loadState = loadStates.LoadPrep;
    //                 }
    //             }
    //             break;
    //         case LoadPrep:
    //             IntakeV2.extendNoPid();
    //             IntakeV2.slowIntake();
    //             // if(runExtention()) {
    //                 loadState = loadStates.Load;
    //             // }
    //             break;
    //         case Load:
    //             Claw.intakeGamePiece();
    //             if(IntakeV2.handOffNoPid()) {
    //                 loadState = loadStates.Loaded;
    //                 clawTimer.reset();
    //                 clawTimer.start();
    //             }
    //             break;
    //         case Loaded:
    //             IntakeV2.handOffNoPid();
    //             if (!clawTimer.hasElapsed(Constants.GamepieceManager.clawExtraRunTime) ){
    //                 Claw.intakeGamePiece();
    //             }else{
    //                 Claw.stopishMotor();
    //             }
    //             loadState = loadStates.Finish;
    //             break;
    //         case Finish:
    //             IntakeV2.handOffNoPid();
    //             if (!clawTimer.hasElapsed(Constants.GamepieceManager.clawExtraRunTime) ){
    //                 Claw.intakeGamePiece();
    //             }else{
    //                 Claw.stopishMotor();
    //             }
    //             break;
    //     }
    // }
    public static void loadCone(){
        if(Arm.setpoint == Constants.Arm.cubeIntake){
        }else{
            IntakeV2.extendNoPidDrop();
        }
    }
    public static void loadGamepieceCube() {
        Claw.intakeGamePiece();
        clawTimer.start();
        clawTimer.reset();
        IntakeV2.retractNoPid();
        if (Arm.atRetractedPosition() || Arm.atCubeRetractPosition()){
            extentionArmFirst(IO.GridRowPosition.CubeIntake, IO.GridArmPosition.CubeIntake);
        }else{
            extention(IO.GridRowPosition.CubeIntake, IO.GridArmPosition.CubeIntake);
        }
    }

    //DO NOT USE UNTIL INTAKE IS MOUNTED
    //Assumes runExtension is constantly called
    public static boolean moveBack = false;
    public static void loadSequence(){
        // If runIntake is pressed (home), then run claw in fast
        if(IO.runIntakeIn()){
            Claw.intakeGamePiece();
            moveBack = false;
        }
        // if clawDropPiece is pressed (Page Down) and the Arm is in position of ConePrep, then drop down the arm into ready and output
        else if(IO.clawDropPiece() && Arm.atConeFarPrepHighPosition() && IO.gridArmPosition == IO.GridArmPosition.ConeFarPrepHigh){
          if (GamepieceManager.extention(IO.gridRowPosition, IO.GridArmPosition.ConeFarReadyHigh)){
                moveBack = true;
                Claw.outputGamePiece();
          }
        }
        // if clawDropPiece is pressed (Page Down) and the Arm is in position of ConePrepMid, then drop down the arm into ready and output
        else if(IO.clawDropPiece() && Arm.atConeFarPrepMidPosition() && IO.gridArmPosition == IO.GridArmPosition.ConeFarPrepMid){
            if (GamepieceManager.extention(IO.gridRowPosition, IO.GridArmPosition.ConeFarReadyMid)){
                moveBack = true;
                Claw.outputGamePiece();
            }
        }
        // if clawDropPiece is pressed (Page Down) and the Arm is in position of ConePrep, then drop down the arm into ready and output
        else if(IO.clawDropPiece() && Arm.atConeClosePrepHighPosition() && IO.gridArmPosition == IO.GridArmPosition.ConeClosePrepHigh){
            if (GamepieceManager.extention(IO.gridRowPosition, IO.GridArmPosition.ConeCloseReadyHigh)){
                moveBack = true;
                Claw.outputGamePiece();
            }
        }
            // if clawDropPiece is pressed (Page Down) and the Arm is in position of ConePrepMid, then drop down the arm into ready and output
        else if(IO.clawDropPiece() && Arm.atConeClosePrepMidPosition() && IO.gridArmPosition == IO.GridArmPosition.ConeClosePrepMid){
            if (GamepieceManager.extention(IO.gridRowPosition, IO.GridArmPosition.ConeCloseReadyMid)){
                moveBack = true;
                Claw.outputGamePiece();
            }
        }
        // if clawDropPiece is pressed (Page Down) and the Arm is in position of up, then drop the cone in the intake
        else if (IO.clawDropPiece() && Arm.setpoint == Constants.Arm.up){
            IntakeV2.reverseIntake();
            Claw.outputGamePiece();
            moveBack = false;
        }
        // if clawDropPiece is pressed (Page Down), then output
        else if(IO.clawDropPiece()){
            Claw.outputGamePiece();
            moveBack = false;
        }
        else if(IO.intakeSequenceCone()){
            // loadGamepieceCone();
            loadCone();
            moveBack = false;
            // clawTimer.start();
            // clawTimer.reset();
        }
        else if(IO.intakeSequenceCube()){
            loadGamepieceCube();
            clawTimer.start();
            clawTimer.reset();
            moveBack = false;
        }
        else{
            if (clawTimer.get() < Constants.GamepieceManager.clawExtraRunTime){
                Claw.intakeGamePiece();
                // Claw.stopishMotor();
                
            }else{
                Claw.stopishMotor();
            }
            IntakeV2.retractNoPid();
            IntakeV2.slowIntake();
            loadState = loadStates.Intake;
            moveBack = false;
        }
    }
    private static boolean elevatorInPosition = false;
    private static boolean armInPosition = false;
    public static void setExtention(IO.GridRowPosition gridRowPosition, IO.GridArmPosition armRowPosition){
        Elevator.setElevator(gridRowPosition);
        Arm.setArm(armRowPosition);
    }
    public static boolean runExtention(){
        elevatorInPosition = Elevator.setElevator();
        armInPosition = Arm.setArm();
        return elevatorInPosition && armInPosition;
    }
    public static boolean extention(IO.GridRowPosition gridRowPosition, IO.GridArmPosition armRowPosition){
        Arm.setArm(armRowPosition);
        Elevator.setElevator(gridRowPosition);
        return runExtention();
    }
    public static boolean extentionElevatorFirst(IO.GridRowPosition gridRowPosition, IO.GridArmPosition armRowPosition){
        Elevator.setElevator(gridRowPosition);
        elevatorInPosition = Elevator.setElevator();
        if (elevatorInPosition){
            Arm.setArm(armRowPosition);
        }
        return runExtention();
    }
    public static boolean extentionArmFirst(IO.GridRowPosition gridRowPosition, IO.GridArmPosition armRowPosition){
        Arm.setArm(armRowPosition);
        armInPosition = Arm.setArm();
        if (armInPosition){
            Elevator.setElevator(gridRowPosition);
        }
        return runExtention();
    }
    public static void autoAlign(){
        if (AutoAlign.isInCommunity()){
            autoPlace();
        }else if (AutoAlign.isInLoadingZone()){
            autoGrab();
        }
    }
    public static boolean clawDropped = false;

    public static enum PlaceState {
        Align,
        Place,
        Leave
    }
    public static PlaceState placeState = PlaceState.Align;
    
    public static void autoPlace(){
        switch(placeState){
            case Align:
                Claw.stopishMotor();
                boolean robotInPosition = AutoAlign.moveToGridPositionOdometryTwoStep();
                
                //If your left right is correct but not your forward back yet, move elevator and arm into position
                if(AutoAlign.gridAlignState == AutoAlign.GridAlignState.InPosition){
                    extention(IO.gridRowPosition, IO.gridArmPosition);
                }else{
                    switch(IO.gridNodeType){
                        //if Hybrid than Hybrid, however must do this before entering anyways(tho maybe it work now)
                        case Hybrid:
                            extention(IO.GridRowPosition.CubeRetract, IO.GridArmPosition.CubeIntake);
                            break;
                        //if your a cube position and your left right is almost correct, then move elevator and arm into position and allow 
                        case Cube:
                            if(Math.abs(IO.keyInputOdometryPosition.getY() - Drivebase.getPose().getY()) < Constants.AutoAlign.cubePreemptiveExtension){
                                extention(IO.gridRowPosition, IO.GridArmPosition.CubePrep);
                            }else{
                                extention(IO.GridRowPosition.Retract, IO.GridArmPosition.CubePrep);
                            }
                            if(Math.abs(IO.keyInputOdometryPosition.getY() - Drivebase.getPose().getY()) < Constants.AutoAlign.cubePreemptiveDrop){
                                placeState = PlaceState.Place;
                            }
                            break;
                        case ConeFar:
                            switch(IO.gridRow){
                                //Not possible
                                case Hybrid:
                                    break;
                                // if ConePrepMid pre move arm into position
                                case Mid:
                                    extention(IO.gridRowPosition, IO.GridArmPosition.ConeFarPrepMid);
                                    break;
                                case High:
                                    if(Math.abs(IO.keyInputOdometryPosition.getY() - Drivebase.getPose().getY()) < Constants.AutoAlign.conePreemptiveExtension){
                                        extention(IO.gridRowPosition, IO.GridArmPosition.ConeFarPrepHigh);
                                    }else{
                                        extention(IO.GridRowPosition.Retract, IO.GridArmPosition.ConeFarPrepHigh);
                                    }
                                    break;
                            }
                            break;
                        case ConeClose:
                            switch(IO.gridRow){
                                //Not possible
                                case Hybrid:
                                    break;
                                case Mid:
                                    extention(IO.gridRowPosition, IO.GridArmPosition.ConeClosePrepMid);
                                    break;
                                case High:
                                    if(Math.abs(IO.keyInputOdometryPosition.getY() - Drivebase.getPose().getY()) < Constants.AutoAlign.conePreemptiveExtension){
                                        extention(IO.gridRowPosition, IO.GridArmPosition.ConeClosePrepHigh);
                                    }else{
                                        extention(IO.GridRowPosition.Retract, IO.GridArmPosition.ConeClosePrepHigh);
                                    }
                                    break;
                            }
                            break;
                    }
                }
                if (robotInPosition){
                    placeState = PlaceState.Place;
                }
                break;
            case Place:
                AutoAlign.moveToGridPositionOdometryTwoStep();
                if(clawDrop()){
                    placeState = PlaceState.Leave;
                }
                break;
            case Leave:
                Claw.outputGamePiece();
                runExtention();
                if(IO.gridArmPosition == IO.GridArmPosition.ConeFarPrepHigh || IO.gridArmPosition == IO.GridArmPosition.ConeClosePrepHigh || IO.gridArmPosition == IO.GridArmPosition.ConeClosePrepMid || IO.gridArmPosition == IO.GridArmPosition.ConeFarPrepMid){
                    AutoAlign.alignOdometry(IO.keyInputOdometryPosition, -180);
                }else if(IO.gridArmPosition == IO.GridArmPosition.CubePrep){
                    AutoAlign.alignOdometrykP(IO.keyInputOdometryPosition.plus(new Translation2d(Constants.isBlue()?Constants.Auto.noHitGridOffset:-Constants.Auto.noHitGridOffset,0)), -180 , 2, 2, 0.05, true);
                }
                break;

        }
    }

    public static void autoGrab(){
        AutoAlign.moveToSubstationPosition();
        if (IO.keyInputSubstationPosition == Constants.FieldPositions.AutoAlignPositions.blueSingleSubstation || 
            IO.keyInputSubstationPosition == Constants.FieldPositions.AutoAlignPositions.redSingleSubstation){
            extention(IO.GridRowPosition.SingleSubstation, IO.GridArmPosition.SingleSubstation);
        }else{
            System.out.println("bruh");
            extention(IO.GridRowPosition.DoubleSubstation, IO.GridArmPosition.DoubleSubstation);
        }
        Claw.intakeGamePiece();
        clawTimer.reset();
        clawTimer.start();
    }
    public static enum CubeIntakeState {
        Align,
        Forward
    }
    public static CubeIntakeState cubeIntakeState = CubeIntakeState.Align;

    public static void autoCubeIntake(){
        if(LimelightCameraWrapper.hasTargets()){
            IO.rumbleStopJoy0();
            if(Arm.atCubeIntakePosition()){
                AutoAlign.forwardToPiece(false);
            }
        }else{
            IO.rumbleJoy0();
            Drivebase.drive();
        }
        loadGamepieceCube();
    }

    public static boolean clawDrop(){
        if (IO.clawDropPiece()){
            //If its cone node then move down, else don't, then output the game piece
            if(IO.gridArmPosition == IO.GridArmPosition.ConeFarPrepHigh){
                if(extention(IO.gridRowPosition, IO.GridArmPosition.ConeFarReadyHigh)){
                    Claw.outputGamePiece();
                    return true;
                }
            }
            else if (IO.gridArmPosition == IO.GridArmPosition.ConeFarPrepMid){
                if(extention(IO.gridRowPosition, IO.GridArmPosition.ConeFarReadyMid)){
                    Claw.outputGamePiece();
                    return true;
                }
                // if(extention(IO.gridRowPosition, IO.GridArmPosition.Up)){
                //     Claw.outputGamePiece();
                //     return true;
                // }
            }
            else if (IO.gridArmPosition == IO.GridArmPosition.ConeClosePrepHigh){
                if(extention(IO.gridRowPosition, IO.GridArmPosition.ConeCloseReadyHigh)){
                    Claw.outputGamePiece();
                    return true;
                }
            }
            else if (IO.gridArmPosition == IO.GridArmPosition.ConeClosePrepMid){
                if(extention(IO.gridRowPosition, IO.GridArmPosition.ConeCloseReadyMid)){
                    Claw.outputGamePiece();
                    return true;
                }
            }
            
            else if(extention(IO.gridRowPosition, IO.gridArmPosition)){
                Claw.outputGamePiece();
                return true;
            }
            else{
                extention(IO.gridRowPosition, IO.gridArmPosition);
                // Claw.stopishMotor();
                Claw.outputGamePiece();

            }
        }else{
            extention(IO.gridRowPosition, IO.gridArmPosition);
            Claw.stopishMotor();
        }
        return false;
    }
    public static boolean wasInCommunityOrLoadingZone = AutoAlign.isInCommunity() || AutoAlign.isInLoadingZone();
    public static void manageExtension(){
        // if(wasInCommunityOrLoadingZone && (!AutoAlign.isInCommunity() || !AutoAlign.isInLoadingZone())){
        //     extention(IO.GridRowPosition.Retract, IO.GridArmPosition.Up);
        // }
        if(IO.elevatorManualUp()){
            extention(IO.gridRowPosition, IO.gridArmPosition);
            // if(Constants.isBlue()? Drivebase.getPose().getX() < Constants.FieldPositions.centerLine : Drivebase.getPose().getX() > Constants.FieldPositions.centerLine){
            // }
            // else{
            //     extention(IO.GridRowPosition.DoubleSubstation, IO.GridArmPosition.DoubleSubstation);
            // }

        }else if (IO.elevatorManualDown()){
            extention(IO.GridRowPosition.UpRetract, IO.GridArmPosition.Up);
        }else if (IO.manualCubeRetract()){
            extention(IO.GridRowPosition.CubeRetract, IO.GridArmPosition.CubeRetract);
        }else if(IO.doubleSubstationExtention()){
            extention(IO.GridRowPosition.DoubleSubstation, IO.GridArmPosition.DoubleSubstation);
        }
        runExtention();
        // wasInCommunityOrLoadingZone = AutoAlign.isInCommunity() || AutoAlign.isInLoadingZone();
    }
    public static void displayInformation(){
        SmartDashboard.putString("PlaceState", placeState.toString());
        SmartDashboard.putString("loadState", loadState.toString());
        // SmartDashboard.putNumber("clawTimer", clawTimer.get());
    }
}