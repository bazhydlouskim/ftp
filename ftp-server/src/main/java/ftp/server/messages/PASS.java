package ftp.server.messages;

import ftp.server.Logger;
import ftp.server.states.Authenticated;
import ftp.server.states.Authenticating;
import ftp.server.states.DfaState;

public class PASS extends Message {

    private String password;


    @Override
    public void getArguments(String[] tokens) throws Exception {
        try {
            password = tokens[1];
        } catch (Exception e) {
            throw new Exception("501 Syntax error in parameters or arguments.");
        }
    }

    @Override
    public String process(DfaState state) {


        try {
            if (((Authenticating)state).userOk && ((Authenticating)state).user.password.equals(password)) { // Hardcoded this time
                server.state = new Authenticated(server.state.currentDir);
                return "230 User logged in, proceed.";
            } else {
                return "530 Not logged in.";
            }
        } catch (Exception e) {
            // Handle it quietly, avoid information leakage
            e.printStackTrace();
            return "530 Not logged in.";
        }

    }
}
