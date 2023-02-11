package frc.robot.Auto.Control;

import frc.robot.Auto.AutoProfile;

public class AutoProfileData {
    public String name;
    public String description;

    public AutoProfileData(AutoProfile profile) {
        name = profile.name;
        description = profile.description;
    }
}
