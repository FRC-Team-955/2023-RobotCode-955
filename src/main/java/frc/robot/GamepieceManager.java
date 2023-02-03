package frc.robot;

public class GamepieceManager {

    public static void runEthanWheels(int speed) {
        if (speed == -1) {
            Intake.reverseEthanWheels();
        } else if (speed == 1) {
            Intake.runEthanWheels();
        } else {
            Intake.stopEthanWheels();
        }
    }

    public static void foldIntake(int position) {
        if (position == 0) {
            Intake.foldOutIntake();
        } else if (position == 1) {
            Intake.foldInIntake();
        }
    }
    
    public static void moveClaw(int speed) {
        if (speed == -1) {
            Claw.outputGamePiece();
        } else if (speed == 1) {
            Claw.intakeGamePiece();
        } else {
            Claw.stopMotor();
        }
    }
    
    // public static void loadClaw() {
    //     if (Intake.senseObj() || IO.loadClawButton()) {
    //         moveClaw(1);
    //         foldIntake(1);
    //     } else {
    //         moveClaw(0);
    //     }
    // }

    private static boolean runClaw = false;
    private static long startTime = System.currentTimeMillis();

    public static void loadClaw(){
        if(IO.intakeDeployRun()){
            Intake.foldOutIntake();
            Intake.runEthanWheels();
            startTime = System.currentTimeMillis();
            runClaw = true;
        }
        else{
            Intake.foldInIntake();
            if (runClaw){
                Claw.intakeGamePiece();
            }
            if (System.currentTimeMillis() - (startTime + Constants.Claw.runTime) > 0){
                runClaw = false;
            }
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
        Elevator.setElevator(gridRowPosition);
        elevatorInPosition = Elevator.setElevator();

        Arm.setArm(armRowPosition);
        armInPosition = Arm.setArm();
        
        return elevatorInPosition && armInPosition;
    }

    public static void clawDrop(){
        if (IO.clawDropPiece()){
            Claw.outputGamePiece();
        }
    }
    private static boolean robotInPosition = false;
    private static boolean extentionInPosition = false;
    public static void autoPlace(){
        robotInPosition = AutoAlign.moveToGridPosition();
        extentionInPosition = AutoAlign.isInCommunity() && extention(IO.gridRowPosition, IO.gridArmPosition);
        if (robotInPosition && extentionInPosition){
            clawDrop();
        }
    }
    public static void manageExtention(){
        if(!AutoAlign.isInCommunity() || !AutoAlign.isInLoadingZong()){
            extention(IO.GridRowPosition.Retract, IO.GridArmPosition.Retract);
        }
        else if(IO.elevatorManualUp()){
            extention(IO.gridRowPosition, IO.gridArmPosition);
        }
        else if (IO.elevatorManualDown()){
            extention(IO.GridRowPosition.Retract, IO.GridArmPosition.Retract);
        }
    }
}