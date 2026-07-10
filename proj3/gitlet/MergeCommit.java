package gitlet;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author Ree
 */

public class MergeCommit extends Commit {
    /** jdsflkj. */
    private Commit merged;

    public Commit getMerged() {
        return merged;
    }

    public void setMerged(Commit mergee) {
        this.merged = mergee;
    }
}
