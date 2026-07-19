package net.tripleteamsstudio.unknownversionpt4.util;

import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChatLinesLoader {

    private static final Random RANDOM = new Random();

    private static final String[] DEFAULT_LINES = {
            "You are alone.",
            "Behind you.",
            "I'm still here.",
            "Don't turn around.",
            "They can see you.",
            "Nobody is coming.",
            "It's watching.",
            "Run."
    };

    private static Path getFilePath() {
        File gameDir = Minecraft.getInstance().gameDirectory;
        return new File(gameDir, "config/unknownversionpt4/chat_lines.txt").toPath();
    }

    private static void ensureFileExists() {
        Path path = getFilePath();
        if (Files.exists(path)) return;

        try {
            Files.createDirectories(path.getParent());
            List<String> defaults = new ArrayList<>();
            for (String line : DEFAULT_LINES) {
                defaults.add(line);
            }
            Files.write(path, defaults, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getRandomLine() {
        ensureFileExists();
        Path path = getFilePath();

        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            lines.removeIf(l -> l.trim().isEmpty());

            if (lines.isEmpty()) return DEFAULT_LINES[0];

            return lines.get(RANDOM.nextInt(lines.size()));
        } catch (IOException e) {
            e.printStackTrace();
            return DEFAULT_LINES[0];
        }
    }
}