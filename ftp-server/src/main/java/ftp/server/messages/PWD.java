package ftp.server.messages;

import ftp.server.states.DfaState;

public class PWD extends Message {
    @Override
    public String process(DfaState state) {

        return "257 " + state.currentDir.toAbsolutePath().toString();
    }
}
