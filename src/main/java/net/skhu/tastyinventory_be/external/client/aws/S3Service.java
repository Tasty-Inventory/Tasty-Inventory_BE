package net.skhu.tastyinventory_be.external.client.aws;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import net.skhu.tastyinventory_be.exception.ErrorCode;
import net.skhu.tastyinventory_be.exception.model.BadRequestException;
import net.skhu.tastyinventory_be.exception.model.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    public final Logger log = LoggerFactory.getLogger(this.getClass());

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    @PostConstruct
    public AmazonS3Client amazonS3Client() {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        return (AmazonS3Client) AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }

    public String uploadImage(MultipartFile multipartFile, String folder) {
        if (multipartFile == null) {
            return "https://s3.ap-northeast-2.amazonaws.com/tasty-inventory-be-image/menu/image/4ae07266-577b-41bd-aa07-a8644c853914.jpeg2024-06-18T03%3A57%3A09.656107790";
        }
        String fileName = createFileName(multipartFile.getOriginalFilename());
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType(multipartFile.getContentType());

        try(InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3.putObject(new PutObjectRequest(bucket+"/"+ folder + "/image", fileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            return amazonS3.getUrl(bucket+"/"+ folder + "/image", fileName).toString();
        } catch(IOException e) {
            throw new NotFoundException(ErrorCode.NOT_FOUND_IMAGE_EXCEPTION, ErrorCode.NOT_FOUND_IMAGE_EXCEPTION.getMessage());
        } catch (AmazonServiceException e) {
            log.error("AmazonServiceException: Error Message: " + e.getMessage());
            log.error("HTTP Status Code: " + e.getStatusCode());
            log.error("AWS Error Code: " + e.getErrorCode());
            log.error("Error Type: " + e.getErrorType());
            log.error("Request ID: " + e.getRequestId());
            throw new NotFoundException(ErrorCode.NOT_FOUND_IMAGE_EXCEPTION, "AmazonServiceException");
        } catch (SdkClientException e) {
            throw new NotFoundException(ErrorCode.NOT_FOUND_IMAGE_EXCEPTION, "SdkClientException");
        }
    }

    // 파일명 (중복 방지)
    private String createFileName(String fileName) {
        LocalDateTime localDateTime = LocalDateTime.now();
        return UUID.randomUUID().toString().concat(getFileExtension(fileName)) + localDateTime;
    }

    // 파일 유효성 검사
    private String getFileExtension(String fileName) {
        if (fileName.length() == 0) {
            throw new NotFoundException(ErrorCode.NOT_FOUND_IMAGE_EXCEPTION, ErrorCode.NOT_FOUND_IMAGE_EXCEPTION.getMessage());
        }
        ArrayList<String> fileValidate = new ArrayList<>();
        fileValidate.add(".jpg");
        fileValidate.add(".JPG");
        fileValidate.add(".jpeg");
        fileValidate.add(".JPEG");
        fileValidate.add(".png");
        fileValidate.add(".PNG");
        fileValidate.add(".webp");
        fileValidate.add(".WebP");
        fileValidate.add(".heif");
        fileValidate.add(".HEIF");
        fileValidate.add(".heic");
        fileValidate.add(".HEIC");
        fileValidate.add(".svg");
        fileValidate.add(".SVG");
        String idxFileName = fileName.substring(fileName.lastIndexOf("."));
        if (!fileValidate.contains(idxFileName)) {
            throw new BadRequestException(ErrorCode.VALIDATION_IMAGE_REQUEST_FAILED, ErrorCode.VALIDATION_IMAGE_REQUEST_FAILED.getMessage());
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }

    // 이미지 삭제
    public void deleteFile(String imageUrl) {
        try {
            String imageKey = imageUrl.substring(49);
            amazonS3.deleteObject(bucket, imageKey);
        } catch (AmazonServiceException e) {
            log.error("AmazonServiceException: Error Message: " + e.getMessage());
            log.error("HTTP Status Code: " + e.getStatusCode());
            log.error("AWS Error Code: " + e.getErrorCode());
            log.error("Error Type: " + e.getErrorType());
            log.error("Request ID: " + e.getRequestId());
        }
    }
}