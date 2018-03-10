package fr.iai.filerepo.controller;

import fr.iai.filerepo.beans.FileInfoBean;
import fr.iai.filerepo.beans.FileReceptionBean;
import fr.iai.filerepo.database.UserRepository;
import fr.iai.filerepo.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/file/")
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    private final FileService fileService;
    private final UserRepository userRepository;

    @Value("#{${urlPath}}")
    private Map<String, String> urlPaths;

    @Autowired
    public FileController(FileService fileService, UserRepository userRepository) {
        this.fileService = fileService;
        this.userRepository = userRepository;
    }

    @GetMapping("get")
    @PreAuthorize("hasAnyAuthority('ADMIN_USER', 'STANDARD_USER')")
    public List<FileInfoBean> getRootPath(UriComponentsBuilder b, Authentication a) throws IOException {
        String url = b.toUriString();
        return fileService.getFileNames("", url, userRepository.findByUsername(a.getName()));
    }

    @GetMapping("get/**")
    @PreAuthorize("hasAnyAuthority('ADMIN_USER', 'STANDARD_USER')")
    public List<FileInfoBean> getPaths(HttpServletRequest request, UriComponentsBuilder b, Authentication a) throws IOException {
        String subpath = request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE)
                .toString().replaceFirst("/file/get", "");
        String url = b.toUriString();
        return fileService.getFileNames(subpath, url, userRepository.findByUsername(a.getName()));
    }

    @GetMapping("download/**")
    @PreAuthorize("hasAnyAuthority('ADMIN_USER', 'STANDARD_USER')")
    public ResponseEntity<Resource> getFile(HttpServletRequest request, Authentication a) throws IOException {
        String subpath = request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE)
                .toString().replaceFirst("/file/download", "");
        File file = fileService.getFile(subpath, userRepository.findByUsername(a.getName()));
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
                .body(new InputStreamResource(new FileInputStream(file)));
    }

    @PostMapping("upload/**")
    @PreAuthorize("hasAnyAuthority('ADMIN_USER', 'STANDARD_USER')")
    public void uploadFile(@RequestParam("file") List<MultipartFile> files, HttpServletRequest request,
                           Authentication a) throws IOException {
        String subpath = request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE)
                .toString().replaceFirst("/file/upload", "");
        fileService.createFolder(subpath, userRepository.findByUsername(a.getName()));
        for (MultipartFile file : files) {
            FileReceptionBean fileReceptionBean = new FileReceptionBean(file.getOriginalFilename(), file.getInputStream());
            fileService.saveFile(fileReceptionBean, subpath, userRepository.findByUsername(a.getName()));
        }
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity handleIOException(IOException e) {
        logger.error("Erreur d'IO", e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("{\"error\" : \"" + e.getMessage() + "\"}");
    }

}
