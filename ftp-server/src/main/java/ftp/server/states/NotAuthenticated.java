package ftp.server.states;

import ftp.server.FtpServer;
import ftp.server.messages.HELP;
import ftp.server.messages.Message;
import ftp.server.messages.QUIT;
import ftp.server.messages.USER;

import java.nio.file.Path;

public class NotAuthenticated extends DfaState {

    public NotAuthenticated(Path dir) {
        super(dir);
    }

    public void processTransition(Message message, FtpServer server) throws Exception {

        // Can exit from any state
        if (message.getClass().equals(QUIT.class))
            return;


        if (!(message.getClass().equals(USER.class) || (message.getClass().equals(HELP.class))))
            throw new Exception("530 Not logged in.");

        if (message.getClass().equals(USER.class))
            server.setState(new Authenticating(currentDir));


    }


}
