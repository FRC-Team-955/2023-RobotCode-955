package frc.robot.Actions;

public class ElevatorAction implements AutoAction{
    int location;
    public ElevatorAction(int location) {
        this.location = location;
    }

    // 0 = retracted, 1 = lowest, 2 = mid, 3 = high
    public boolean Act() {
        return gamepieceManager.setLocation(location);
    }
}
