package com.aws.s3;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

//@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ObjectEntryBo {
	
	private String id;
	private String objectName;
	private String contentType;
	private long objectSize;
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	private MultipartFile file;
	private byte[] data;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getObjectName() {
		return objectName;
	}
	
	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}
	
	public String getContentType() {
		return contentType;
	}
	
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	public long getObjectSize() {
		return objectSize;
	}
	
	public void setObjectSize(long objectSize) {
		this.objectSize = objectSize;
	}
	
	public byte[] getData() {
		return data;
	}
	
	public void setData(byte[] data) {
		this.data = data;
	}
	
	public void setFile(MultipartFile file) throws IOException {
		this.file = file;
		if (file != null) {
			this.data = file.getBytes();
			this.id = file.getName();
			this.objectName = file.getOriginalFilename();
			this.contentType = file.getOriginalFilename()
					.substring(file.getOriginalFilename().lastIndexOf(".") + 1);
			this.objectSize = file.getSize();
		}
	}
	public MultipartFile getFile(){
		return this.file;
	}
}
