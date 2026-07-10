import static org.junit.Assert.*;
import org.junit.Test;

public class MultiArrTest {
    static final int[][] test1 = new int[][] {{1, 2, 3}, {4}, {5, 6, 11, 8}, {7, 9}};
    static final int[][] test2 = new int[][] {{1, 3}, {77}, {7, 9}, {7, 3, 66, 2}};
    static final int[][] test3 = new int[][] {{99}, {3}, {7, 10, -6, 9}};
    static final int[] type1 = new int[] {6, 4, 30, 16};
    static final int[] type2 = new int[] {4, 77, 16, 78};
    static final int[] type3 = new int[] {99, 3, 20};

    @Test
    public void testMaxValue() {
        assertEquals(11, MultiArr.maxValue(test1));
        assertEquals(77, MultiArr.maxValue(test2));
        assertEquals(99, MultiArr.maxValue(test3));

    }

    @Test
    public void testAllRowSums() {
        assertArrayEquals(type1, MultiArr.allRowSums(test1));
        assertArrayEquals(type2, MultiArr.allRowSums(test2));
        assertArrayEquals(type3, MultiArr.allRowSums(test3));
    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(MultiArrTest.class));
    }
}
