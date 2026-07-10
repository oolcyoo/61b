package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.*;

import ucb.util.CommandArgs;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author
 */
public final class  Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            CommandArgs options =
                    new CommandArgs("--verbose --=(.*){1,3}", args);
            if (!options.ok()) {
                throw error("Usage: java enigma.Main [--verbose] "
                        + "[INPUT [OUTPUT]]");
            }

            _verbose = options.contains("--verbose");
            new Main(options.get("--")).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Open the necessary files for non-option arguments ARGS (see comment
     *  on main). */
    Main(List<String> args) {
        _config = getInput(args.get(0));

        if (args.size() > 1) {
            _input = getInput(args.get(1));
        } else {
            _input = new Scanner(System.in);
        }

        if (args.size() > 2) {
            _output = getOutput(args.get(2));
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        Machine M = readConfig();
        while (_input.hasNextLine()) {
            String line = _input.nextLine();
            if (!Objects.equals(line, "")) {
                if (line.charAt(0) == '*') {
                    setUp(M, line);
                } else {
                    printMessageLine(M.convert(line));
                }
            } else {
                System.out.println();
            }
        }
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            _alphabet = new Alphabet(_config.next());
            int numRotors = _config.nextInt();
            int pawls = _config.nextInt();

            Collection<Rotor> allRotors = new ArrayList<>();

            while (_config.hasNext()) {
                allRotors.add(readRotor());
            }

            return new Machine(_alphabet, numRotors, pawls, allRotors);

        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            String readName = _config.next();
            String readRotors = _config.next();
            String readCycles = "";
            String readNotches = "";

            while (_config.hasNext("(\\(.+\\)\\s?)+")) {
                readCycles += _config.next("(\\(.+\\)\\s?)+");
            }

            Permutation readPerm = new Permutation(readCycles, _alphabet);

            for (int i = 1; i < readRotors.length(); i++) {
                readNotches += readRotors.charAt(i);
            }

            if (readRotors.charAt(0) == 'N') {
                return new FixedRotor(readName, readPerm);
            } else if (readRotors.charAt(0) == 'R') {
                return new Reflector(readName, readPerm);
            } else {
                return new MovingRotor(readName, readPerm, readNotches);
            }

        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {

        String[] rotors = new String[M.numRotors() + 1];
        Scanner scanRotors = new Scanner(settings);
        String cycle = "";
        String setting = "";
        int count = 0;
        int numR = M.numRotors() + 1;

        while (numR != 0) {
            rotors[count] = scanRotors.next();
            count += 1;
            numR -= 1;
        }
        if (!scanRotors.hasNext()) {
            throw error("Wrong anyway");
        }

        setting = scanRotors.next();
        String[] realSetting = Arrays.copyOfRange(rotors, 1, rotors.length);
        while (scanRotors.hasNext()) {
            cycle += scanRotors.next();
        }
        if (setting == null) {
            throw error("Wrong config");
        }

        Permutation plug = new Permutation(cycle, _alphabet);
        M.emptyRotors(new Rotor[M.numRotors()]);
        M.insertRotors(realSetting);
        M.setPlugboard(plug);
        M.setRotors(setting);

    }


    /** Return true iff verbose option specified. */
    static boolean verbose() {
        return _verbose;
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        for (int i = 0; i < msg.length(); i++) {
            System.out.print(msg.charAt(i));
            if ((i + 1) % 5 == 0) {
                System.out.print(" ");
            }
        }
        System.out.println();
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;

    /** True if --verbose specified. */
    private static boolean _verbose;
}
