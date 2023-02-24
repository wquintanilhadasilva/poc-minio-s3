package com.aws.s3;

import lombok.Value;

@Value
public class ObjectMetaData {
	private String objectName;
	private String contentType;
	private long objectSize;
}
