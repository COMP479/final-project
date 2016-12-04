package Sentiment;

import java.util.Arrays;

/**
 * Created by pyoung on 2016-12-04.
 */
public class Statistics
{
    double[] sentimentScores;
    int size;

    public Statistics(double[] sentimentScores) {
        this.sentimentScores = sentimentScores;
        size = sentimentScores.length;
    }

    public double getMean() {
        double sum = 0.0;
        for(double a : sentimentScores)
            sum += a;
        return sum/size;
    }

    private double getVariance() {
        double mean = getMean();
        double temp = 0;
        for(double a :sentimentScores)
            temp += (a-mean)*(a-mean);
        return temp/size;
    }

    public double getStandardDeviation() {
        return Math.sqrt(getVariance());
    }

    public double getMedian() {
        Arrays.sort(sentimentScores);

        if (sentimentScores.length % 2 == 0) {
            return (sentimentScores[(sentimentScores.length / 2) - 1] + sentimentScores[sentimentScores.length / 2]) / 2.0;
        }
        return sentimentScores[sentimentScores.length / 2];
    }
}
