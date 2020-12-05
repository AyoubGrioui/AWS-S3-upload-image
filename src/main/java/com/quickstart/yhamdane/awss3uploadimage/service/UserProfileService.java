package com.quickstart.yhamdane.awss3uploadimage.service;

import com.quickstart.yhamdane.awss3uploadimage.bucket.BucketS3;
import com.quickstart.yhamdane.awss3uploadimage.filestore.FileStore;
import com.quickstart.yhamdane.awss3uploadimage.model.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.apache.http.entity.ContentType.*;

@Service
public class UserProfileService {
    private final UserProfileDataAccessService userProfileDataAccessService;
    private final FileStore fileStore;

    @Autowired
    public UserProfileService(UserProfileDataAccessService userProfileDataAccessService, FileStore fileStore) {
        this.userProfileDataAccessService = userProfileDataAccessService;
        this.fileStore = fileStore;
    }

    public List<UserProfile> getUserProfiles() {
        return userProfileDataAccessService.getUserProfiles();
    }


    public void uploadUserProfileImage(UUID userProfileId, MultipartFile file) {
        isFileEmpty(file);
        IsImage(file);
        UserProfile user = getUserProfileOrThrow(userProfileId);
        //4. grad some metadata from file if any
        Map<String, String> metadata= new HashMap<>();
        metadata.put("Content-Type",file.getContentType());
        metadata.put("Content-Length",String.valueOf(file.getSize()));

        //5. store the image in s3 and update userProfileImageLink in our database with s3 image link
        final String path = String.format("%s/%s", BucketS3.POFILE_IMAGE.getBucketName(), user.getUserProfileId());
        final String filename = String.format("%s-%s", file.getName(), UUID.randomUUID());

        try {
            fileStore.save(path,filename,Optional.of(metadata),file.getInputStream());
        }catch (IOException ex) {
            throw  new IllegalStateException(ex);
        }
    }

    private UserProfile getUserProfileOrThrow(UUID userProfileId) {
        return userProfileDataAccessService.getUserProfiles()
                .stream()
                .filter(userProfile -> userProfile.getUserProfileId().equals(userProfileId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(String.format("User with %d  id does not exist", userProfileId)));
    }

    private void IsImage(MultipartFile file) {
        if (!Arrays.asList(IMAGE_JPEG.getMimeType(), IMAGE_PNG.getMimeType(), IMAGE_GIF.getMimeType()).contains(file.getContentType())) {
            throw new IllegalStateException("Upload file must be an image [" + file.getContentType() + "]");
        }
    }

    private void isFileEmpty(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalStateException(" File is Empty");
        }
    }
}
