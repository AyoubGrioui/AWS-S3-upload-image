package com.quickstart.yhamdane.awss3uploadimage.bucket;

public enum BucketS3 {

    POFILE_IMAGE("yhamdane-s3-upload-images-001");

    private final String bucketName;

    BucketS3(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getBucketName() {
        return bucketName;
    }
}
