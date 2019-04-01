package ftp.server.messages;

import ftp.server.states.DfaState;

public class HELP extends Message {
    @Override
    public String process(DfaState state) {

        StringBuilder msg = new StringBuilder();

        msg.append("211 This server supports the following commands:\n");
        msg.append("CDUP    HELP    PORT\n");
        msg.append("CWD     LIST    PWD\n");
        msg.append("EPRT    PASS    QUIT\n");
        msg.append("EPSV    PASV    RETR\n");
        msg.append("USER");

        return msg.toString();
    }
}
