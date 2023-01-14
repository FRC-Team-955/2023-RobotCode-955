package frc.robot;

public class Enums {
    public enum Actions {
        OBTAINOBJECT,
        MOVEINTAKEFORWARD,
        MOVEINTAKEBACKWARD,
        MOVEELEVATORUP,
        MOVEELEVATORDOWN,
        AUTOALIGNAPRILTAG,
        AUTOALIGNTAPE
    }

    public enum State {
        CURVE,
        ACTION,
        TRANSITION,
        STOP,
        LOADTRAJECTORY,
        PAUSE
    }
}
