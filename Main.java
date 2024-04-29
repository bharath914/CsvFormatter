import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        int [] nums = {2,3,5,2,1};
        int[] sorted = Arrays.stream(nums).sorted().distinct().toArray()   ;
    }
}
