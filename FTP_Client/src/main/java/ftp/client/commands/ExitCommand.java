package ftp.client.commands;

import ftp.client.messages.Message;
import ftp.client.messages.QuitMessage;

public class ExitCommand extends Command {


    public void execute() {
        Message quitMessage = new QuitMessage();
        quitMessage.execute();


    }
}
