package com.chatapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleHelper {
    private static BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

    public static void writeMessage(String message) {
        System.out.println(message);
    }

    public static String readString() {
        String s = "";
        while (s.equals("")) {
            try {
                s = bufferedReader.readLine();
            } catch (IOException e) {
                System.out.println("An error occurred while trying to enter text. Try again.");
            }
        }

        return s;
    }

    public static int readInt() {
        int n = 0;
        try {
            n = Integer.parseInt(String.valueOf(readString()));
        } catch (NumberFormatException e) {
            System.out.println("An error while trying to enter a number. Try again.");
            try {
                n = Integer.parseInt(String.valueOf(readString()));
            } catch (NumberFormatException ex) {

            }
        }
        return n;
    }

}
