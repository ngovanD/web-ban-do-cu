package haui.cntt.myproject.common.otp;

import java.util.Random;

public class RandomOtpUtil {
    private static Random rand = new Random();

    private RandomOtpUtil() {
        super();
    }

    public static String createOtp() {
        System.out.println("");
        int upperbound = 10000;
        //generate random values from 0-9999
        int intRandom = rand.nextInt(upperbound);

        return String.format("%04d", intRandom);
    }
}
