package br.com.moreira.desafiopetize.domain.services;

import br.com.moreira.desafiopetize.exceptions.ErrorStorageFileException;
import br.com.moreira.desafiopetize.exceptions.IncorrectFileNameException;
import org.apache.tomcat.util.http.fileupload.InvalidFileNameException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    public FileStorageService(@Value("${file.upload-dir}") String uploadDir) {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception e) {
            throw new RuntimeException("It was not possible to create the directory to storage the files.", e);
        }
    }

    public String storeFile(MultipartFile file) {
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String uniqueFilename = UUID.randomUUID().toString() + "_" + originalFilename;

        try {
            if (uniqueFilename.contains("..")) {
                throw new IncorrectFileNameException("Sorry! The filename contains invalid path sequence " + uniqueFilename);
            }

            Path targetLocation = this.fileStorageLocation.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return uniqueFilename;
        } catch (IOException e) {
            throw new ErrorStorageFileException("It was not possible to storage the file " + uniqueFilename + ". Please, Try again!");
        }
    }
}
