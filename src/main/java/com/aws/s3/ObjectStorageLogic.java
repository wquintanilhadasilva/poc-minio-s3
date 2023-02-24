package com.aws.s3;

public interface ObjectStorageLogic {
	String persistObject(ObjectEntryBo fileEntry);
	ObjectEntryBo getObject(String objectId);
	ObjectMetaData getObjectMetaData(String objectId);
	void deleteObject(String objectId);
}
