package gitlet;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author Ree
 */

public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */
    public static void main(String... args) {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        GitManager gitManager = new GitManager();
        switch (args[0]) {
        case "init":
            gitManager.init();
            break;
        case "add":
            yiyang(gitManager, args);
            gitManager.add(args[1]);
            break;
        case "rm":
            yiyang(gitManager, args);
            gitManager.remove(args[1]);
            break;
        case "commit":
            reduce(gitManager, args);
            break;
        case "checkout":
            reduceLine(gitManager, args);
            break;
        case "log":
            gitManager.log();
            break;
        case "global-log":
            gitManager.globalLog();
            break;
        case "find":
            hahah(gitManager, args);
            break;
        case "status":
            gitManager.status();
            break;
        case "branch":
            yiyang(gitManager, args);
            gitManager.branch(args[1]);
            break;
        case "rm-branch":
            yiyang(gitManager, args);
            gitManager.rmBranch(args[1]);
            break;
        case "reset":
            yiyang(gitManager, args);
            gitManager.reset(args[1]);
            break;
        case "merge":
            yiyang(gitManager, args);
            gitManager.merge(args[1]);
            break;
        case "diff":
            buyiyang(gitManager, args);
            gitManager.diff(args);
            break;
        default:
            System.out.println("No command with that name exists.");
        }
    }

    public static void hahah(GitManager gitManager, String... args) {
        if (args.length < 2) {
            System.out.println("Incorrect operands.");
            return;
        }
        String msg2 = args[1].replaceAll("\"", "");
        gitManager.find(msg2);
    }

    public static void reduceLine(GitManager gitManager, String... args) {
        if (args.length == 2) {
            gitManager.checkoutBranch(args[1]);
        } else if (args.length == 3 && args[1].equals("--")) {
            gitManager.checkout(args[2]);
        } else if (args.length == 4 && args[2].equals("--")) {
            gitManager.checkout(args[1], args[3]);
        } else {
            System.out.println("Incorrect operands.");
            return;
        }
    }
    public static void reduce(GitManager gitManager, String... args) {
        if (args.length < 2) {
            System.out.println("Please enter a commit message.");
            return;
        }
        String msg = args[1].replaceAll("\"", "");
        gitManager.commit(msg);
    }
    public static void yiyang(GitManager gitManager, String... args) {
        if (args.length < 2) {
            System.out.println("Incorrect operands.");
            return;
        }
    }
    public static void buyiyang(GitManager gitManager, String... args) {
        if (args.length >= 4) {
            System.out.println("Incorrect operands.");
            return;
        }
    }
}
