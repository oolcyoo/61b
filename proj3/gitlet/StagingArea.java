package gitlet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author Ree
 */
public class StagingArea implements Dumpable {


    /** jdsflkj. */
    private Map<String, byte[]> stageFiles = new HashMap<>();
    /** jdsflkj. */
    private Set<String> rmStageFiles = new HashSet<>();

    public Map<String, byte[]> getStageFiles() {
        return stageFiles;
    }

    public void setStageFiles(Map<String, byte[]> stageFileses) {
        this.stageFiles = stageFileses;
    }

    public void removeFile(String fileName) {
        if (stageFiles.containsKey(fileName)) {
            stageFiles.remove(fileName);
        }
    }

    public void addFile(String fileName, byte[] data) {
        stageFiles.put(fileName, data);
    }

    public void addRmFile(String fileName) {
        rmStageFiles.add(fileName);
    }

    public Set<String> getRmStageFiles() {
        return rmStageFiles;
    }

    public void setRmStageFiles(Set<String> rmStageFileses) {
        this.rmStageFiles = rmStageFileses;
    }

    @Override
    public void dump() {
        for (String file : stageFiles.keySet()) {
            System.out.println("staging " + file);
        }
    }
}

