package com.school.sba.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.school.sba.requestdto.SchoolRequest;
import com.school.sba.responsedto.SchoolResponse;
import com.school.sba.service.SchoolService;
import com.school.sba.util.ResponseStructure;

@RestController
public class SchoolController {
	
	@Autowired
	private SchoolService schoolService;
	
	@PostMapping("/users/{userId}/schools")
	public ResponseEntity<ResponseStructure<SchoolResponse>> createSchool(@RequestBody SchoolRequest schoolRequest,@PathVariable int userId){
		return schoolService.createSchool(schoolRequest,userId);
	}
	
	@PutMapping("/schools/{schoolId}")
	public ResponseEntity<ResponseStructure<SchoolResponse>> updateSchool(@PathVariable int schoolId,@RequestBody SchoolRequest school)
	{
		return schoolService.updateSchool(schoolId,school);
	}

	@GetMapping("/schools")
	public ResponseEntity<ResponseStructure<List<SchoolResponse>>> findAllSchool()
	{
		return schoolService.findAllSchool();
	}
	@DeleteMapping("/schools/{schoolId}")
	public ResponseEntity<ResponseStructure<SchoolResponse>> deleteSchool(@PathVariable int schoolId)
	{
		return schoolService.deleteSchool(schoolId);
	}
	@GetMapping("/schools/{schoolId}")
	public ResponseEntity<ResponseStructure<SchoolResponse>> findSchoolById(@PathVariable int schoolId)
	{
		return schoolService.findSchoolById(schoolId);
	}
}
