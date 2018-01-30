package fr.iai.filerepo.beans;

public class FileReceptionBean {
    private String name;
    private byte[] content;

    public FileReceptionBean(String name, byte[] content) {
        this.name = name;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public byte[] getContent() {
        return content;
    }
}
