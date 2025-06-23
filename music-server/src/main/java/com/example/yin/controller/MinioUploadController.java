package com.example.yin.controller;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

@Service
public class MinioUploadController {

    private static MinioClient minioClient;
    private static String bucketName;
    private static String mvBucketName;
    public static void init() {
        Properties properties = new Properties();
        try {
            // 使用类加载器获取资源文件的输入流
            InputStream inputStream = MinioUploadController.class.getClassLoader().getResourceAsStream("application-dev.properties");
            if (inputStream != null) {
                properties.load(inputStream);
                String minioEndpoint = properties.getProperty("minio.endpoint");
                String minioAccessKey = properties.getProperty("minio.access-key");
                String minioSecretKey = properties.getProperty("minio.secret-key");
                String minioBucketName = properties.getProperty("minio.bucket-name");
                String minioMvBucketName = properties.getProperty("minio.mv-bucket");

                mvBucketName = minioMvBucketName;
                bucketName = minioBucketName;
                minioClient = MinioClient.builder()
                        .endpoint(minioEndpoint)
                        .credentials(minioAccessKey, minioSecretKey)
                        .build();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static String uploadFile(MultipartFile file) {
        try {
            init();
            InputStream inputStream = file.getInputStream();
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(file.getOriginalFilename())
                            .stream(inputStream, inputStream.available(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
            return "File uploaded successfully!";
        } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            return "Error uploading file to MinIO: " + e.getMessage();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static String uploadImgFile(MultipartFile file) {
        try {
            init();
            InputStream inputStream = file.getInputStream();
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object("/singer/img/" + file.getOriginalFilename())
                            .stream(inputStream, inputStream.available(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
            return "File uploaded successfully!";
        } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            return "Error uploading file to MinIO: " + e.getMessage();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static String uploadSonglistImgFile(MultipartFile file) {
        try {
            init();
            InputStream inputStream = file.getInputStream();
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object("/songlist/" + file.getOriginalFilename())
                            .stream(inputStream, inputStream.available(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
            return "File uploaded successfully!";
        } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            return "Error uploading file to MinIO: " + e.getMessage();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static String uploadSongImgFile(MultipartFile file) {
        try {
            init();
            InputStream inputStream = file.getInputStream();
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object("/singer/song/" + file.getOriginalFilename())
                            .stream(inputStream, inputStream.available(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
            return "File uploaded successfully!";
        } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            return "Error uploading file to MinIO: " + e.getMessage();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String uploadAtorImgFile(MultipartFile file) {
        try {
            init();
            InputStream inputStream = file.getInputStream();
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object("/img/avatorImages/" + file.getOriginalFilename())
                            .stream(inputStream, inputStream.available(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
            return "File uploaded successfully!";
        } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            return "Error uploading file to MinIO: " + e.getMessage();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    //mv文件上传
    public static String uploadMvFile(MultipartFile file) {
        try {
            init();
            InputStream inputStream = file.getInputStream();
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(mvBucketName)
                            .object("mv/" + file.getOriginalFilename())
                            .stream(inputStream, inputStream.available(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
            return "File uploaded successfully!";
        } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            return "Error uploading file to MinIO: " + e.getMessage();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //mv封面
    public static String uploadMvCoverFile(MultipartFile file) {
        try {
            init();
            InputStream inputStream = file.getInputStream();
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object("mv/covers/" + file.getOriginalFilename())
                            .stream(inputStream, inputStream.available(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
            return "File uploaded successfully!";
        } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            return "Error uploading file to MinIO: " + e.getMessage();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //mv预览片段
    public static String uploadMvPreviewFile(MultipartFile file) {
        try {
            init();
            InputStream inputStream = file.getInputStream();
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object("mv/previews/" + file.getOriginalFilename())
                            .stream(inputStream, inputStream.available(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
            return "File uploaded successfully!";
        } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            return "Error uploading file to MinIO: " + e.getMessage();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

    // mv文件
//
//    /**
//     * 上传mv
//     */
//    public static String uploadMvFile(MultipartFile file) {
//        return uploadMvFile(file, null);
//    }
//
//    /**
//     * Upload MV file with custom path
//     * @param file MV file to upload
//     * @param resolution Optional resolution subfolder (hd/sd/4k)
//     */
//    public static String uploadMvFile(MultipartFile file, String resolution) {
//        try {
//            init();
//            String objectPath = "mv/" + (resolution != null ? resolution + "/" : "") + file.getOriginalFilename();
//
//            InputStream inputStream = file.getInputStream();
//            minioClient.putObject(
//                    PutObjectArgs.builder()
//                            .bucket(bucketName)
//                            .object(objectPath)
//                            .stream(inputStream, inputStream.available(), -1)
//                            .contentType(file.getContentType())
//                            .build()
//            );
//            return "MV uploaded successfully!";
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "Error uploading MV: " + e.getMessage();
//        }
//    }
//
//    /**
//     * Upload MV cover image
//     */
//    public static String uploadMvCoverFile(MultipartFile file) {
//        try {
//            init();
//            InputStream inputStream = file.getInputStream();
//            minioClient.putObject(
//                    PutObjectArgs.builder()
//                            .bucket(bucketName)
//                            .object("mv/covers/"+file.getOriginalFilename())
//                            .stream(inputStream, inputStream.available(), -1)
//                            .contentType(file.getContentType())
//                            .build()
//            );
//            return "MV cover uploaded successfully!";
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "Error uploading MV cover: " + e.getMessage();
//        }
//    }
//
//    /**
//     * Upload MV preview clip
//     */
//    public static String uploadMvPreviewFile(MultipartFile file) {
//        try {
//            init();
//            InputStream inputStream = file.getInputStream();
//            minioClient.putObject(
//                    PutObjectArgs.builder()
//                            .bucket(bucketName)
//                            .object("mv/previews/"+file.getOriginalFilename())
//                            .stream(inputStream, inputStream.available(), -1)
//                            .contentType(file.getContentType())
//                            .build()
//            );
//            return "MV preview uploaded successfully!";
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "Error uploading MV preview: " + e.getMessage();
//        }
//    }

