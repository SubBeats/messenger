package org.example.Server;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static String address;
    private static int port;
    private static final int SIZE = 5;  // Размер двумерных массивов

    private static int[][] integerArray = new int[SIZE][SIZE];
    private static double[][] doubleArray = new double[SIZE][SIZE];
    private static String[][] stringArray = new String[SIZE][SIZE];

    public static void main(String[] args) {
        try {
            address = args[0];
            port = Integer.parseInt(args[1]);

            InetAddress ipAddress = InetAddress.getByName(address);

            ServerSocket serverSocket = new ServerSocket(port);

            System.out.println("Сервер ожидает подключения...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket, args[2]).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler extends Thread {
        private final Socket clientSocket;
        private final String fileName;

        public ClientHandler(Socket clientSocket, String fileName) {
            this.clientSocket = clientSocket;
            this.fileName = fileName;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                while (true) {
                    String request = in.readLine();
                    if (request == null || request.equalsIgnoreCase("exit")) {
                        break;
                    }

                    try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true))) {
                        bw.append("From client: " + request);
                        bw.append("\n");
                        bw.flush();
                    }

                    String[] tokens = request.split(",");
                    int arrayIndex = Integer.parseInt(tokens[0]);
                    int cellIndex = Integer.parseInt(tokens[1]);

                    if (tokens.length > 2) {
                        int defaultValue = Integer.parseInt(tokens[2]);
                        updateArrays(arrayIndex, cellIndex, defaultValue);
                        out.println("значение ячейки изменено : " + getArrayValue(arrayIndex, cellIndex));
                    }
                    out.println("значение ячейки : " + getArrayValue(arrayIndex, cellIndex));

                }
            } catch (IOException e) {
                System.err.println("Ошибка при обработке клиента: " + e.getMessage());
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    System.err.println("Ошибка при закрытии клиентского сокета: " + e.getMessage());
                }
            }
        }

        private void updateArrays(int arrayIndex, int cellIndex, int defaultValue) {
            if (arrayIndex == 0) {
                for (int i = 0; i < SIZE; i++) {
                    integerArray[i][cellIndex] = defaultValue;
                }
            } else if (arrayIndex == 1) {
                for (int i = 0; i < SIZE; i++) {
                    doubleArray[i][cellIndex] = defaultValue;
                }
            } else if (arrayIndex == 2) {
                for (int i = 0; i < SIZE; i++) {
                    stringArray[i][cellIndex] = String.valueOf(defaultValue);
                }
            }

            printArrays();
        }

        private String getArrayValue(int arrayIndex, int cellIndex) {
            if (arrayIndex == 0) {
                return String.valueOf(integerArray[cellIndex][cellIndex]);
            } else if (arrayIndex == 1) {
                return String.valueOf(doubleArray[cellIndex][cellIndex]);
            } else if (arrayIndex == 2) {
                return stringArray[cellIndex][cellIndex];
            } else {
                return "Неверный индекс массива";
            }
        }

        private void printArrays() {
            System.out.println("Массив целочисленных:");
            for (int[] row : integerArray) {
                for (int num : row) {
                    System.out.print(num + " ");
                }
                System.out.println();
            }

            System.out.println("Массив вещественных:");
            for (double[] row : doubleArray) {
                for (double num : row) {
                    System.out.print(num + " ");
                }
                System.out.println();
            }

            System.out.println("Массив строковых:");
            for (String[] row : stringArray) {
                for (String str : row) {
                    System.out.print(str + " ");
                }
                System.out.println();
            }
        }
    }
}