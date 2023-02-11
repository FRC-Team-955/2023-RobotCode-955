// package frc.robot.Subsystems;

// import frc.robot.Constants;
// import frc.robot.IO;

// import com.ctre.phoenix.sensors.CANCoder;
// import com.ctre.phoenix.sensors.CANCoderConfiguration;
// import com.ctre.phoenix.sensors.SensorTimeBase;
// import com.revrobotics.CANSparkMax;
// import com.revrobotics.CANSparkMaxLowLevel.MotorType;

// import edu.wpi.first.math.MathUtil;
// import edu.wpi.first.math.controller.ArmFeedforward;
// import edu.wpi.first.math.controller.PIDController;
// import edu.wpi.first.util.datalog.DataLog;
// import edu.wpi.first.util.datalog.DoubleLogEntry;
// import edu.wpi.first.wpilibj.DataLogManager;
// import edu.wpi.first.wpilibj.Timer;

// public final class Arm {
//     static CANSparkMax armMotor;
//     static PIDController pid;
//     static CANCoder encoder;
//     static ArmFeedforward feedFoward;
//     static Timer timer;
//     static CANCoderConfiguration config;
//     static double lastVelocity = 0;
//     static DoubleLogEntry motorLog;
//     static DoubleLogEntry encoderLog;

//     public static void setup() {
//         armMotor = new CANSparkMax(Constants.Arm.kArmMotorId, MotorType.kBrushless);
//         armMotor.setSmartCurrentLimit(40);
//         pid = new PIDController(Constants.Arm.kP, 
//                                 Constants.Arm.kI, 
//                                 Constants.Arm.kD);   
//         encoder = new CANCoder(Constants.Arm.kArmEncoderId);

//         // set units of the CANCoder to radians, with velocity being radians per second
//         config = new CANCoderConfiguration();
//         config.sensorCoefficient = 2 * Math.PI / 4096.0;
//         config.unitString = "rad";
//         config.sensorTimeBase = SensorTimeBase.PerSecond;
//         encoder.configAllSettings(config);

//         feedFoward = new ArmFeedforward(Constants.Arm.kS, Constants.Arm.kG, Constants.Arm.kV);
//         timer = new Timer();

//         DataLog log = DataLogManager.getLog();
//         motorLog = new DoubleLogEntry(log, "/intake/armMotor");
//         encoderLog = new DoubleLogEntry(log, "/intake/armEncoder");
//     }

//     public static void logData() {
//         motorLog.append(armMotor.getOutputCurrent());
//         encoderLog.append(encoder.getPosition());
//     }

//     public static void moveArm(double joyPos) {
//         if (!IO.isOverrideEnabled()) { 
//             if ((encoder.getPosition() >= Constants.Arm.kArmUpperLimit && joyPos > 0)|| 
//                 (encoder.getPosition() <= Constants.Arm.kArmLowerLimit && joyPos < 0)) { // If elevator reach top AND trying to go up
//                 armMotor.stopMotor(); 
//             }
//             else {
//                 armMotor.set(joyPos);
//             }
//         }
//     }
//     public static void moveArmOverride(double joyPos) {
//         armMotor.set(joyPos);
//     }

//     public static double armSetPoint = 0;
//     public static void setArm(IO.GridArmPosition level) {
//         switch(level) {
//             case Retract:
//                 armSetPoint = Constants.Arm.kRetracted;
//                 break;
//             case ConePrep:
//                 armSetPoint = Constants.Arm.kConePrep;
//                 break;
//             case ConeReady:
//                 armSetPoint = Constants.Arm.kConeReady;
//                 break;
//             case CubePrep:
//                 armSetPoint = Constants.Arm.kCubePrep;
//                 break;
//             case CubeReady:
//                 armSetPoint = Constants.Arm.kCubeReady;
//                 break;
//         }
//     }
//     public static boolean setArm(){
//         if (!IO.isOverrideEnabled()) {
//             timer.stop(); 
//             double accelRadPerSecond = (lastVelocity - encoder.getVelocity()) / timer.get(); 
//             timer.reset();
//             timer.start();
//             double feedFowardCalc = feedFoward.calculate(armSetPoint, 
//                                                     encoder.getVelocity(), 
//                                                     accelRadPerSecond);

//             double output = MathUtil.clamp(pid.calculate(encoder.getPosition(), armSetPoint) + feedFowardCalc, -12, 12);
            
//             armMotor.setVoltage(output); 
//             lastVelocity = encoder.getVelocity();
//             return pid.atSetpoint();
//         }
//         return false;
//     }
// }


