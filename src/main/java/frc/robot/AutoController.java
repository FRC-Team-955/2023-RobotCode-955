package frc.robot;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;
import java.io.ObjectInputStream;

public class AutoController {
    public AutoProfile Profile;

    public AutoState AutoState;

    public void Drive() {
        switch (AutoState) {

            case Load:
                //Load the trajectory
                break;

            case Curve:
                //Follow the current curve
                break;

            case Action:
                //Do an action (Ex: Pick up a cube)
                break;            

            case Transition:
                //Drive in a straight line to the first point of the next curve
                break;

            case Stop:
                //Do nothing or combat errors
                break;
        }
    }

    public enum AutoState {
        Load,
        Curve,
        Action,
        Transition,
        Stop
    }

    public void LoadProfile(String ProfileName) {
        try {
            Profile = (AutoProfile) new ObjectInputStream(new ByteArrayInputStream(Files.readAllBytes(new File("home/lvuser/AutoProfiles/" + ProfileName).toPath()))).readObject();
        } catch (Exception E) {
            System.out.println("Error loading auto profile");
        }
    }
}