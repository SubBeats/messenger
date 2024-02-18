package org.example.Client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        String adress;
        int port;
        try {
            adress = args[0];
            port = Integer.parseInt(args[1]);

            Socket socket = new Socket(adress, port);

            sendToServer(socket);
            readFromServer(socket,args[2]);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendToServer(Socket socket) throws IOException {
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        out.println("1,1,15");
    }

    public static void readFromServer(Socket socket,String fileName) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String response = in.readLine();
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(fileName,true))) {
            bw.append("Message from server: " + response);
            bw.append("\n");
            bw.flush();
        }
    }
}