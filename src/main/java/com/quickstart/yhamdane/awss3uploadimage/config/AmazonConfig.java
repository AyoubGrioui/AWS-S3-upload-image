package com.quickstart.yhamdane.awss3uploadimage.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmazonConfig {
    @Value("aws.amazon.credentials.AWSAccessKeyId")
    private String awsAccessKeyId;
    @Value("aws.amazon.credentials.AWSSecretKey")
    private String awsSecretKey;
    @Bean
    public AmazonS3 s3() {
        AWSCredentials awsCredentials = new BasicAWSCredentials(
                awsAccessKeyId, awsSecretKey
        );
        return AmazonS3Client.builder()
                .withRegion(Regions.AP_EAST_1)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }
}
