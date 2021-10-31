import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IncrementVersion {

    private static final String[] VERSIONS = { "major", "minor", "patch", "build" };

    public static void main(String[] args) {
        String input;
        if (args.length == 1) {
            input = args[0];
        } else {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Version (" + String.join(".", VERSIONS) + ") [build]: ");
            input = scanner.nextLine();
        }
        int index = 0;
        boolean valid = false;
        for (String s : VERSIONS) {
            if (s.equals(input)) {
                valid = true;
                break;
            }
            ++index;
        }
        if (!valid) {
            throw new IllegalArgumentException("Unknown version " + input);
        }
        System.out.println("\nIncreasing " + input + " version...");
        File file = new File("./build.gradle");
        List<String> list = new ArrayList<>(120);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String[] lines = list.toArray(new String[0]);
        Pattern versionPattern = Pattern.compile("([0-9]+\\.){3}[0-9]+");
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            Matcher matcher = versionPattern.matcher(line);
            if (matcher.find()) {
                String version = matcher.group();
                String[] versions = version.split("\\.");
                if (versions.length < index) {
                    String[] updatedVersions = new String[index];
                    Arrays.fill(updatedVersions, "0");
                    System.arraycopy(versions, 0, updatedVersions, 0, versions.length);
                    versions = updatedVersions;
                }
                String value = versions[index];
                int versionValue;
                try {
                    versionValue = Integer.parseInt(value);
                } catch (NumberFormatException nfe) {
                    throw new RuntimeException(nfe);
                }
                versions[index] = String.valueOf(++versionValue);
                String versionStr = String.join(".", versions);
                lines[i] = line.replaceAll("([0-9]+\\.){3}[0-9]+", versionStr);
                break;
            }
        }
        try (FileWriter writer = new FileWriter(file)) {
            for (String line : lines) {
                writer.write(line + "\n");
            }
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
}
