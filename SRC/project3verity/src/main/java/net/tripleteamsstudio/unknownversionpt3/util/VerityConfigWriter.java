package net.tripleteamsstudio.unknownversionpt3.util;

import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VerityConfigWriter {

    private static final Pattern API_KEY_LINE =
            Pattern.compile("^(\\s*apiKey\\s*=\\s*\")([^\"]*)(\"\\s*)$");

    private static Path getConfigPath() {
        File gameDir = Minecraft.getInstance().gameDirectory;
        return new File(gameDir, "config/verity-common.toml").toPath();
    }

    // Returns true if the file doesn't exist or apiKey is empty (menu should show)
    public static boolean isApiKeyMissing() {
        Path path = getConfigPath();
        if (!Files.exists(path)) {
            return true;
        }
        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            for (String line : lines) {
                Matcher m = API_KEY_LINE.matcher(line);
                if (m.matches()) {
                    return m.group(2).trim().isEmpty();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    // Writes the apiKey into the config file. Returns true on success.
    public static boolean writeApiKey(String apiKey) {
        Path path = getConfigPath();
        if (!Files.exists(path)) {
            System.out.println("[UnknownVersionPT3] Config file not found: " + path);
            return false;
        }

        String escaped = apiKey.replace("\\", "\\\\").replace("\"", "\\\"");

        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            boolean found = false;

            for (int i = 0; i < lines.size(); i++) {
                Matcher m = API_KEY_LINE.matcher(lines.get(i));
                if (m.matches()) {
                    lines.set(i, m.group(1) + escaped + m.group(3));
                    found = true;
                    break;
                }
            }

            if (!found) {
                System.out.println("[UnknownVersionPT3] apiKey line not found in config!");
                return false;
            }

            Files.write(path, lines, StandardCharsets.UTF_8);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static String getApiKey() {
        Path path = getConfigPath();
        if (!Files.exists(path)) return "";
        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            for (String line : lines) {
                Matcher m = API_KEY_LINE.matcher(line);
                if (m.matches()) {
                    return m.group(2);
                 }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    return "";
}

}