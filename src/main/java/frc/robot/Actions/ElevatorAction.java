package frc.robot.Actions;

public class ElevatorAction implements AutoAction{
    public ElevatorAction() {}

    // Options[0]: 0 = retracted, 1 = lowest, 2 = mid, 3 = high
    public boolean Act(int[] Options) {
        gamepieceController.setLocation(Options[0]);
        return gamepieceController.elevatorAtSetpoint();
    }
}
