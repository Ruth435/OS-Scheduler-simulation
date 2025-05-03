package barScheduling;

import java.util.Arrays;

public class StatsUtil {
    public static double mean(long[] values) {
        return Arrays.stream(values).average().orElse(0);
    }

    public static double median(long[] values) {
        Arrays.sort(values);
        int n = values.length;
        return n % 2 == 0 ? (values[n/2 - 1] + values[n/2]) / 2.0 : values[n/2];
    }

    public static double variance(long[] values) {
        if (values.length < 2) return 0;
        double mean = mean(values);
        double var = 0;
        for(int i=0;i<values.length;i++)
        {
            var += (double)((values[i]-mean)*(values[i]-mean));
        }
        return var/(values.length-1);
    }
    

    public static double mean(int[] values) {
        return Arrays.stream(values).average().orElse(0);
    }

    public static double median(int[] values) {
        Arrays.sort(values);
        int n = values.length;
        return n % 2 == 0 ? (values[n/2 - 1] + values[n/2]) / 2.0 : values[n/2];
    }

    public static double variance(int[] values) {
        if (values.length < 2) return 0;
        double mean = mean(values);
        double var = 0;
        for(int i=0;i<values.length;i++)
        {
            var += (double)((values[i]-mean)*(values[i]-mean));
        }
        return var/(values.length-1);
    }

    public static double stdDev(long[] values) {
        return Math.sqrt(variance(values));
    }

    public static double stdDev(int[] values) {
        return Math.sqrt(variance(values));
    }
}
