package fr.iai.filerepo.beans;

public class FileInfoBean {

    private String name;
    private long size;
    private String path;
    private FileType fileType;

    public FileInfoBean(String name, long size, String path, FileType fileType) {
        this.name = name;
        this.size = size;
        this.path = path;
        this.fileType = fileType;
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

    public FileType getFileType() {
        return fileType;
    }
}
