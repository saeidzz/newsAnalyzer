package ir.jiring.assignment.service;

import ir.jiring.assignment.dto.News;
import ir.jiring.assignment.exception.SocketAcceptanceException;
import ir.jiring.assignment.exception.SocketDispatchingException;
import ir.jiring.assignment.exception.SocketStoppingException;
import ir.jiring.assignment.task.PriorityTask;
import ir.jiring.assignment.util.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TreeMap;


public class Analyzer {
    private ServerSocket serverSocket;

    private SortedMap<Short, News> highestPriorityNews = new TreeMap<>();
    private Integer count = 0;

    public SortedMap<Short, News> getHighestPriorityNews() {
        return highestPriorityNews;
    }

    public void reset() {
        this.highestPriorityNews = new TreeMap<>();
        this.count = 0;
    }

    public Integer getCount() {
        return count;
    }

    public void start(int port) {

        try {
            serverSocket = new ServerSocket(port);
            while (true) {
                new EchoNewsHandler(serverSocket.accept()).start();
                highestPriorityNews = new TreeMap<>();
                Timer timer = new Timer();
                timer.scheduleAtFixedRate(new PriorityTask(this), 10000L, 10000L);
            }
        } catch (IOException exception) {
            throw new SocketAcceptanceException();
        }

    }

    public void stop() {

        try {
            serverSocket.close();
        } catch (IOException exception) {
            throw new SocketStoppingException();
        }
    }

    private class EchoNewsHandler extends Thread {
        private final Socket clientSocket;

        public EchoNewsHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {

            try {
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));

                String inputNews;
                while ((inputNews = in.readLine()) != null) {
                    if ("terminate".equals(inputNews)) {
                        out.println("bye");
                        serverSocket.close();
                        break;
                    }
                    out.println(inputNews);

                    highestPriorityNews = Util.addHighestPriorities(highestPriorityNews, inputNews);
                    if (Util.isPositive(inputNews)) {
                        count++;
                    }
                }
                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException exception) {
                throw new SocketDispatchingException();
            }
        }

    }

}

