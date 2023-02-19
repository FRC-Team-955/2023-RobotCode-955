package frc.robot.Auto.Control;

public class Message {
    public enum MessageType {
        Set, //Set the active auto profile
        Upload, //A new auto profile from the controller
        Update, //An updated version of an auto profile from a controller
        Current, //Get the information about the current auto profile
        List, //Get all available auto profiles
        Download, //Send an auto profile to the controller

        Success //Confirm or deny successful completion of a message
    } 

    public MessageType messageType;

    public String message;

    public Message(MessageType type, String msg) {
        messageType = type;
        message = msg;
    }
}