package org.example.util;

public final class FileUploadUtils {

    private FileUploadUtils() {
    }

    public static boolean isImage(String contentType) {
        return contentType != null && contentType.startsWith("image/");
    }

    public static String extractExtension(String fileName) {
        if (fileName == null) {
            return "";
        }

        int extensionIndex = fileName.lastIndexOf('.');
        if (extensionIndex < 0) {
            return "";
        }
        return fileName.substring(extensionIndex);
    }
}
