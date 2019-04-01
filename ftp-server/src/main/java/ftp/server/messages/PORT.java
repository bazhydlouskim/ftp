package ftp.server.messages;

import ftp.server.states.DfaState;

public class PORT extends Message {
    @Override
    public String process(DfaState state) {

        return "502 Command not implemented.";
    }
}
