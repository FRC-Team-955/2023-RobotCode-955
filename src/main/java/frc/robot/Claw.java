public class Claw {
    static TalonSRX clawMotorOne;
    
    public Claw(){
        clawMotorOne = new TalonSRX(Constants.Claw.motorOneNum);
    }

    //sucks in the game piece and stop
    public static void intakeGamePiece(){
        clawMotorOne.set(TalonSRXControlMode.PercentOutput, Constants.Claw.motorOutput);
}

    //spits out the game piece
    public static void outputGamePiece(){
        clawMotorOne.set(TalonSRXControlMode.PercentOutput, -Constants.Claw.motorOutput);
    }
}
