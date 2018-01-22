package fr.iai.filerepo.beans;

public class FileInfoBean {
    private String name;
    private long size;
    private String path;

    public FileInfoBean(String name, long size, String path) {
        this.name = name;
        this.size = size;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public long getSize() {
        return size;
    }

    public String getPath() {
        return path;
    }
}
