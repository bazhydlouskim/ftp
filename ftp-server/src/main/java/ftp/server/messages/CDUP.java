package ftp.server.messages;

import ftp.server.FtpServerMain;
import ftp.server.states.DfaState;

public class CDUP extends Message {

    @Override
    public String process(DfaState state) {

        try {
            state.currentDir = state.currentDir.getParent();

            return "200 Command okay.";

        } catch (Exception e) {
            FtpServerMain.logger.writeLog("CDUP Failed!");
            return "550 Requested action not taken.";
        }
    }
}
