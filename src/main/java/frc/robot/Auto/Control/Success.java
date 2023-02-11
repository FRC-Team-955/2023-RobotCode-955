package frc.robot.Auto.Control;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import frc.robot.Auto.Control.Message.MessageType;

public class Success {
    public boolean successful;

    public Message.MessageType type;

    public static Message Message(boolean successful, Message.MessageType type) {
        Success success = new Success();
        success.successful = successful;
        success.type = type;

        try {
            return new Message(MessageType.Success, new ObjectMapper().writeValueAsString(success));
        }
        catch (IOException e) {
            System.out.println("Error when creating success message: ");
            System.out.print(e.getLocalizedMessage());
            return null;
        }
    }
}
