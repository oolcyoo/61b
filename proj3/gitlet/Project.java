package gitlet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author Ree
 */
public class Project implements Dumpable {
    /** jdsflkj. */
    private Branch currentBranch;
    /** jdsflkj. */
    private List<Branch> branchList = new ArrayList<>();

    public Map<String, Commit> getCommits() {
        return commits;
    }

    public void setCommits(Map<String, Commit> commitses) {
        this.commits = commitses;
    }
    /** jdsflkj. */
    private Map<String, Commit> commits = new HashMap<>();

    public List<Branch> getBranchList() {
        return branchList;
    }

    public void setBranchList(List<Branch> branchLists) {
        this.branchList = branchLists;
    }

    public void addBranch(Branch branch) {
        this.branchList.add(branch);
    }

    public void rmBranch(Branch branch) {
        this.branchList.remove(branch);
    }

    public Branch getCurrentBranch() {
        return currentBranch;
    }

    public void setCurrentBranch(Branch currentBranches) {
        this.currentBranch = currentBranches;
    }

    public Branch getBranch(String name) {
        for (Branch b : this.branchList) {
            if (b.getBranchName().equals(name)) {
                return b;
            }
        }
        return null;
    }

    @Override
    public void dump() {
        for (Branch branch : branchList) {
            System.out.println("branch");
            branch.dump();
        }
    }
}
