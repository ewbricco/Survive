import java.util.Calendar;

/**
 * Created by ebricco on 7/26/18.
 */
public class Test {
    public static void main(String[] args) {
        //Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        //calendar.setTime(date);

        System.out.println(calendar.get(Calendar.MONTH));

        String.valueOf(true);

        Integer one = new Integer(1);

        if(calendar.get(Calendar.MONTH) == 7) {
            one = new Integer(2);
        }

        System.out.println(one);
    }
}
