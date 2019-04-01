package ftp.server.states;

import ftp.server.FtpServer;
import ftp.server.messages.Message;
import ftp.server.messages.PASS;
import ftp.server.messages.QUIT;
import ftp.server.messages.USER;

import java.nio.file.Path;

public class Transferring extends DfaState {
    public Transferring(Path dir) {
        super(dir);
    }

    public void processTransition(Message message, FtpServer server) throws Exception {

        // Can exit from any state
        if (message.getClass().equals(QUIT.class))
            return;


        if (message.getClass().equals(USER.class))
            server.setState(new Authenticating(currentDir));

        else if (message.getClass().equals(PASS.class))
            throw new Exception("503 Bad sequence of commands.");

        // All other commands don't change the state

    }
}
