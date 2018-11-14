package com.pince.renovace2.request;

import com.pince.renovace2.config.Config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * @author athou
 * @date 2017/12/13
 */
public class UploadRequestBuidler extends RequestBuilder<UploadRequestBuidler> {
    private List<FileInput> files = new ArrayList<>();

    public UploadRequestBuidler(Class<? extends Config> clientConfig) {
        super(Method.UploadFile, clientConfig);
    }

    public UploadRequestBuidler files(String des, Map<String, File> files) {
        for (String filename : files.keySet()) {
            this.files.add(new FileInput(des, filename, files.get(filename)));
        }
        return this;
    }

    public UploadRequestBuidler addFile(String des, String filename, File file) {
        files.add(new FileInput(des, filename, file));
        return this;
    }

    public boolean isMultiFile() {
        return files.size() > 1;
    }

    public RequestBody getDescription() {
        if (files.isEmpty()) {
            throw new IllegalArgumentException("you cannot upload an empty file");
        }
        return RequestBody.create(MultipartBody.FORM, files.get(0).des);
    }

    public MultipartBody.Part getPart() {
        if (files.isEmpty()) {
            throw new IllegalArgumentException("you cannot upload an empty file");
        }
        return getPart(files.get(0));
    }

    private MultipartBody.Part getPart(FileInput fileInput) {
        RequestBody requestFile = RequestBody.create(MultipartBody.FORM, fileInput.file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", fileInput.filename, requestFile);
        return body;
    }

    public List<MultipartBody.Part> getParts() {
        List<MultipartBody.Part> parts = new ArrayList<>(files.size());
        for (FileInput fileInput : files) {
            parts.add(getPart(fileInput));
        }
        return parts;
    }

    public static class FileInput {
        public String des;
        public String filename;
        public File file;

        public FileInput(String des, String filename, File file) {
            this.des = des;
            this.filename = filename;
            this.file = file;
        }

        @Override
        public String toString() {
            return "FileInput{" +
                    "key='" + des + '\'' +
                    ", filename='" + filename + '\'' +
                    ", file=" + file +
                    '}';
        }
    }
}
