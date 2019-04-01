package ftp.server.messages;

import ftp.server.FtpServer;
import ftp.server.FtpServerMain;
import ftp.server.states.DfaState;

public abstract class Message {

    public FtpServer server;

    public void getArguments(String[] tokens) throws Exception {



    }

    public abstract String process(DfaState state);

}
