package com.pixcat.warehouseproducts;

import io.minio.*;
import io.minio.errors.MinioException;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class S3ImageRepository implements ImageRepository {

    private static final String bucketName = "product-images";
    private static final String publicGetObjectPolicy = """
            {
                 "Statement": [
                     {
                         "Action": "s3:GetObject",
                         "Effect": "Allow",
                         "Principal": "*",
                         "Resource": "arn:aws:s3:::product-images/*"
                     }
                 ],
                 "Version": "2012-10-17"
             }""";

    private final MinioClient minioClient =
            MinioClient.builder()
                    .endpoint("http://localhost:7000")
                    .credentials("local.s3", "local.s3")
                    .build();

    @Override
    public void saveImage(ProductId productId, ImagePayload image) {
        try {
            createBucketIfAbsent();
            putImageIntoBucket(productId.value, image);
        } catch (MinioException | IOException ex) {
            throw new RuntimeException(ex);
        } catch (NoSuchAlgorithmException | InvalidKeyException ex) {
            throw new SecurityException("Could not authorize S3 access", ex);
        }
    }

    private void createBucketIfAbsent()
            throws MinioException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        final var bucketExists = BucketExistsArgs.builder()
                .bucket(bucketName)
                .build();
        boolean foundBucket = minioClient.bucketExists(bucketExists);
        if (!foundBucket) {
            final var makeBucket = MakeBucketArgs.builder()
                    .bucket(bucketName)
                    .build();
            minioClient.makeBucket(makeBucket);
            setBucketAsPublic();

            log.info("Created public {} bucket", bucketName);
        }
    }

    private void setBucketAsPublic()
            throws MinioException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        final var setBucketPolicy = SetBucketPolicyArgs.builder()
                .bucket(bucketName)
                .config(publicGetObjectPolicy)
                .build();
        minioClient.setBucketPolicy(setBucketPolicy);
    }

    private void putImageIntoBucket(String objectName, ImagePayload image)
            throws MinioException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        final var putObject = PutObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .stream(new ByteArrayInputStream(image.getData()), image.getData().length, -1)
                .contentType(image.getMediaType().toString())
                .build();
        minioClient.putObject(putObject);

        log.info("Image {} saved", objectName);
    }
}
