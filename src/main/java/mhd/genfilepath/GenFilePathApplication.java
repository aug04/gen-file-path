package mhd.genfilepath;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GenFilePathApplication {

    public static void main(String[] args) {
        final String WINDOWS = "windows";
        final String LINUX = "linux";

        String url = "";
        String filename = "FilesChanged.txt";
        String dir = Paths.get("").toAbsolutePath().toString();
        String type = null;
        boolean absolutePath = false;
        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                final String flag = args[i];
                final String next = i == args.length - 1 ? null : args[i + 1];
                final boolean canAssign = next != null && !next.isEmpty();
                if ("--target".equals(flag) && canAssign) {
                    url = next;
                } else if (("--filename".equals(flag) || "-f".equals(flag)) && canAssign) {
                    filename = next;
                } else if (("--dir".equals(flag) || "-d".equals(flag)) && canAssign) {
                    dir = next;
                } else if (("--type".equals(flag) || "-t".equals(flag))
                        && (WINDOWS.equals(next) || LINUX.equals(next))) {
                    type = next;
                } else if ("--absolutePath".equals(flag)) {
                    absolutePath = true;
                }
            }
        }

        Path path = Paths.get(url);
        try (Stream<Path> stream = Files.walk(path)) {
            List<String> paths = stream.filter(p -> p.toFile().isFile())
                    .map(p2 -> p2.toAbsolutePath().toString()).collect(Collectors.toList());

            final String destinationFilePath = dir + File.separator + filename;
            BufferedWriter bw = new BufferedWriter(new FileWriter(destinationFilePath, false));
            bw.write("----------------------------------------");
            bw.newLine();
            bw.write("Total files: " + paths.size());
            bw.newLine();
            bw.write("----------------------------------------");
            bw.newLine();

            final boolean separatorReplace = ("\\".equals(File.separator) && LINUX.equals(type) || ("/".equals(File.separator)) && WINDOWS.equals(type));
            String replaceWith = "";
            for (int i = 0; i < paths.size(); i++) {
                if (separatorReplace && replaceWith.isEmpty()) {
                    replaceWith = WINDOWS.equals(type) ? "\\" : "/";
                }
                String s = paths.get(i);
                if (!absolutePath) {
                    s = s.replace(!url.isEmpty() ? url : dir, "");
                }
                bw.write(!separatorReplace ? s : s.replace(File.separator, replaceWith));
                if (i < paths.size() - 1) {
                    bw.newLine();
                }
            }

            bw.flush();
            bw.close();
            System.out.println("Done => " + destinationFilePath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
