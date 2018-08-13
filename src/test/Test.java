import eastin.Survive.objects.MainCharacter;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by ebricco on 7/26/18.
 */
public class Test {
    public static void main(String[] args) {
        //Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        //calendar.setTime(date);

        System.out.println(calendar.get(Calendar.MONTH));
    }
}
