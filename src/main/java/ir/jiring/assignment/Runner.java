package ir.jiring.assignment;

import ir.jiring.assignment.service.Analyzer;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
public class Runner {
    public static void main(String[] args) {
        Analyzer server = new Analyzer();
        server.start(6666);
    }
}
