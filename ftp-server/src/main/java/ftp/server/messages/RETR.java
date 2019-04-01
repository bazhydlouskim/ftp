package ftp.server.messages;

import ftp.server.states.DfaState;

public class RETR extends Message {

    public String fileName;


    @Override
    public void getArguments(String[] tokens) throws Exception {
        try {
            fileName = tokens[1];
        } catch (Exception e) {
            throw new Exception("501 Syntax error in parameters or arguments.");
        }
    }

    @Override
    public String process(DfaState state) {




        return "502 Command not implemented.";
    }
}
