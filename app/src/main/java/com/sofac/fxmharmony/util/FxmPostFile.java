package com.sofac.fxmharmony.util;

import com.sofac.fxmharmony.dto.PostDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class FxmPostFile {

    private List<String> videoList;
    private List<String> imageList;
    private List<String> fileList;
    private PostDTO fxmPost;

    public FxmPostFile(PostDTO fxmPost) {
        this.fxmPost = fxmPost;
//        this.videoList = parserPathFile(fxmPost.getLinksVideo());
//        this.imageList = parserPathFile(fxmPost.getLinksImage());
//        this.fileList = parserPathFile(fxmPost.getLinksFile());

    }

    public List<String> getVideoList() {
        return videoList;
    }

    public List<String> getImageList() {
        return imageList;
    }

    public List<String> getFileList() {
        return fileList;
    }

    public PostDTO getFxmPost() {
        return fxmPost;
    }

    private List<String> parserPathFile(String path) {
        if (path != null && !"".equals(path)) {
            String[] pathArr = path.split(";#");
            List<String> pathList = new ArrayList<>(Arrays.asList(pathArr));
            return pathList;
        }
        return new ArrayList<>();
    }
}
