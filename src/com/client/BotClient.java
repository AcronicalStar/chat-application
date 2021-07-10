package com.client;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import com.chatapp.ConsoleHelper;

public class BotClient extends Client {
    @Override
    protected String getUserName() {
        return "date_bot_" + (int)(100.0 * Math.random());
    }

    @Override
    protected boolean shouldSendTextFromConsole() {
        return false;
    }

    @Override
    protected SocketThread getSocketThread() {
        return new BotSocketThread();
    }

    public static void main(String[] args) {
        BotClient botClient = new BotClient();
        botClient.run();
    }

    public class BotSocketThread extends SocketThread {
        @Override
        protected void clientMainLoop() throws IOException, ClassNotFoundException {
            BotClient.this.sendTextMessage("Hello, there. I'm a bot. I understand the following commands: date, day, month, year, time, hour, minutes, seconds.");
            super.clientMainLoop();
        }

        @Override
        protected void processIncomingMessage(String message) {
            ConsoleHelper.writeMessage(message);
            if (message.contains(":")) {
                String[] nameAndMessage = message.split(": ");
                String name = nameAndMessage[0];
                String text = nameAndMessage[1];
                SimpleDateFormat sdf = null;
                Calendar calendar = Calendar.getInstance();
                if (text.equals("date")) {
                    Date date = calendar.getTime();
                    sdf = new SimpleDateFormat("d.MM.YYYY");
                    sendTextMessage("Information for " + name + ": " + sdf.format(date));
                } else if (text.equals("day")) {
                    Date date = calendar.getTime();
                    sdf = new SimpleDateFormat("d");
                    sendTextMessage("Information for " + name + ": " + sdf.format(date));
                } else if (text.equals("month")) {
                    Date date = calendar.getTime();
                    sdf = new SimpleDateFormat("MMMM");
                    sendTextMessage("Information for " + name + ": " + sdf.format(date));
                } else if (text.equals("year")) {
                    Date date = calendar.getTime();
                    sdf = new SimpleDateFormat("YYYY");
                    sendTextMessage("Information for " + name + ": " + sdf.format(date));
                } else if (text.equals("time")) {
                    Date date = calendar.getTime();
                    sdf = new SimpleDateFormat("H:mm:ss");
                    sendTextMessage("Information for " + name + ": " + sdf.format(date));
                } else if (text.equals("hour")) {
                    Date date = calendar.getTime();
                    sdf = new SimpleDateFormat("H");
                    sendTextMessage("Information for " + name + ": " + sdf.format(date));
                } else if (text.equals("minutes")) {
                    Date date = calendar.getTime();
                    sdf = new SimpleDateFormat("m");
                    sendTextMessage("Information for " + name + ": " + sdf.format(date));
                } else if (text.equals("seconds")) {
                    Date date = calendar.getTime();
                    sdf = new SimpleDateFormat("s");
                    sendTextMessage("Information for " + name + ": " + sdf.format(date));
                }
            }
        }
    }
}
