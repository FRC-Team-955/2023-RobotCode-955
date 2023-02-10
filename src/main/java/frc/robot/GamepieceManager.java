package frc.robot;

public class GamepieceManager {
    Elevator elevator;

    public boolean CollectFromIntake() {
        Intake.foldInIntake();
        elevator.setElevator(0);
        Arm.setArm(0, 0);
        Handoff.intakeGamePiece();

        //Add something that doesnt let holdGamePiece and outputGamePiece aren't called at the same time please - Ethan Kim
        return /*SENSOR RTUFF YAAAAAAAAAAAAAAYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY */ false;
    }
}
