package ir.jiring.assignment.task;

import ir.jiring.assignment.service.Analyzer;
import ir.jiring.assignment.util.Util;

import java.util.TimerTask;
import java.util.logging.Level;

public class PriorityTask extends TimerTask {
    Analyzer analyzer;

    public PriorityTask(Analyzer analyzer) {
        this.analyzer = analyzer;
    }


    @Override
    public void run() {

        Util.log(Level.INFO, "positive news counts last 10 seconds :{0} ", analyzer.getCount());
        Util.log(Level.INFO, "top priority news is  : {0}\n", Util.toJson(analyzer.getHighestPriorityNews()));

        analyzer.reset();
    }
}