/**
 * Created by ebricco on 7/26/18.
 */
public class Test {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        for(int i=0; i<10; i++) {
            System.out.println(System.currentTimeMillis() - 123456778);
        }
        System.out.println(System.currentTimeMillis() - start);
    }
}
