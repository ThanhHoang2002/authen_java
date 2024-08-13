package PizzaHut_be.service;

import PizzaHut_be.model.constant.FileConstant;
import PizzaHut_be.util.Util;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;

@CustomLog
@Service
@RequiredArgsConstructor
public class FileService {
    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucket;

    @SneakyThrows
    public String uploadImageFileFromImageUrl(String imageUrl, String userId, String typeUpload){
        try(CloseableHttpClient httpClient = HttpClients.createDefault()){
            HttpGet httpGet = new HttpGet(imageUrl);
            CloseableHttpResponse response = httpClient.execute(httpGet);

            String fileName = imageUrl.substring(imageUrl.lastIndexOf('/') + 1);
            String contentType = response.getEntity().getContentType().getValue();

            String fileExtension = contentType.substring(contentType.lastIndexOf('/') + 1);

            String originalFileName = String.join(".", fileName, fileExtension);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            response.getEntity().writeTo(outputStream);

            response.close();

            String filePathKey = getFilePathKey(originalFileName, userId, typeUpload);
            byte[] fileBytes = outputStream.toByteArray();

            fileBytes = resizeImage(fileBytes);

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(fileBytes);

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(filePathKey) // Set directory structure
                            .stream(byteArrayInputStream, fileBytes.length, -1)
                            .build());

            return Util.generateFileDirectory(bucket, filePathKey);

        }catch (Exception e){
            log.error("Upload image file from image url to minio failed: " + e.getMessage(), e);

            return null;
        }
    }

    private String getFilePathKey(String originalFilename, String userId, String typeUpload) {
        // Generate new filename
        Date current = new Date();

        String newFileName = String.valueOf(current.getTime());
        String modifiedFilename = modifyFilename(originalFilename, newFileName);

        return Util.generateFileDirectory(typeUpload, userId, modifiedFilename);
    }

    private String modifyFilename(String originalFilename, String newFilename) {
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));

        return newFilename + fileExtension;
    }

    private static BufferedImage getBufferedImageResize(BufferedImage originalImage, int width, int height) {
        BufferedImage resizedImage = new BufferedImage(width, height, originalImage.getType());
        Graphics2D graphics = resizedImage.createGraphics();

        graphics.drawImage(originalImage, 0, 0, width, height, null);
        graphics.dispose();

        return resizedImage;
    }

    @SneakyThrows
    private byte[] resizeImage(byte[] imageData) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(imageData);

            BufferedImage image = ImageIO.read(inputStream);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            int originalWidth = image.getWidth();
            int originalHeight = image.getHeight();
            int resizeDimension = FileConstant.FILE_IMAGE_RESIZE_DIMENSION_MAX;

            if (Math.max(originalWidth, originalHeight) > resizeDimension) {
                String formatName = getImageFormat(imageData);

                if (!Util.isNullOrEmpty(formatName)) {
                    // Resize the image with max of with or height is 480px
                    int newWidth;
                    int newHeight;

                    if (originalWidth > originalHeight) {
                        newWidth = FileConstant.FILE_IMAGE_RESIZE_DIMENSION_MAX;
                        newHeight = (int) (((double) originalHeight / originalWidth) * newWidth);
                    } else {
                        newHeight = FileConstant.FILE_IMAGE_RESIZE_DIMENSION_MAX;
                        newWidth = (int) (((double) originalWidth / originalHeight) * newHeight);
                    }

                    image = getBufferedImageResize(image, newWidth, newHeight);

                    ImageIO.write(image, formatName, outputStream);

                    imageData = outputStream.toByteArray();

                    outputStream.reset();
                }
            }

            return imageData;
        } catch (IOException e) {
            log.error("Compress image failed " + e.getMessage(), e);

            throw e;
        }
    }
    @SneakyThrows
    private static String getImageFormat(byte[] imageData) {
        try {
            ImageInputStream iis = ImageIO.createImageInputStream(new ByteArrayInputStream(imageData));

            Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(iis);

            if (imageReaders.hasNext()) {
                ImageReader reader = imageReaders.next();
                return reader.getFormatName();
            }
        } catch (IOException e) {
            log.error("Get image format failed " + e.getMessage(), e);

            return null;
        }

        return null;
    }
}
