// package frc.robot.Subsystems;

// import frc.robot.Constants;
// import frc.robot.IO;

// import com.ctre.phoenix.motorcontrol.ControlMode;
// import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
// import com.ctre.phoenix.motorcontrol.can.TalonSRX;

// import edu.wpi.first.math.MathUtil;
// import edu.wpi.first.util.datalog.DataLog;
// import edu.wpi.first.util.datalog.DoubleLogEntry;
// import edu.wpi.first.wpilibj.DataLogManager;
// import edu.wpi.first.wpilibj.I2C.Port;

// import com.revrobotics.CANSparkMax;
// import com.revrobotics.ColorSensorV3;
// import com.revrobotics.SparkMaxAbsoluteEncoder;
// import com.revrobotics.CANSparkMax.IdleMode;
// import com.revrobotics.CANSparkMaxLowLevel.MotorType;
// import com.revrobotics.SparkMaxAbsoluteEncoder.Type;

// public class IntakeV2 {
//     private static CANSparkMax retractMotor = new CANSparkMax(Constants.IntakeV2.retractMotorID, MotorType.kBrushless);
//     private static TalonSRX handOffMotor = new TalonSRX(Constants.IntakeV2.retractMotorID);

//     public static void setup() {
//         retractMotor.setIdleMode(IdleMode.kBrake);
//     }

//     public static boolean extend() {
//         if (!IO.isOverrideEnabled()) {
//             if (retractMotor.getOutputCurrent() < Constants.IntakeV2.intakeAmpThreshhold) {
//                 retractMotor.set(Constants.IntakeV2.retractMotorRun);
//             }
//             else {
//                 retractMotor.set(0);
//                 return true;
//             }
//         }
//         return false;
//     }
    

//     public static boolean retract() {
//         if (!IO.isOverrideEnabled()) {
//             if (retractMotor.getOutputCurrent() < Constants.IntakeV2.intakeAmpThreshhold) {
//                 retractMotor.set(-Constants.IntakeV2.retractMotorRun);
//             }
//             else {
//                 retractMotor.set(0);
//                 return true;
//             }
//         }
//         return false;
//     }

//     public static boolean intake() {
//         if (!IO.isOverrideEnabled()) {
//             handOffMotor.set(TalonSRXControlMode.PercentOutput, Constants.IntakeV2.handOffMotorRun);
//             if (handOffMotor.getStatorCurrent() >= Constants.IntakeV2.intakeAmpThreshhold) {
//                 handOffMotor.set(TalonSRXControlMode.PercentOutput, 0);
//                 return true;
//             }
//             else {
//                 return false;
//             }
//         }
//         return true;
//     }

//     public static boolean handOff() {
//         if (!IO.isOverrideEnabled())
//     }

// }
