package ftp.server.messages;

import ftp.server.FtpServerMain;
import ftp.server.UserCredentials;
import ftp.server.states.Authenticating;
import ftp.server.states.DfaState;

public class USER extends Message {

    public String username;



    @Override
    public void getArguments(String[] tokens) throws Exception {
        try {
            username = tokens[1];
        } catch (Exception e) {
            throw new Exception("501 Syntax error in parameters or arguments.");
        }
    }

    @Override
    public String process(DfaState state) {

        try {
            UserCredentials authorizedUser = FtpServerMain.users.stream().filter(x -> x.username.equals(username)).findFirst().orElse(null);



            if (authorizedUser != null) { // Hardcoded this time
                ((Authenticating) state).userOk = true;
                ((Authenticating) state).user = authorizedUser;
            } else {
                ((Authenticating) state).userOk = false;
                ((Authenticating) state).user = null;
            }
            FtpServerMain.logger.writeLog("User tried to log in : " + username, server.socket);
        } catch (Exception e) {
            // Handle it quietly, avoid information leakage
            FtpServerMain.logger.writeLog("USER command failed!", server.socket);
        }

        return "331 User name okay, need password.";
    }
}
