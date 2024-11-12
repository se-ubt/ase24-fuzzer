import java.io.*;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Fuzzer {
    public static void main(String[] args) {
        //String commandToFuzz = "./program_unsafe";
        String commandToFuzz = "./program_coverage";
        ProcessBuilder builder = new ProcessBuilder();

        boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
        if (isWindows) {
            builder.command("cmd.exe", "/c", commandToFuzz);
        } else {
            builder.command("sh", "-c", commandToFuzz);
        }

        System.out.printf("Command: %s\n", builder.command());
        builder.directory(new File("./"));
        builder.redirectErrorStream(true);

        for (int i=0; i<10; i++) {
            try {
                runCommand(builder, fuzzInput(17, 'a', 26));
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void runCommand(ProcessBuilder builder, String input) throws IOException, InterruptedException {
        Process process = builder.start();

        System.out.printf("Input: %s\n", input);
        OutputStream os = process.getOutputStream();
        os.write(input.getBytes());
        os.flush();
        os.close();

        InputStream is = process.getInputStream();
        String output = readStreamIntoString(is);
        is.close();
        System.out.printf("Output: %s\n", output
                .replaceAll("warning: this program uses gets\\(\\), which is unsafe.", "")
                .trim()
        );

        int exitCode = process.waitFor();
        System.out.printf("Exit code: %s\n", exitCode);

        if (exitCode != 0) {
            System.out.println("Non-zero exit code detected!");
        }
    }

    private static String readStreamIntoString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        return reader.lines()
                .map(line -> line + System.lineSeparator())
                .collect(Collectors.joining());
    }

    private static String fuzzInput(int maxLength, char charStart, int charRange) {
        Random random = new Random();
        return IntStream.range(0, random.nextInt(maxLength + 1))
                .mapToObj(i -> (char) ((int) charStart + random.nextInt(charRange)))
                .map(String::valueOf)
                .collect(Collectors.joining());
    }
}
