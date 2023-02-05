// package frc.robot.Subsystems;

// import frc.robot.Constants;

// import com.ctre.phoenix.motorcontrol.ControlMode;
// import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
// import com.ctre.phoenix.motorcontrol.can.TalonSRX;

// import edu.wpi.first.util.datalog.DataLog;
// import edu.wpi.first.util.datalog.DoubleLogEntry;
// import edu.wpi.first.wpilibj.DataLogManager;

// public class Claw {
//     static TalonSRX clawMotorOne;
//     static DoubleLogEntry motorLog;

//     public static void setup(){
//         //claw only has one motor
//         clawMotorOne = new TalonSRX(Constants.Claw.clawMotorOneNum);
//         DataLog log = DataLogManager.getLog();
//         motorLog = new DoubleLogEntry(log, "/claw/motor");
//     }

//     //sucks in the game piece and stops
//     public static void intakeGamePiece(){
//         clawMotorOne.set(TalonSRXControlMode.PercentOutput, Constants.Intake.motorOutput);
//     }

//     //spits out the game piece
//     public static void outputGamePiece(){
//         clawMotorOne.set(TalonSRXControlMode.PercentOutput, -Constants.Intake.motorOutput);
//     }

//     public static void stopMotor() {
//         clawMotorOne.set(ControlMode.PercentOutput, 0);
//     }
// }