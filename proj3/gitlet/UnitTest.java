package gitlet;

import ucb.junit.textui;
import org.junit.Test;
import static org.junit.Assert.*;

/** The suite of all JUnit tests for the gitlet package.
 *  @author
 */
public class UnitTest {

    /** Run the JUnit tests in the loa package. Add xxxTest.class entries to
     *  the arguments of runClasses to run other JUnit tests. */
    public static void main(String[] ignored) {
        System.exit(textui.runClasses(UnitTest.class));
    }

    /** A dummy test to avoid complaint. */
    @Test
    public void placeholderTest() {
    }

    public static void main1(String[] args) {
        GitManager manager = new GitManager();
        manager.init();
        manager.add("proj3.iml");
        manager.commit("add file");
        manager.remove("proj3.iml");
        manager.status();
        System.out.println("--end");
    }

}


