package ftp.server.messages;

import ftp.server.states.DfaState;

public class EPSV extends Message {
    @Override
    public String process(DfaState state) {

        return "502 Command not implemented.";
    }
}
