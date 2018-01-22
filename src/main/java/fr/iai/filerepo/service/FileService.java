package fr.iai.filerepo.service;

import fr.iai.filerepo.beans.FileInfoBean;
import fr.iai.filerepo.beans.FileReceptionBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileService {

    @Value("${filePath}")
    private String fileRepositoryPath;
    @Value("${url.path.directory}")
    private String directoryPath;
    @Value("${url.path.file}")
    private String filePath;

    private Logger logger = LoggerFactory.getLogger(FileService.class);

    public void saveFile(FileReceptionBean file, String subpath) throws IOException{
        Path newFilePath = Paths.get(fileRepositoryPath, subpath, file.getName());
        Files.write(newFilePath, file.getContent());
        logger.info("Saved file {} in folder {}, size {}B",
                file.getName(), newFilePath.toAbsolutePath(), file.getContent().length);
    }

    public void createFolder(String subpath) throws IOException {
        Path newFolder = Paths.get(fileRepositoryPath, subpath);
        Files.createDirectories(newFolder);
        logger.info("New folder {} created", newFolder.toAbsolutePath());
    }

    public List<FileInfoBean> getFileNames(String subpath, String url) throws IOException {
        Path mainFolderPath = Paths.get(fileRepositoryPath);
        Path savedFilesPath = Paths.get(fileRepositoryPath, subpath);
        return Files.list(savedFilesPath)
                .map(p -> new FileInfoBean(
                        p.getFileName().toString(),
                        p.toFile().length(),
                        url +
                                (p.toFile().isDirectory() ? directoryPath : filePath) +
                                p.subpath(mainFolderPath.getNameCount(), p.getNameCount())
                                .toString()
                                .replace("\\", "/")
                ))
                .collect(Collectors.toList());
    }

    public File getFile(String subpath) {
        return Paths.get(fileRepositoryPath, subpath).toFile();
    }
}
