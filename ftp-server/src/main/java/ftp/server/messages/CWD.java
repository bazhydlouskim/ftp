package ftp.server.messages;

import ftp.server.states.DfaState;

import java.nio.file.Path;

public class CWD extends Message {

    public String directory;

    @Override
    public void getArguments(String[] tokens) throws Exception {
        try {
            directory = tokens[1];
        } catch (Exception e) {
            throw new Exception("Invalid usage!");
        }
    }

    @Override
    public String process(DfaState state) {

        state.currentDir = state.currentDir.resolve(directory);

        return "";
    }
}
