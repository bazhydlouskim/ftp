package ftp.server.states;

import ftp.server.FtpServer;
import ftp.server.FtpServerMain;
import ftp.server.messages.*;

import java.nio.file.Path;

public class Authenticated extends DfaState {


    public Authenticated(Path dir) {
        super(dir);
    }

    public void processTransition(Message message, FtpServer server) throws Exception {

        // Can exit from any state
        if (message.getClass().equals(QUIT.class))
            return;

        else if (message.getClass().equals(USER.class))
            server.setState(new Authenticating(currentDir));

        else if (message.getClass().equals(PASS.class))
            throw new Exception("503 Bad sequence of commands.");

        else if (message.getClass().equals(RETR.class))
            server.setState(new Transferring(currentDir));

        else if (((message.getClass().equals(PASV.class)) || (message.getClass().equals(EPSV.class)))
                && !FtpServerMain.pasvSupported)
            throw new Exception("500 Command not supported.");

        else if (((message.getClass().equals(PORT.class)) || (message.getClass().equals(EPRT.class)))
                && !FtpServerMain.portSupported)
            throw new Exception("500 Command not supported.");

        // All other commands don't change this state

    }

}
