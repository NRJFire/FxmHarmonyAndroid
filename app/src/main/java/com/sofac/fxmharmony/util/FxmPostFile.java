package com.sofac.fxmharmony.util;

import android.widget.LinearLayout;

import com.sofac.fxmharmony.data.dto.PostDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.sofac.fxmharmony.Constants.BASE_URL;
import static com.sofac.fxmharmony.Constants.GET_POST_FILES_END_URL;


public class FxmPostFile {

    private List<String> videoList;
    private List<String> imageList;
    private List<String> fileList;
    private PostDTO fxmPost;

    public FxmPostFile(PostDTO fxmPost) {
        this.fxmPost = fxmPost;
        this.videoList = parserPathFile(fxmPost.getLinksVideo());
        this.imageList = parserPathFile(fxmPost.getLinksImage());
        this.fileList = parserPathFile(fxmPost.getLinksFile());

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
