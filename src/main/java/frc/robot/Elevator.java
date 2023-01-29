package frc.robot;

import com.revrobotics.CANSparkMax;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.ElevatorFeedforward;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Elevator {
    CANSparkMax motor;
    PIDController pid;
    ElevatorFeedforward feedforward;
    Encoder encoder;
    Timer timer;
    double previousRate=0;

    public Elevator(CANSparkMax motor, Encoder encoder) {
        pid = new PIDController(Constants.Elevator.kPElevator,
                                Constants.Elevator.kIElevator,
                                Constants.Elevator.kDElevator);
        feedforward = new ElevatorFeedforward(Constants.Elevator.kSElevator,
                                            Constants.Elevator.kGElevator,
                                            Constants.Elevator.kVElevator,
                                            Constants.Elevator.kAElevator);
        pid.setTolerance(Constants.Elevator.kElevatorTolerance);
        this.motor = motor;
        this.encoder = encoder;
        timer = new Timer();
        timer.start();
    }

    public void moveElevator(double joyPos) {
        if((encoder.get() <= Constants.Elevator.kElevatorUpperLimit || joyPos < 0)
            && (encoder.get() >= Constants.Elevator.kElevatorLowerLimit || joyPos > 0)) { // if elevator hit the top or bottom
            motor.set(joyPos);
        } else {
            motor.set(0);
        }
    }

    public boolean setElevator(int level) { // level = desired elevator level
            double elevatorSetpoint = Constants.Elevator.kRetracted;
            switch(level) {
                case 0:
                    elevatorSetpoint = Constants.Elevator.kRetracted;
                    break;
                case 1:
                    elevatorSetpoint = Constants.Elevator.kBottomLevel;
                    break;
                case 2:
                    elevatorSetpoint = Constants.Elevator.kMediumLevel;
                    break;
                case 3:
                    elevatorSetpoint = Constants.Elevator.kTopLevel;
                    break;
            }

            double pidAmount = pid.calculate(encoder.get(), elevatorSetpoint);
            double amount = pidAmount + feedforward.calculate(encoder.getRate());
            SmartDashboard.putNumber("pid output", pidAmount);
            SmartDashboard.putNumber("pid modified", amount);
            motor.set(MathUtil.clamp(amount, -1, 1));

            return pid.atSetpoint();
        }
}