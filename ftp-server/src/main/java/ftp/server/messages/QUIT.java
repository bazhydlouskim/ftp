package ftp.server.messages;

import ftp.server.states.DfaState;

public class QUIT extends Message {
    @Override
    public String process(DfaState state) {

        return "221 Service closing control connection.";
    }
}
