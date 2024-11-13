import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Fuzzer {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java Fuzzer.java \"<command_to_fuzz>\"");
        }
        String commandToFuzz = args[0];
        String workingDirectory = "./";

        if (!Files.exists(Paths.get(workingDirectory, commandToFuzz))) {
            throw new RuntimeException("Could not find command '%s'.".formatted(commandToFuzz));
        }

        ProcessBuilder builder = getProcessBuilderForCommand(commandToFuzz, workingDirectory);
        System.out.printf("Command: %s\n", builder.command());

        IntStream.range(0, 10).forEach(i -> {
            try {
                runCommand(builder, getFuzzInput(17, 'a', 26));
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static ProcessBuilder getProcessBuilderForCommand(String command, String workingDirectory) {
        ProcessBuilder builder = new ProcessBuilder();
        boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
        if (isWindows) {
            builder.command("cmd.exe", "/c", command);
        } else {
            builder.command("sh", "-c", command);
        }
        builder.directory(new File(workingDirectory));
        builder.redirectErrorStream(true); // redirect stderr to stdout
        return builder;
    }

    private static void runCommand(ProcessBuilder builder, String input) throws IOException, InterruptedException {
        Process process = builder.start();

        System.out.printf("Input: %s\n", input);
        OutputStream streamToCommand = process.getOutputStream();
        streamToCommand.write(input.getBytes());
        streamToCommand.flush();
        streamToCommand.close();

        int exitCode = process.waitFor();
        System.out.printf("Exit code: %s\n", exitCode);

        InputStream streamFromCommand = process.getInputStream();
        String output = readStreamIntoString(streamFromCommand);
        streamFromCommand.close();
        System.out.printf("Output: %s\n", output
                // ignore warnings due to usage of gets() in test program
                .replaceAll("warning: this program uses gets\\(\\), which is unsafe.", "")
                .trim()
        );

        if (exitCode != 0) {
            System.out.println("Non-zero exit code detected!");
        }
    }

    private static String readStreamIntoString(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        return reader.lines()
                .map(line -> line + System.lineSeparator())
                .collect(Collectors.joining());
    }

    private static String getFuzzInput(int maxLength, char charStart, int charRange) {
        Random random = new Random();
        int length = random.nextInt(maxLength) + 1; // length in [1, maxLength]
        // start with sequence of integers representing random characters in the configured range
        return random.ints(length, charStart, charStart + charRange)
                .mapToObj(Character::toChars) // int to char
                .map(String::valueOf) // char to string
                .collect(Collectors.joining());
    }
}
