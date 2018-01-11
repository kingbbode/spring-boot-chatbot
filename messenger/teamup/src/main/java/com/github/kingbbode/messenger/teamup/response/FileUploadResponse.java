package com.github.kingbbode.messenger.teamup.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by YG on 2017-05-17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FileUploadResponse {

    public FileUploadResponse() {
    }

    public FileUploadResponse(List<File> files) {
        this.files = files;
    }

    private List<File> files;

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class File {
        public File() {
        }

        public File(String name, String id) {
            this.name = name;
            this.id = id;
        }

        private String name;
        private String id;

        public String getName() {
            return name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
