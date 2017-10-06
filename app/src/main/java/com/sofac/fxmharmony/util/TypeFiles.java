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

    //mp4 3gp
    //if (mov.length()>4 &&getMimeType(mov).contains("video/"))
    public ArrayList<String> getMovies() {
        ArrayList<String> arrayListMovies = new ArrayList<>();
        for (String mov : files) {
            if (mov.length()>4 &&(getMimeType(mov).contains("mp4")||getMimeType(mov).contains("3gp")))
                arrayListMovies.add(mov);
        }
        return arrayListMovies;
    }

    public ArrayList<String> getDocs() {
        ArrayList<String> arrayListDocs = new ArrayList<>();
        for (String doc : files) {
            if (doc.length()>4 &&!getMimeType(doc).contains("image/") && !(getMimeType(doc).contains("mp4")||getMimeType(doc).contains("3gp")))
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
