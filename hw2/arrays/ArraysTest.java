package arrays;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

/** FIXME
 *  @Ree
 */

public class ArraysTest {
    static final int[] type1 = new int[] {6, 4, 30, 16};
    static final int[] type2 = new int[] {4, 77, 16, 78};
    static final int[] type3 = new int[] {99, 3, 20};

    static final int[] re = new int[]{6, 4, 30, 16, 4, 77, 16, 78};
    static final int[] re1 = new int[]{6, 4, 30, 16, 99, 3, 20};
    static final int[] re3 = new int[]{6, 4, 30, 20};

    @Test
    public void testCatenate(){
        assertArrayEquals(re, Arrays.catenate(type1, type2));
        assertArrayEquals(re1, Arrays.catenate(type1, type3));
    }

    @Test
    public void testremove(){
        assertArrayEquals(type2, Arrays.remove(re, 0, 4));
        assertArrayEquals(re3, Arrays.remove(re1, 3, 3));

    }


    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ArraysTest.class));
    }
}
