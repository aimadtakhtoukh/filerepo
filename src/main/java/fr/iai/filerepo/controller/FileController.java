package fr.iai.filerepo.controller;

import fr.iai.filerepo.beans.FileInfoBean;
import fr.iai.filerepo.beans.FileReceptionBean;
import fr.iai.filerepo.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/file/")
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    private final FileService fileService;

    @Value("#{${url.path}}")
    private Map<String, String> urlPaths;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("get")
    public List<FileInfoBean> getRootPath(UriComponentsBuilder b) throws IOException {
        String url = b.toUriString();
        return fileService.getFileNames("", url);
    }

    @GetMapping("get/**")
    public List<FileInfoBean> getPaths(HttpServletRequest request, UriComponentsBuilder b) throws IOException {
        String subpath = request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE)
                .toString().replaceFirst("/file/get", "");
        String url = b.toUriString();
        return fileService.getFileNames(subpath, url);
    }

    @GetMapping("download/**")
    public ResponseEntity<Resource> getFile(HttpServletRequest request) throws IOException {
        String subpath = request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE)
                .toString().replaceFirst("/file/download", "");
        File file = fileService.getFile(subpath);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("Accept", "*/*");
        headers.add("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new ByteArrayResource(Files.readAllBytes(file.toPath())));
    }

    @PostMapping("upload/**")
    public void uploadFile(@RequestParam("file") List<MultipartFile> files, HttpServletRequest request) throws IOException {
        String subpath = request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE)
                .toString().replaceFirst("/file/upload", "");
        fileService.createFolder(subpath);
        for (MultipartFile file : files) {
            FileReceptionBean fileReceptionBean = new FileReceptionBean(file.getOriginalFilename(), file.getBytes());
            fileService.saveFile(fileReceptionBean, subpath);
        }
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity handleIOException(IOException e) {
        logger.error("Erreur d'IO", e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Erreur d'IO");
    }

}
