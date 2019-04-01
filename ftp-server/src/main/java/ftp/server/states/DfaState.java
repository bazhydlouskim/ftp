package ftp.server.states;

import ftp.server.FtpServer;
import ftp.server.messages.Message;

import java.nio.file.Path;

public class DfaState {

    public Path currentDir;

    public DfaState (Path dir) {
        currentDir = dir;
    }


    public void processTransition(Message message, FtpServer server) throws Exception {

        throw new Exception("502 Command not implemented");

    }
}
