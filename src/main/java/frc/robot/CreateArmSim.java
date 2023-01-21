package frc.robot;

import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.simulation.SingleJointedArmSim;
import edu.wpi.first.wpilibj.smartdashboard.Mechanism2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismLigament2d;
import edu.wpi.first.wpilibj.smartdashboard.MechanismRoot2d;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj.util.Color8Bit;

public class CreateArmSim {
    private static final DCMotor m_armGearbox = DCMotor.getVex775Pro(2);
    private static final double m_armReduction = 200;
    private static final double m_armMass = 8.0; // Kilograms
    private static final double m_armLength = Units.inchesToMeters(30);
    private static final double kArmEncoderDistPerPulse = 2.0 * Math.PI / 4096;
    public static final SingleJointedArmSim m_armSim =
    new SingleJointedArmSim(
        m_armGearbox,
        m_armReduction,
        SingleJointedArmSim.estimateMOI(m_armLength, m_armMass),
        m_armLength,
        Units.degreesToRadians(-75),
        Units.degreesToRadians(75),
        m_armMass,
        true,
        VecBuilder.fill(kArmEncoderDistPerPulse) // Add noise with a std-dev of 1 tick
        );
    public static final Mechanism2d m_mech2d = new Mechanism2d(60, 60);
    private static final MechanismRoot2d m_armPivot = m_mech2d.getRoot("ArmPivot", 30, 30);
    private static final MechanismLigament2d m_armTower =
        m_armPivot.append(new MechanismLigament2d("ArmTower", 30, -90));
    private static final MechanismLigament2d m_arm =
        m_armPivot.append(
            new MechanismLigament2d(
                "Arm",
                30,
                Units.radiansToDegrees(m_armSim.getAngleRads()),
                6,
                new Color8Bit(Color.kYellow)));

    public static void setupDisplay() {
        m_armTower.setColor(new Color8Bit(Color.kBlue));
    }

    public static void setArm() {
        m_arm.setAngle(Units.radiansToDegrees(m_armSim.getAngleRads()));
    }
}
