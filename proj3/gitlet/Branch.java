package gitlet;

import java.util.ArrayList;
import java.util.List;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author Ree
 */

public class Branch implements Dumpable {

    /** jdsflkj. */
    private String branchName;
    /** jdsflkj. */
    private List<Commit> commitList = new ArrayList<>();
    /** jdsflkj. */
    private Commit head;

    public Branch(String branchs) {
        this.branchName = branchs;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchtheB) {
        this.branchName = branchtheB;
    }

    public List<Commit> getCommitList() {
        return commitList;
    }

    public void setCommitList(List<Commit> com) {
        this.commitList = com;
    }
    public Commit getHead() {
        return head;
    }

    public void setHead(Commit foot) {
        this.head = foot;
    }

    public void addCommit(Commit commit) {
        this.commitList.add(commit);
    }

    @Override
    public void dump() {
        System.out.println("branch:" + branchName);
        for (int i = 0; i < commitList.size(); i++) {
            System.out.print("commit: ");
            commitList.get(i).dump();
        }
    }
}
