package lists;

import image.In;
import org.junit.Test;
import static org.junit.Assert.*;

/** FIXME
 *
 *  @Ree
 */

public class ListsTest {



    @Test
    public void basicRunsTest() {
        IntList input = IntList.list(1, 2, 3, 1, 2);
        IntList run1 = IntList.list(1, 2, 3);
        IntList run2 = IntList.list(1, 2);
        IntListList result = IntListList.list(run1, run2);
        assertEquals(result, Lists.naturalRuns(input));

        IntList in = IntList.list(1, 2, 3, 1, 2, 1, 2);
        IntList ru1 = IntList.list(1, 2, 3);
        IntList ru2 = IntList.list(1, 2);
        IntList ru3 = IntList.list(1, 2);
        IntListList re = IntListList.list(ru1, ru2,ru3);
        assertEquals(re, Lists.naturalRuns(in));
    }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ListsTest.class));
    }
}
