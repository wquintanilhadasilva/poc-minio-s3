package com.aws.s3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/objects", produces = MediaType.APPLICATION_JSON_VALUE)
public class ObjectStorageController {
	@Autowired
	ObjectStorageLogic objectStorageLogic;
	
	@GetMapping("/welcome")
	public String welcome() {
		return "welcome";
	}
	
	@PostMapping(value = "/persistObject", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String persistObject(@ModelAttribute final ObjectEntryBo fileEntry) {
		return objectStorageLogic.persistObject(fileEntry);
	}
	
	@GetMapping("/getObject/{objectId}")  //, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ObjectEntryBo getObject(@PathVariable(name = "objectId") final String objectId) {
		return objectStorageLogic.getObject(objectId);
	}
	
	
	@GetMapping("/getObjectMetaData/{objectId}")
	public ObjectMetaData getObjectMetaData(@PathVariable(name = "objectId") final String objectId) {
		return objectStorageLogic.getObjectMetaData(objectId);
	}
	
	@DeleteMapping("/deleteObject/{objectId}")
	public void deleteObject(@PathVariable(name = "objectId") final String objectId) {
		objectStorageLogic.deleteObject(objectId);
	}
}
