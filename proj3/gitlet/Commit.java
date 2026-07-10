package gitlet;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Locale;


/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author Ree
 */
public class Commit implements Dumpable {
    /** jdsflkj. */
    private String comment;
    /** jdsflkj. */
    private String uid;
    /** jdsflkj. */
    private long timestamp;
    /** jdsflkj. */
    private Map<String, byte[]> commitFiles = new HashMap<>();
    /** jdsflkj. */
    private Commit previous;
    /** jdsflkj. */
    private Commit previousMerge;

    public String getComment() {
        return comment;
    }

    public void setComment(String comments) {
        this.comment = comments;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uids) {
        this.uid = uids;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamps) {
        this.timestamp = timestamps;
    }

    public Map<String, byte[]> getCommitFiles() {
        return commitFiles;
    }

    public void setCommitFiles(Map<String, byte[]> commitFileses) {
        this.commitFiles = commitFileses;
    }

    public Commit getPrevious() {
        return previous;
    }

    public void setPrevious(gitlet.Commit previouses) {
        this.previous = previouses;
    }

    @Override
    public void dump() {
        Date date = new Date(getTimestamp());
        System.out.println("===");
        System.out.println("commit " + getUid());
        Calendar calendar = Calendar.getInstance();
        DateTimeFormatter dtf = DateTimeFormatter.
                ofPattern("E MMM dd HH:mm:ss yyyy XXXX",
                Locale.ENGLISH);
        String dateStr = dtf.format(Instant.ofEpochSecond(date.getTime() / 1000)
                .atZone(calendar.getTimeZone().toZoneId()));
        System.out.println("Date: " + dateStr);
        System.out.println(getComment());
    }

    public Commit getPreviousMerge() {
        return previousMerge;
    }

    public void setPreviousMerge(Commit previousMerges) {
        this.previousMerge = previousMerges;
    }
}
