package com.redlimerl.ghostrunner.util;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class TarGzUtil {
    public static boolean createTarGzipFolder(Path source, Path target) {

        if (!Files.isDirectory(source)) {
            return false;
        }

        try (OutputStream fOut = Files.newOutputStream(target);
             BufferedOutputStream buffOut = new BufferedOutputStream(fOut);
             GzipCompressorOutputStream gzOut = new GzipCompressorOutputStream(buffOut);
             TarArchiveOutputStream tOut = new TarArchiveOutputStream(gzOut)) {

            Files.walkFileTree(source, new SimpleFileVisitor<Path>() {

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) {

                    // only copy files, no symbolic links
                    if (attributes.isSymbolicLink()) {
                        return FileVisitResult.CONTINUE;
                    }

                    // get filename
                    Path targetFile = source.relativize(file);

                    try {
                        TarArchiveEntry tarEntry = new TarArchiveEntry(file.toFile(), targetFile.toString());
                        tOut.putArchiveEntry(tarEntry);
                        Files.copy(file, tOut);
                        tOut.closeArchiveEntry();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) {
                    return FileVisitResult.CONTINUE;
                }
            });

            tOut.finish();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean decompressTarGzipFile(Path source, Path target) {

        if (Files.notExists(source)) {
            return false;
        }

        try (InputStream fi = Files.newInputStream(source);
             BufferedInputStream bi = new BufferedInputStream(fi);
             GzipCompressorInputStream gzi = new GzipCompressorInputStream(bi);
             TarArchiveInputStream ti = new TarArchiveInputStream(gzi)) {

            ArchiveEntry entry;
            while ((entry = ti.getNextEntry()) != null) {

                Path newPath = zipSlipProtect(entry, target);

                if (entry.isDirectory()) {
                    Files.createDirectories(newPath);
                } else {

                    // check parent folder again
                    Path parent = newPath.getParent();
                    if (parent != null) {
                        if (Files.notExists(parent)) {
                            Files.createDirectories(parent);
                        }
                    }

                    // copy TarArchiveInputStream to Path newPath
                    Files.copy(ti, newPath, StandardCopyOption.REPLACE_EXISTING);

                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static Path zipSlipProtect(ArchiveEntry entry, Path targetDir) throws IOException {
        Path targetDirResolved = targetDir.resolve(entry.getName());
        return targetDirResolved.normalize();
    }
}
