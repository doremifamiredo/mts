package mts.pay;

public class DataHelper {
    public static float getCostInfo(String costInfo) {
        var cut = costInfo.indexOf(" BYN");
        var value = costInfo.substring(0, cut);
        return Float.parseFloat(value);
    }
}
