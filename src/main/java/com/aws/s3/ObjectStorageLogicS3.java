package com.aws.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import java.io.ByteArrayInputStream;

@Profile("s3-object-storage")
@Component
public class ObjectStorageLogicS3 implements ObjectStorageLogic {
	@Autowired
	AmazonS3 s3Client;
	
	@Value("${cloud.aws.s3.bucket.name}")
	String bucketName;
	
	@Value("${cloud.aws.s3.anonymous-files.enabled}")
	Boolean anonymousFilesEnabled;
	
	@Override
	@SneakyThrows
	public String persistObject(@NonNull ObjectEntryBo objectEntry) {
		String keyName = getKeyName(objectEntry, anonymousFilesEnabled);
		s3Client.putObject(bucketName,
				keyName,
				new ByteArrayInputStream(objectEntry.getData()),
				map(objectEntry));
		return keyName;
	}
	
	@Override
	@SneakyThrows
	public ObjectEntryBo getObject(@NonNull String objectId) {
		final S3Object s3Object = s3Client.getObject(bucketName, objectId);
		return map(objectId,
				s3Object.getObjectContent().readAllBytes(),
				map(s3Object.getObjectMetadata()));
	}
	
	@Override
	public ObjectMetaData getObjectMetaData(@NonNull String objectId) {
		return map(s3Client.getObjectMetadata(bucketName, objectId));
	}
	
	@Override
	public void deleteObject(@NonNull String objectId) {
		s3Client.deleteObject(bucketName, objectId);
	}
	
	static ObjectEntryBo map(@NonNull String keyName, byte[] data, @NonNull ObjectMetaData objectMetaDataProjection)  {
		return ObjectEntryBo.builder()
				.id(keyName)
				.data(data)
				.objectName(objectMetaDataProjection.getObjectName())
				.contentType(objectMetaDataProjection.getContentType())
				.objectSize(objectMetaDataProjection.getObjectSize())
				.build();
	}
	
	static ObjectMetaData map(ObjectMetadata s3ObjectMetaData) {
		return new ObjectMetaData(
				s3ObjectMetaData.getUserMetadata().get("filename"),
				s3ObjectMetaData.getContentType(),
				s3ObjectMetaData.getContentLength());
	}
	
	static ObjectMetadata map(@NonNull ObjectEntryBo fileEntry) {
		final ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentLength(fileEntry.getData().length);
		objectMetadata.setContentType(fileEntry.getContentType());
		objectMetadata.addUserMetadata("filename", fileEntry.getObjectName());
		return objectMetadata;
	}
	
	static String getKeyName(@NonNull ObjectEntryBo objectEntry, Boolean anonymousFilesEnabled) {
		final String[] fix = objectEntry.getObjectName().split("\\.");
		return (fix.length < 2 || anonymousFilesEnabled)
				? objectEntry.getId()
				: (fix[0] + "_" + objectEntry.getId() + "." + fix[1]);
	}

}
