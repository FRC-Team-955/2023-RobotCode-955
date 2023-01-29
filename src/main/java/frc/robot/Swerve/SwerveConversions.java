package frc.robot.Swerve;

public class SwerveConversions {
    //   /**
    //  * @param counts Falcon Counts
    //  * @param gearRatio Gear Ratio between Falcon and Mechanism
    //  * @return Degrees of Rotation of Mechanism
    //  */
    // // public static double neoToDegrees(double counts, double gearRatio) {
    // //     return counts * (360.0 / (gearRatio * 2048.0));
    // // }


    // /**
    //  * @param degrees Degrees of rotation of Mechanism
    //  * @param gearRatio Gear Ratio between Falcon and Mechanism
    //  * @return Falcon Counts
    //  */
    // public static double degreesToNeo(double degrees, double gearRatio) {
    //     // double ticks =  degrees / (360.0 / (gearRatio * 42));
    //     // return ticks;
    //     return degrees / (360/12.8);
    // }

    /**
     * @param velocityCounts Falcon Velocity Counts
     * @param gearRatio Gear Ratio between Falcon and Mechanism (set to 1 for Falcon RPM)
     * @return RPM of Mechanism
     */
    public static double neoToRPM(double velocityCounts, double gearRatio) {
        double motorRPM = velocityCounts * (600.0 / 2048.0);        
        double mechRPM = motorRPM / gearRatio;
        return mechRPM;
    }

    /**
     * @param RPM RPM of mechanism
     * @param gearRatio Gear Ratio between Falcon and Mechanism (set to 1 for Falcon RPM)
     * @return RPM of Mechanism
     */
    public static double RPMToNeo(double RPM, double gearRatio) {
        double motorRPM = RPM * gearRatio;
        double sensorCounts = motorRPM * (2048.0 / 600.0);
        return sensorCounts;
    }


    // /**
    //  * @param velocitycounts Falcon Velocity Counts
    //  * @param circumference Circumference of Wheel
    //  * @param gearRatio Gear Ratio between Falcon and Mechanism (set to 1 for Falcon RPM)
    //  * @return Falcon Velocity Counts
    //  */
    // public static double neoToMPS(double velocitycounts, double gearRatio){
    //     double wheelRPM = neoToRPM(velocitycounts, gearRatio);
    //     double wheelMPS = (wheelRPM * SwerveSettings.SwerveConstants.wheelCircumference) / 60;
    //     return wheelMPS;
    // }



    // /**
    //  * @param velocity Velocity MPS
    //  * @return Falcon Velocity Counts
    //  */
    // public static double MPSToNeo(double velocity, double gearRatio){
    //     double wheelRPM = ((velocity * 60) / SwerveSettings.SwerveConstants.wheelCircumference);
    //     double wheelVelocity = RPMToNeo(wheelRPM, gearRatio);
    //     return wheelVelocity;
    // }
}
