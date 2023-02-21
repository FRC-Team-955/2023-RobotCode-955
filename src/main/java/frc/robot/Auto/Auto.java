package frc.robot.Auto;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
//import frc.robot.GamePieceController;
import frc.robot.Auto.Actions.AutoAction;
import frc.robot.Auto.Actions.AutoAction.EarlyEndMode;
import frc.robot.Auto.Control.Message;
import frc.robot.Auto.Control.Success;
import frc.robot.Auto.Control.Message.MessageType;
import frc.robot.Swerve.SwerveDrive;


public class Auto implements Runnable {
    public AutoProfile profile;

    //See EarlyEndMode and LateEndMode in AutoAction to understand why there are 3 Auto Action arrays

    ArrayList<AutoAction> queuedActions = new ArrayList<AutoAction>(); //Actions which are awaiting their start time

    ArrayList<AutoAction> currentActions = new ArrayList<AutoAction>(); //Actions that are running and have not passed their end time

    ArrayList<AutoAction> lateActions = new ArrayList<AutoAction>(); //Actions that have passed their end time but are still running

    public static Timer autoTimer;

    boolean started;

    ServerSocket autoControlServer;
    Socket autoController;
    BufferedReader controlIn;
    PrintWriter controlOut;
    boolean autoControlConnected;
    Thread autoControlThread;

    public Auto() {

    }

    public Auto(int controlPort) { //Use port 5810 maybe?
        try {
            autoControlServer = new ServerSocket(controlPort);
            autoControlServer.setSoTimeout(2);
        }
        catch (IOException e) {
            System.out.println("Failed to start auto control connection: ");
            System.out.print(e);
        }

        autoControlThread = new Thread(this);
        autoControlThread.start();
    }

    public void run() {
        while (!started) {
            try {
                String input;
                if ((input = controlIn.readLine()) != null) {
                    //AutoProfile directory: /home/lvuser/autoProfiles/
                    Message message = new ObjectMapper().readValue(input, Message.class);
                    switch (message.messageType) {
                        case Set:
                            try {
                                profile = new ObjectMapper().readValue(new File("/home/lvuser/autoProfiles/" + message.message + ".auto"), AutoProfile.class);
                                controlOut.println(new ObjectMapper().writeValueAsString(Success.Message(true, MessageType.Set)));
                            }
                            catch (IOException e) {
                                controlOut.println(new ObjectMapper().writeValueAsString(Success.Message(false, MessageType.Set)));
                            }
                            break;

                        case Upload:
                            try {
                                AutoProfile newProfile = new ObjectMapper().readValue(message.message, AutoProfile.class);
                                File upload = new File("/home/lvuser/autoProfiles/" + newProfile.name + ".auto");
                                upload.mkdirs();
                                upload.createNewFile();
                                new ObjectMapper().writeValue(upload, newProfile);
                                controlOut.println(new ObjectMapper().writeValueAsString(Success.Message(true, MessageType.Upload)));
                            }
                            catch (IOException e) {
                                controlOut.println(new ObjectMapper().writeValueAsString(Success.Message(false, MessageType.Upload)));
                            }
                            break;

                        case Update: 
                            try {
                                AutoProfile newProfile = new ObjectMapper().readValue(message.message, AutoProfile.class);
                                File upload = new File("/home/lvuser/autoProfiles/" + newProfile.name + ".auto");
                                new ObjectMapper().writeValue(upload, newProfile);
                                controlOut.println(new ObjectMapper().writeValueAsString(Success.Message(true, MessageType.Update)));
                            }
                            catch (IOException e) {
                                controlOut.println(new ObjectMapper().writeValueAsString(Success.Message(false, MessageType.Update)));
                            }
                            break;

                        case Current: 
                            try {
                                controlOut.println(new ObjectMapper().writeValueAsString(new Message(MessageType.Current, profile.name)));
                            }
                            catch (IOException e) {
                                controlOut.println(new ObjectMapper().writeValueAsString(Success.Message(false, MessageType.Current)));
                            }
                            break;

                        case List:
                            try {
                                ArrayList<String> profiles = new ArrayList<String>();
                                for (File autoProfile : new File("/home/lvuser/autoProfiles/").listFiles()) {
                                    profiles.add(autoProfile.getName());
                                }
                                controlOut.println(new ObjectMapper().writeValueAsString(new Message(MessageType.List, new ObjectMapper().writeValueAsString(profiles.toArray()))));
                            }
                            catch (IOException e) {
                                controlOut.println(new ObjectMapper().writeValueAsString(Success.Message(false, MessageType.List)));
                            }                            
                            break;

                        case Download: 
                            try {
                                controlOut.println(new ObjectMapper().writeValueAsString(new Message(MessageType.Download, new ObjectMapper().writeValueAsString(new ObjectMapper().readValue(new File("/home/lvuser/autoProfiles/" + message.message + ".auto"), AutoProfile.class)))));
                            }
                            catch (IOException e) {
                                controlOut.println(new ObjectMapper().writeValueAsString(Success.Message(false, MessageType.List)));
                            }
                            break;

                        case Success:
                            //We're only sending Success messages
                            break;
                    }
                }
                else {
                    autoController = autoControlServer.accept();   
                    controlIn = new BufferedReader(new InputStreamReader(autoController.getInputStream()));
                    controlOut = new PrintWriter(autoController.getOutputStream(), true);   
                }
            }
            catch (IOException e) {
                System.out.println("Auto control error: ");
                System.out.print(e.getLocalizedMessage());
            }
        }
    }

    public void autoTestInit(AutoProfile testProfile) {
        profile = testProfile;
    }

    public void autoInit() {
        //autoControlThread.interrupt();
        started = true;

        queuedActions.addAll(profile.Actions);

        ArrayList<AutoAction> finishedActions = new ArrayList<AutoAction>();

        for (int i = 0; i < queuedActions.size(); i++) {
            if (queuedActions.get(i).startTime == 0) {
                currentActions.add(profile.Actions.get(i));
                finishedActions.add(queuedActions.get(i));
            }
        }

        queuedActions.removeAll(finishedActions);

        autoTimer = new Timer();
        autoTimer.start();
    }

    public void autoPeriodic() {
        ArrayList<AutoAction> finishedActions = new ArrayList<AutoAction>();

        for (int i = 0; i < lateActions.size(); i++) {
            if (lateActions.get(i).Act()) {
                finishedActions.add(lateActions.get(i));

                if (!lateActions.get(i).endDeployed) {
                    for (AutoAction dependent : lateActions.get(i).endActions) {
                        lateActions.add(dependent);
                    }
                    lateActions.get(i).endDeployed = true;
                }
            }
        }

        lateActions.removeAll(finishedActions);

        finishedActions = new ArrayList<AutoAction>();
        
        for (int i = 0; i < queuedActions.size(); i++) {
            if (queuedActions.get(i).startTime < autoTimer.get()) { //getMatchTime returns the time remaining in auto / teleop, not time elapsed
                currentActions.add(profile.Actions.get(i));
                finishedActions.add(queuedActions.get(i));
            }
        }

        queuedActions.removeAll(finishedActions);

        finishedActions = new ArrayList<AutoAction>();

        for (int i = 0; i < currentActions.size(); i++) {
            if (currentActions.get(i).Act()) {
                switch (currentActions.get(i).earlyMode) { //Early modes are explained in AutoAction
                    case Continuous:
                        for (AutoAction dependent : currentActions.get(i).endActions) {
                            queuedActions.add(dependent);
                            dependent.startTime = currentActions.get(i).endTime;
                        }
                        currentActions.get(i).endDeployed = true;
                        break;

                    case Minimumn:
                        for (AutoAction dependent : currentActions.get(i).endActions) {
                            queuedActions.add(dependent);
                            dependent.startTime = currentActions.get(i).endTime;
                        }
                        currentActions.get(i).endDeployed = true;
                        finishedActions.add(currentActions.get(i));
                        break;

                    case End:
                        for (AutoAction dependent : currentActions.get(i).endActions) {
                            currentActions.add(dependent);
                        }
                        currentActions.get(i).endDeployed = true;
                        finishedActions.add(currentActions.get(i));
                        break;
                }
            }
            else if (currentActions.get(i).endTime >= autoTimer.get()) {
                if (currentActions.get(i).earlyMode == EarlyEndMode.Continuous && currentActions.get(i).endDeployed)
                    finishedActions.add(currentActions.get(i));
                else {
                    switch (currentActions.get(i).lateMode) { //Late modes are explained in AutoAction
                        case Continue:
                            for (AutoAction dependent : currentActions.get(i).endActions) {
                                currentActions.add(dependent);
                            }
                            currentActions.get(i).endDeployed = true;
                            break;

                        case Enforce:
                            for (AutoAction dependent : currentActions.get(i).endActions) {
                                currentActions.add(dependent);
                            }
                            currentActions.get(i).endDeployed = true;
                            finishedActions.add(currentActions.get(i));
                            break;

                        case Rely:
                            finishedActions.add(currentActions.get(i));
                            break;

                        default:
                            finishedActions.add(currentActions.get(i));
                            lateActions.add(currentActions.get(i));
                            break;
                    }
                }
            }
        }

        currentActions.removeAll(finishedActions);
    }

    public void displayData() {
        //SmartDashboard.putNumber("Step", AutoProfileNew.getOriginalList().size() - AutoProfileNew.AutoActions.size() + 1);
    }

    public static class Utils {
        //public static GamePieceController gamepieceController = new GamePieceController();
        public static SwerveDrive swerveDrive = new SwerveDrive();
    }
}