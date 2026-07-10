package gitlet;
import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.HashSet;
import java.util.TreeSet;



/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author Ree
 */
public class GitManager {
    /** jdsflkj. */
    private String rOOTPATH = ".gitlet";
    /** jdsflkj. */
    private boolean init;
    /** jdsflkj. */
    private Map<String, GitManager> remotes = new HashMap<>();

    public GitManager() {
        this.init = false;
        this.rOOTPATH = ".gitlet";
    }

    public GitManager(String path) {
        this.init = false;
        this.rOOTPATH = path;
    }

    public boolean checkInit() {
        File folder = new File(rOOTPATH);
        return folder.exists() || folder.isDirectory();
    }

    public void init() {
        if (checkInit()) {
            System.out.println("A Gitlet version-control system "
                    + "already exists in the current directory.");
            return;
        }
        File folder = new File(rOOTPATH);
        folder.mkdirs();
        Project project = new Project();
        Branch branch = new Branch("master");
        Commit commit = new Commit();
        commit.setComment("initial commit");
        commit.setTimestamp(0);
        commit.setUid(Utils.sha1("initial commit" + 0));
        branch.addCommit(commit);
        branch.setHead(commit);
        project.addBranch(branch);
        project.setCurrentBranch(branch);
        Utils.writeObject(Paths.get(rOOTPATH, ".project").toFile(), project);
        StagingArea stagingArea = new StagingArea();
        Utils.writeObject(Paths.get(rOOTPATH, ".stage").toFile(), stagingArea);
    }

    public void add(String fileName) {
        if (!checkInit()) {
            System.out.println("Not in an initialized Gitlet directory.");
            return;
        }
        File file = new File(fileName);
        if (file.isDirectory() || !file.exists()) {
            System.out.println("File does not exist.");
            return;
        }
        Project project = Utils.readObject(Paths.
                get(rOOTPATH, ".project").toFile(), Project.class);
        StagingArea stagingArea = Utils.readObject(Paths.
                get(rOOTPATH, ".stage").toFile(), StagingArea.class);
        byte[] context = Utils.readContents(file);

        if (project.getCurrentBranch().getHead().
                getCommitFiles().containsKey(fileName)
                && Arrays.equals(project.
                getCurrentBranch().getHead().
                getCommitFiles().get(fileName), context)) {
            stagingArea.removeFile(fileName);
            stagingArea.getRmStageFiles().remove(fileName);
        } else {
            stagingArea.removeFile(fileName);
            stagingArea.addFile(fileName, context);
        }
        Utils.writeObject(Paths.get(rOOTPATH, ".stage").toFile(), stagingArea);
    }

    public void remove(String fileName) {
        if (!checkInit()) {
            System.out.println("Not in an "
                    + "initialized Gitlet directory.");
            return;
        }
        Project project = Utils.readObject(Paths.
                get(rOOTPATH, ".project").toFile(), Project.class);
        StagingArea stagingArea = Utils.readObject(Paths.
                get(rOOTPATH, ".stage").toFile(), StagingArea.class);

        boolean flag = false;
        if (project.getCurrentBranch().getHead().
                getCommitFiles().containsKey(fileName)) {
            stagingArea.addRmFile(fileName);
            Utils.join(fileName).delete();
            flag = true;
        } else if (stagingArea.getStageFiles().containsKey(fileName)) {
            stagingArea.removeFile(fileName);
            flag = true;
        }
        if (!flag) {
            System.out.println("No reason to remove the file.");
            return;
        }
        Utils.writeObject(Paths.get(rOOTPATH, ".stage").toFile(), stagingArea);
    }

    public void commit(String message) {
        if (!checkInit()) {
            System.out.println("Not in an initialized Gitlet directory.");
            return;
        }
        Project project = Utils.readObject(Paths.
                get(rOOTPATH, ".project").toFile(), Project.class);
        StagingArea stagingArea = Utils.readObject(Paths.
                get(rOOTPATH, ".stage").toFile(), StagingArea.class);

        if (stagingArea.getStageFiles().
                size() == 0 && stagingArea.getRmStageFiles().size() == 0) {
            System.out.println("No changes added to the commit.");
            return;
        }
        if (message.isEmpty()) {
            System.out.println("Please enter a commit message.");
            return;
        }

        Commit preCommit = project.getCurrentBranch().getHead();

        Commit commit = new Commit();
        commit.setComment(message);
        commit.setTimestamp(new Date().getTime());
        commit.setPrevious(preCommit);
        commit.setCommitFiles(new HashMap<>(preCommit.getCommitFiles()));

        for (String file : stagingArea.getRmStageFiles()) {
            if (commit.getCommitFiles().containsKey(file)) {
                commit.getCommitFiles().remove(file);
            }
        }

        for (String file : stagingArea.getStageFiles().keySet()) {
            commit.getCommitFiles().put(file, stagingArea.
                    getStageFiles().get(file));
        }

        commit.setUid(Utils.sha1(commit.
                getCommitFiles().toString(), preCommit.
                getUid(), message, new Date().toString()));
        project.getCommits().put(commit.getUid(), commit);
        project.getCurrentBranch().addCommit(commit);
        project.getCurrentBranch().setHead(commit);

        stagingArea.getStageFiles().clear();
        stagingArea.getRmStageFiles().clear();
        Utils.writeObject(Paths.get(rOOTPATH, ".project").toFile(), project);
        Utils.writeObject(Paths.get(rOOTPATH, ".stage").toFile(), stagingArea);
    }

    public void checkout(String fileName) {
        if (!checkInit()) {
            System.out.println("Not in an initialized Gitlet directory.");
            return;
        }
        Project project = Utils.readObject(Paths.
                get(rOOTPATH, ".project").toFile(), Project.class);
        if (project.getCurrentBranch().
                getHead().getCommitFiles().containsKey(fileName)) {
            byte[] data = project.getCurrentBranch().
                    getHead().getCommitFiles().get(fileName);
            Utils.writeContents(new File(fileName), data);
        } else {
            System.out.println("File does not exist in that commit.");
            return;
        }
    }

    public void checkout(String commitID, String fileName) {
        if (!checkInit()) {
            System.out.println("Not in an initialized Gitlet directory.");
            return;
        }
        Project project = Utils.readObject(Paths.
                get(rOOTPATH, ".project").toFile(), Project.class);
        for (Commit commit : project.getCurrentBranch().getCommitList()) {
            if (commit.getUid().startsWith(commitID)) {
                if (commit.getCommitFiles().containsKey(fileName)) {
                    byte[] data = commit.getCommitFiles().get(fileName);
                    Utils.writeContents(new File(fileName), data);
                } else {
                    System.out.println("File does not exist in that commit.");
                    return;
                }
                return;
            }
        }
        System.out.println("No commit with that id exists.");
    }

    public void checkoutBranch(String branchName) {
        if (!checkInit()) {
            System.out.println("Not in an "
                    + "initialized Gitlet directory.");
            return;
        }
        Project project = Utils.readObject(Paths.
                get(rOOTPATH, ".project").toFile(), Project.class);
        StagingArea stagingArea = Utils.readObject(Paths.
                get(rOOTPATH, ".stage").toFile(), StagingArea.class);
        for (Branch branch : project.getBranchList()) {
            if (branch.getBranchName().equals(branchName)) {
                if (branch.equals(project.getCurrentBranch())) {
                    System.out.println("No "
                            + "need to checkout the current branch.");
                    return;
                }
                boolean existUntrack = false;
                Map<String, byte[]> files = project.
                        getCurrentBranch().getHead().getCommitFiles();
                Map<String, byte[]> prefiles = branch.
                        getHead().getCommitFiles();
                helper(branchName, stagingArea, files, prefiles, project);
                for (String file : project.
                        getCurrentBranch().getHead().
                        getCommitFiles().keySet()) {
                    new File(file).delete();
                }
                for (String file : prefiles.keySet()) {
                    byte[] data = prefiles.get(file);
                    Utils.writeContents(new File(file), data);
                }
                for (String file : files.keySet()) {
                    if (!prefiles.containsKey(file)) {
                        Paths.get(file).toFile().delete();
                    }
                }
                project.setCurrentBranch(branch);
                stagingArea.getStageFiles().clear();
                stagingArea.getRmStageFiles().clear();
                Utils.writeObject(Paths.
                        get(rOOTPATH, ".project").toFile(), project);
                Utils.writeObject(Paths.
                        get(rOOTPATH, ".stage").toFile(), stagingArea);
                return;
            }
        }
        System.out.println("No such branch exists.");
    }

    public void helper(String branchName, StagingArea stagingArea,
                       Map<String, byte[]> files, Map<String, byte[]> prefiles,
                       Project project) {
        boolean existUntrack = false;
        for (String f : files.keySet()) {
            if (stagingArea.getStageFiles().
                    containsKey(f) || stagingArea.
                    getRmStageFiles().contains(f)) {
                continue;
            }
            File ff = new File(f);
            if (ff.exists()) {
                byte[] content = Utils.readContents(ff);
                if (!Arrays.equals(files.get(f), content)) {
                    existUntrack = true;
                }
            } else {
                existUntrack = true;
            }
        }
        if (existUntrack) {
            System.out.println("There is an untracked file "
                    + "in the way; delete it, or add "
                    + "and commit it first.");
            return;
        }
        for (String file : prefiles.keySet()) {
            if (project.getCurrentBranch().
                    getHead().getCommitFiles().containsKey(file)) {
                continue;
            }
            if (stagingArea.getStageFiles().
                    containsKey(file) || stagingArea.
                    getRmStageFiles().contains(file)) {
                continue;
            }
            if (new File(file).exists()) {
                byte[] workingfile = Utils.
                        readContents(new File(file));
                byte[] commitfile = prefiles.get(file);
                if (!Arrays.equals(workingfile, commitfile)) {
                    System.out.println("There is an untracked "
                            + "file in the way; delete it, or add "
                            + "and commit it first.");
                    return;
                }
            }
        }
    }

    public void log() {
        if (!checkInit()) {
            System.out.println("Not in an initialized Gitlet directory.");
            return;
        }
        Project project = Utils.readObject(Paths.
                get(rOOTPATH, ".project").toFile(), Project.class);
        Commit head = project.getCurrentBranch().getHead();
        while (head != null) {
            head.dump();
            System.out.println();
            head = head.getPrevious();
        }
    }

    public void globalLog() {
        if (!checkInit()) {
            System.out.println("Not in an initialized Gitlet directory.");
            return;
        }
        Project project = Utils.readObject(Paths.
                get(rOOTPATH, ".project").toFile(), Project.class);
        List<Commit> commitList = project.getCurrentBranch().getCommitList();
        for (int i = commitList.size() - 1; i >= 0; i--) {
            commitList.get(i).dump();
            System.out.println();
        }
    }

    public void find(String msg) {
        if (!checkInit()) {
            System.out.println("Not in an initialized Gitlet directory.");
            return;
        }
        boolean exist = false;
        Project project = Utils.readObject(Paths.
                get(rOOTPATH, ".project").toFile(), Project.class);
        List<Commit> commitList = project.getCurrentBranch().getCommitList();
        for (int i = commitList.size() - 1; i >= 0; i--) {
            if (commitList.get(i).getComment().equals(msg)) {
                System.out.println(commitList.get(i).getUid());
                exist = true;
            }
        }
        if (!exist) {
            System.out.println("Found no commit with that message.");
        }
    }

    public void status() {
        if (!checkInit()) {
            System.out.println("Not in an initialized Gitlet directory.");
            return;
        }
        Project project = Utils.readObject(Paths.
                get(rOOTPATH, ".project").toFile(), Project.class);
        StagingArea stagingArea = Utils.readObject(Paths.
                get(rOOTPATH, ".stage").toFile(), StagingArea.class);
        System.out.println("=== Branches ===");
        List<String> names = new ArrayList<>();
        status1(names, project);
        Collections.sort(names);
        for (String n : names) {
            System.out.println(n);
        }
        System.out.println();
        System.out.println("=== Staged Files ===");
        names = new ArrayList<>(stagingArea.
                getStageFiles().keySet());
        Collections.sort(names);
        for (String n : names) {
            System.out.println(n);
        }
        System.out.println();
        System.out.println("=== Removed Files ===");
        names = new ArrayList<>(stagingArea.
                getRmStageFiles());
        Collections.sort(names);
        for (String n : names) {
            System.out.println(n);
        }
        System.out.println();
        System.out.println("=== Modifications Not Staged For Commit ===");
        names = new ArrayList<>();
        Map<String, byte[]> files = project.
                getCurrentBranch().getHead().getCommitFiles();
        for (String key : stagingArea.getStageFiles().keySet()) {
            files.put(key, stagingArea.getStageFiles().get(key));
        }
        for (String key : stagingArea.getRmStageFiles()) {
            files.remove(key);
        }
        status3(files, names);

        Collections.sort(names);
        for (String n : names) {
            System.out.println(n);
        }
        System.out.println();
        System.out.println("=== Untracked Files ===");

        names = new ArrayList<>();
        status2(files, names);
    }
    public void status1(List<String> names, Project project) {
        for (Branch branch : project.getBranchList()) {
            if (branch.equals(project.getCurrentBranch())) {
                names.add("*" + branch.getBranchName());
            } else {
                names.add(branch.getBranchName());
            }
        }
    }
    public void status2(Map<String, byte[]> files, List<String> names) {
        for (String f : Utils.plainFilenamesIn(".")) {
            if (files.get(f) == null) {
                names.add(f);
            }
        }
        Collections.sort(names);
        for (String n : names) {
            System.out.println(n);
        }
        System.out.println();
    }

    public void status3(Map<String, byte[]> files, List<String> names) {
        Map<String, byte[]> tracked = readfiles(files.keySet());
        for (String key : files.keySet()) {
            if (tracked.get(key) == null) {
                names.add(String.format("%s (deleted)", key));
            } else if (Arrays.compare(files.get(key), tracked.get(key)) != 0) {
                names.add(String.format("%s (modified)", key));
            }
        }
    }

    public void branch(String branchName) {
        if (!checkInit()) {
            System.out.println("Not in an initialized Gitlet directory.");
            return;
        }
        Project project = Utils.readObject(Paths.
                get(rOOTPATH, ".project").toFile(), Project.class);
        for (Branch branch : project.getBranchList()) {
            if (branch.getBranchName().equals(branchName)) {
                System.out.println("A branch with that name already exists.");
                return;
            }
        }
        Branch branch = new Branch(branchName);
        branch.setCommitList(new ArrayList<>(project.
                getCurrentBranch().getCommitList()));
        branch.setHead(project.getCurrentBranch().getHead());
        project.addBranch(branch);
        Utils.writeObject(Paths.
                get(rOOTPATH, ".project").toFile(), project);
    }

    public void rmBranch(String branchName) {
        if (!checkInit()) {
            System.out.println("Not in an initialized Gitlet directory.");
            return;
        }
        Project project = Utils.readObject(Paths.
                get(rOOTPATH, ".project").toFile(), Project.class);
        if (project.getCurrentBranch().
                getBranchName().equals(branchName)) {
            System.out.println("Cannot remove the current branch.");
            return;
        }
        for (Branch branch : project.getBranchList()) {
            if (branch.getBranchName().
                    equals(branchName)) {
                project.rmBranch(branch);
                Utils.writeObject(Paths.
                        get(rOOTPATH, ".project").toFile(), project);
                return;
            }
        }
        System.out.println("A branch "
                + "with that name does not exist.");
    }

    /** jdsflkj. */
    private String code = "There is an untracked "
            + "file in the "
            + "way; delete it, "
            + "or add and "
            + "commit it first.";
    /** jdsflkj. */
    private String code2 = "There is an untracked file "
            + "in the way; delete it, "
            + "or add and commit it first.";
    /** jdsflkj. */
    private String code3 = "There is an untracked file"
            + " in the way; "
            + "delete it, or "
            + "add and commit it first.";
    /** jdsflkj. */
    private String code4 = "Not in "
            + "an initialized Gitlet directory.";
    public void yesOrno() {
        if (!checkInit()) {
            System.out.println(code4);
            return;
        }
    }
    public void reset(String commitID) {
        yesOrno();
        Project project = Utils.readObject(Paths.
                get(rOOTPATH, ".project").
                toFile(), Project.class);
        StagingArea stagingArea = Utils.
                readObject(Paths.get(rOOTPATH, ".stage").
                        toFile(), StagingArea.class);
        for (Branch branch : project.
                getBranchList()) {
            for (Commit commit : branch.
                    getCommitList()) {
                if (commit.getUid().startsWith(commitID)) {
                    reset1(commitID, stagingArea, project);
                    Map<String, byte[]> files = commit.
                            getCommitFiles();
                    for (String file : files.keySet()) {
                        if (!project.getCurrentBranch().
                                getHead().getCommitFiles().
                                containsKey(file)) {
                            if (new File(file).exists()) {
                                byte[] workingfile = Utils.
                                        readContents(new File(
                                        file));
                                byte[] commitfile = files.get(file);
                                if (!Arrays.equals(workingfile,
                                        commitfile)) {
                                    System.out.println(code);
                                    return;
                                }
                            }
                            continue;
                        }
                        if (!new File(file).exists()) {
                            System.out.println(code2);
                            return;
                        } else {
                            byte[] workingfile = Utils.
                                    readContents(new File(file));
                            byte[] commitfile =
                                    project.getCurrentBranch().getHead()
                                            .getCommitFiles().get(file);
                            if (!Arrays.equals(workingfile,
                                    commitfile)) {
                                System.out.println(code3);
                                return;
                            }
                        }
                    }
                    reset2(files, project);
                    project.getCurrentBranch().setHead(commit);
                    reset0(stagingArea, project);
                    return;
                }
            }
        }
        System.out.println("No commit with that id exists.");
    }

    public void reset0(StagingArea stagingArea, Project project) {
        stagingArea.getStageFiles().clear();
        stagingArea.getRmStageFiles().clear();
        Utils.writeObject(Paths.
                get(rOOTPATH, ".project")
                .toFile(), project);
        Utils.writeObject(Paths.
                get(rOOTPATH, ".stage").
                toFile(), stagingArea);
    }

    public void reset1(String commitID,
                       StagingArea stagingArea, Project project) {
        for (String file : project.
                getCurrentBranch().getHead()
                .getCommitFiles().keySet()) {
            if (stagingArea.getStageFiles().
                    containsKey(file) || stagingArea
                    .getRmStageFiles().contains(file)) {
                continue;
            }
            if (new File(file).exists()) {
                byte[] workingfile = Utils.
                        readContents(new File(file));
                byte[] commitfile =
                        project.getCurrentBranch().getHead()
                                .getCommitFiles().get(file);
                if (!Arrays.equals(workingfile, commitfile)) {
                    System.out.println(
                            "There is an untracked file in the "
                                    + "way; delete it, or "
                                    + "add and commit it first.");
                    return;
                }
            } else {
                System.out.println(
                        "There is an untracked file in the "
                                + "way; delete it, "
                                + "or add and commit it first.");
                return;
            }
        }
    }

    public void reset2(Map<String, byte[]> files,
                       Project project) {
        for (String file : project.getCurrentBranch().
                getHead().getCommitFiles().keySet()) {
            new File(file).delete();
        }
        for (String file : files.keySet()) {
            byte[] data = files.get(file);
            Utils.writeContents(new File(file), data);
        }
    }


    void dfs1(Commit commit, List<String> result) {
        if (!result.contains(commit.getUid())) {
            result.add(commit.getUid());
            if (commit.getPrevious() != null) {
                dfs1(commit.getPrevious(), result);
            }
            if (commit.getPreviousMerge() != null) {
                dfs1(commit.getPreviousMerge(), result);
            }
        }
    }

    Object[] dfs2(Commit commit,
                  List<String> result, int depth) {
        if (result.contains(commit.getUid())) {
            return new Object[]{commit, depth};
        }
        Commit current = null;
        int mindepth = Integer.MAX_VALUE;
        if (commit.getPrevious() != null) {
            Object[] a = dfs2(commit.
                    getPrevious(), result, depth + 1);
            if (a != null) {
                Commit next = (Commit) a[0];
                Integer dep = (Integer) a[1];
                if (dep < mindepth) {
                    mindepth = dep;
                    current = next;
                }
            }
        }
        if (commit.getPreviousMerge() != null) {
            Object[] a = dfs2(commit.
                    getPreviousMerge(), result, depth + 1);
            if (a != null) {
                Commit next = (Commit) a[0];
                Integer dep = (Integer) a[1];
                if (dep < mindepth) {
                    mindepth = dep;
                    current = next;
                }
            }
        }
        if (current != null) {
            return new Object[]{current, mindepth};
        }
        return null;
    }

    public void merge1(String branchName,
                       Commit lcaCommit, Project project,
                       Branch mergeBranch) {
        Map<String, byte[]> lcaFiles = lcaCommit.getCommitFiles();
        Map<String, byte[]> curFiles = project.
                getCurrentBranch().getHead().getCommitFiles();
        Map<String, byte[]> mergeFiles = mergeBranch.
                getHead().getCommitFiles();
        Set<String> ff = new HashSet<>(lcaFiles.keySet());
        ff.addAll(curFiles.keySet());
        ff.addAll(mergeFiles.keySet());
        StagingArea sa = new StagingArea();
        boolean meetFlit = false;
        for (String fff : ff) {
            byte[] lcaFile = lcaFiles.get(fff);
            byte[] mergeFile = mergeFiles.get(fff);
            byte[] curFile = curFiles.get(fff);
            if (Objects.equals(lcaFile, mergeFile)) {
                continue;
            }
            if (Objects.equals(curFile, mergeFile)) {
                continue;
            }
            if (!Objects.equals(lcaFile, curFile)) {
                meetFlit = true;
                String conf = "<<<<<<< HEAD\n"
                        + (curFile == null ? "" : new String(curFile))
                        + "=======\n"
                        + (mergeFile == null ? "" : new String(mergeFile))
                        + ">>>>>>>\n";
                sa.addFile(fff, conf.getBytes());
            } else {
                if (mergeFile != null) {
                    sa.addFile(fff, mergeFile);
                } else {
                    sa.addRmFile(fff);
                }
            }
        }
        merge0(branchName, project, mergeBranch, sa, meetFlit);
    }

    public void merge(String branchName) {
        if (!checkInit()) {
            System.out.println("Not in "
                    + "an initialized Gitlet directory.");
            return;
        }
        Project project = Utils.readObject(Paths.
                get(rOOTPATH, ".project").
                toFile(), Project.class);
        StagingArea stagingArea = Utils.readObject(Paths.
                get(rOOTPATH, ".stage").
                toFile(), StagingArea.class);
        if (stagingArea.getStageFiles().size() > 0
                || stagingArea.getRmStageFiles().size() > 0) {
            System.out.println("You have uncommitted changes.");
            return;
        }
        if (project.getCurrentBranch().
                getBranchName().equals(branchName)) {
            System.out.println("Cannot merge a branch with itself.");
            return;
        }

        Branch mergeBranch = project.getBranch(branchName);
        if (mergeBranch == null) {
            System.out.println("A branch "
                    + "with that name does not exist.");
            return;
        }
        List<String> results = new ArrayList<>();
        dfs1(mergeBranch.getHead(), results);
        Object []commitPair = dfs2(project.
                        getCurrentBranch().getHead(),
                results, 0);
        if (commitPair == null) {
            System.out.println("Detached head, unexpected");
            return;
        }
        Commit lcaCommit = (Commit) commitPair[0];
        if (lcaCommit.getUid().equals(
                project.getCurrentBranch().getHead().getUid())) {
            checkoutBranch(branchName);
            System.out.println("Current branch fast-forwarded.");
            return;
        }
        if (lcaCommit.getUid().equals(mergeBranch.
                getHead().getUid())) {
            System.out.println(
                    "Given branch is an ancestor of the current branch.");
            return;
        }
        merge1(branchName, lcaCommit, project, mergeBranch);
    }

    public void merge0(String branchName, Project project, Branch mergeBranch,
                       StagingArea sa, Boolean meetFlit) {
        Commit preCommit = project.getCurrentBranch().getHead();
        Commit commit = new Commit();
        commit.setComment("Merged " + branchName + " into "
                + project.getCurrentBranch().getBranchName() + ".");
        commit.setTimestamp(new Date().getTime());
        commit.setPrevious(preCommit);
        commit.setPreviousMerge(mergeBranch.getHead());
        commit.setCommitFiles(new HashMap<>(project.
                getCurrentBranch().getHead().getCommitFiles()));

        for (String file : sa.getRmStageFiles()) {
            if (commit.getCommitFiles().containsKey(file)) {
                commit.getCommitFiles().remove(file);
            }
        }
        for (String file : sa.getStageFiles().keySet()) {
            commit.getCommitFiles().put(file, sa.getStageFiles().get(file));
        }

        commit.setUid(Utils.sha1(commit.
                getCommitFiles().toString(), preCommit.
                getUid(), commit.
                getComment(), new Date().toString()));
        project.getCurrentBranch().
                addCommit(commit);
        project.getCommits().put(commit.getUid(), commit);
        Utils.writeObject(Paths.
                get(rOOTPATH, ".project").toFile(), project);
        reset(commit.getUid());
        if (meetFlit) {
            System.out.println("Encountered a merge conflict.");
        }
    }

    public void addRemote(String name, String path) {
        if (remotes.containsKey(name)) {
            System.out.println("A "
                    + "remote with that name already exists.");
            return;
        }
        GitManager gitManager = new GitManager(path);
        remotes.put(name, gitManager);
    }

    public Project getProject() {
        Project project = Utils.readObject(Paths.
                get(rOOTPATH, ".project").
                toFile(), Project.class);
        return project;
    }

    public void dumpProject(Project project) {
        Utils.writeObject(Paths.
                get(rOOTPATH, ".project").toFile(), project);
    }

    public void rmRemote(String name) {
        if (!remotes.containsKey(name)) {
            System.out.
                    println("A remote with that name does not exist.");
            return;
        }
        remotes.remove(name);
    }

    public void pushRemote(String name, String branchName) {
        if (!remotes.containsKey(name)) {
            System.out.println("Remote directory not found.");
            return;
        }
        Project curPro = getProject();
        GitManager remoteManager = remotes.get(name);
        Project remotePro = remoteManager.getProject();
        Commit node = curPro.getCurrentBranch().getHead();
        for (Branch branch : remotePro.getBranchList()) {
            if (branch.getBranchName().equals(branchName)) {
                while (node.getPrevious() != null) {
                    if (remotePro.getCurrentBranch().
                            getHead().equals(node)) {
                        remotePro.getCurrentBranch().setHead(node);
                        remotePro.addBranch(curPro.
                                getCurrentBranch());
                        return;
                    }
                    node = node.getPrevious();
                }

                System.out.println("Please pull "
                        + "down remote changes before pushing.");
                return;
            }
        }

        remotePro.addBranch(curPro.getCurrentBranch());
        remotePro.dump();
    }

    public void fetchRemote(String name, String branchName) {
        if (!remotes.containsKey(name)) {
            System.out.println("Remote directory not found.");
            return;
        }
        Project curPro = getProject();
        GitManager remoteManager = remotes.get(name);
        Project remotePro = remoteManager.getProject();
        for (Branch branch : remotePro.getBranchList()) {
            if (branch.getBranchName().equals(branchName)) {
                Branch newBranch = new Branch(name + "/" + branchName);
                newBranch.setHead(branch.getHead());
                newBranch.setCommitList(branch.getCommitList());
                curPro.addBranch(newBranch);
                curPro.dump();
                return;
            }
        }
        System.out.println("That remote does not have that branch.");
    }

    public void pull(String name, String branchName) {
        if (!remotes.containsKey(name)) {
            System.out.println("Remote directory not found.");
            return;
        }
        fetchRemote(name, branchName);
        merge(name + "/" + branchName);
    }

    public Map<String, byte[]> readfiles(Set<String> input) {
        Map<String, byte[]> files = new HashMap<>();
        for (String name : input) {
            if (new File(name).exists()) {
                files.put(name, Utils.readContents(new File(name)));
            }
        }
        return files;
    }

    public void diff(String[] args) {
        Map<String, byte[]> bucketA = null, bucketB = null;
        if (args.length == 1) {
            bucketA = getProject().
                    getCurrentBranch().getHead().getCommitFiles();
            bucketB = readfiles(bucketA.keySet());
        } else if (args.length == 2) {
            Branch b1 = getProject().getBranch(args[1]);
            if (b1 == null) {
                System.out.println("A branch"
                        + " with that name does not exist.");
                return;
            }
            bucketA = b1.getHead().getCommitFiles();
            bucketB = readfiles(bucketA.keySet());
        } else if (args.length == 3) {
            Branch b1 = getProject().getBranch(args[1]);
            Branch b2 = getProject().getBranch(args[2]);
            if (b1 == null || b2 == null) {
                System.out.println("At "
                        + "least one branch does not exist.");
                return;
            }
            bucketA = b1.getHead().getCommitFiles();
            bucketB = b2.getHead().getCommitFiles();
        }

        Set<String> filesToCompare = new TreeSet<>();
        filesToCompare.addAll(bucketA.keySet());
        filesToCompare.addAll(bucketB.keySet());
        for (String fileCmp : filesToCompare) {
            String filenameA = "a/" + fileCmp,
                    filenameB = "b/" + fileCmp;
            String contentA, contentB;
            if (bucketA.get(fileCmp) == null) {
                contentA = "";
                filenameA = "/dev/null";
            } else {
                contentA = new String(bucketA.get(fileCmp));
            }
            if (bucketB.get(fileCmp) == null) {
                contentB = "";
                filenameB = "/dev/null";
            } else {
                contentB = new String(bucketB.get(fileCmp));
            }
            if (!contentA.equals(contentB)) {
                diffFiles(filenameA, filenameB, contentA, contentB);
            }
        }



    }

    private void diffFiles(String filenameA,
                           String filenameB, String contentA,
                           String contentB) {
        Diff diffManager = new Diff();
        diffManager.setSequences(
                Arrays.asList(contentA.split("\n")),
                Arrays.asList(contentB.split("\n")));
        int[] diffResult = diffManager.diffs();
        if (filenameA.equals("/dev/null")) {
            diffResult[0] = 0;
            diffResult[1] = 0;
        }
        if (filenameB.equals("/dev/null")) {
            diffResult[2] = 0;
            diffResult[3] = 0;
        }
        if (diffResult.length == 0) {
            return;
        }
        System.out.println(String.
                format("diff --git %s %s", filenameA, filenameB));
        System.out.println(String.format("--- %s", filenameA));
        System.out.println(String.format("+++ %s", filenameB));

        int len = diffResult.length / 4;
        for (int i = 0; i < len; ++i) {
            int n1 = diffResult[i * 4];
            int l1 = diffResult[i * 4 + 1];
            int n2 = diffResult[i * 4 + 2];
            int l2 = diffResult[i * 4 + 3];
            String out = "";
            out +=  "@@";
            out += " -" + (l1 == 0 ? n1 : (n1 + 1));
            if (l1 != 1) {
                out += "," + l1;
            }
            out += " +" + (l2 == 0 ? n2 : (n2 + 1));
            if (l2 != 1) {
                out += "," + l2;
            }
            out += " @@";
            System.out.println(out);
            for (int k = n1; k < n1 + l1; ++k) {
                System.out.println("-" + diffManager.get1(k));
            }
            for (int k = n2; k < n2 + l2; ++k) {
                System.out.println("+" + diffManager.get2(k));
            }
        }
    }
}
