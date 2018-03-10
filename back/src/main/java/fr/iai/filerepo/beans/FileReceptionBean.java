package fr.iai.filerepo.beans;

import java.io.InputStream;

public class FileReceptionBean {
    private String name;
    private InputStream content;

    public FileReceptionBean(String name, InputStream content) {
        this.name = name;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public InputStream getContent() {
        return content;
    }
}
