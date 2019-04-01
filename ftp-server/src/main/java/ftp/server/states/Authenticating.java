package ftp.server.states;

import ftp.server.FtpServer;
import ftp.server.UserCredentials;
import ftp.server.messages.Message;
import ftp.server.messages.PASS;
import ftp.server.messages.QUIT;

import java.nio.file.Path;

public class Authenticating extends DfaState {

    public boolean userOk;
    public UserCredentials user;

    public Authenticating(Path currentDir) {
        super(currentDir);
    }

    public void processTransition(Message message, FtpServer server) throws Exception {

        // Can exit from any state
        if (message.getClass().equals(QUIT.class))
            return;


        if (!message.getClass().equals(PASS.class)) {
            server.setState(new NotAuthenticated(currentDir));
            throw new Exception("503 Bad sequence of commands.");
        }
    }

}
