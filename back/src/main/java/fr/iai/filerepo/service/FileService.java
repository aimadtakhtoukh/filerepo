package fr.iai.filerepo.service;

import fr.iai.filerepo.beans.FileInfoBean;
import fr.iai.filerepo.beans.FileReceptionBean;
import fr.iai.filerepo.beans.FileType;
import fr.iai.filerepo.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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

    public void saveFile(FileReceptionBean file, String subpath, User user) throws IOException{
        Path newFilePath = Paths.get(fileRepositoryPath, subpath, file.getName());
        Files.copy(file.getContent(), newFilePath, StandardCopyOption.REPLACE_EXISTING);
        logger.info("{} saved file {} in folder {}, size {}B",
                user.getFullName(),
                file.getName(), newFilePath.toAbsolutePath(), newFilePath.toFile().length());
    }

    public void createFolder(String subpath, User user) throws IOException {
        Path newFolder = Paths.get(fileRepositoryPath, subpath);
        Files.createDirectories(newFolder);
        logger.info("New folder {} created by {}", newFolder.toAbsolutePath(), user.getFullName());
    }

    public List<FileInfoBean> getFileNames(String subpath, String url, User user) throws IOException {
        logger.info("{} gets file list in {}", user.getFullName(), subpath);
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
                                .replace("\\", "/"),
                        (p.toFile().isDirectory() ? FileType.FOLDER : FileType.FILE)))
                .collect(Collectors.toList());
    }

    public File getFile(String subpath, User user) {
        logger.info("{} downloads {}", user.getFullName(), subpath);
        return Paths.get(fileRepositoryPath, subpath).toFile();
    }
}
