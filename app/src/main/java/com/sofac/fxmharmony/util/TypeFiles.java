package com.sofac.fxmharmony.util;

import android.webkit.MimeTypeMap;
import java.util.ArrayList;

public class TypeFiles {

    private String[] files;

    public TypeFiles(String[] allFiles) {
        this.files = allFiles;
    }

    public ArrayList<String> getImages() {
        ArrayList<String> arrayListImages = new ArrayList<>();
        for (String img : files) {
            if (img.length()>4 &&getMimeType(img).contains("image/"))
                arrayListImages.add(img);
        }
        return arrayListImages;
    }

    public ArrayList<String> getMovies() {
        ArrayList<String> arrayListMovies = new ArrayList<>();
        for (String mov : files) {
            if (mov.length()>4 &&getMimeType(mov).contains("video/"))
                arrayListMovies.add(mov);
        }
        return arrayListMovies;
    }

    public ArrayList<String> getDocs() {
        ArrayList<String> arrayListDocs = new ArrayList<>();
        for (String doc : files) {
            if (doc.length()>4 &&!getMimeType(doc).contains("image/") && !getMimeType(doc).contains("video/"))
                arrayListDocs.add(doc);
        }
        return arrayListDocs;
    }

    private String getMimeType(String url) {
        String type = "null";
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        if (type == null) return "";
        else return type;
    }

}
