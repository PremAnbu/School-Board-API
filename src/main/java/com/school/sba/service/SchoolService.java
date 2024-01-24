package com.school.sba.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.school.sba.requestdto.SchoolRequest;
import com.school.sba.responsedto.SchoolResponse;
import com.school.sba.util.ResponseStructure;

public interface SchoolService{

	ResponseEntity<ResponseStructure<SchoolResponse>> createSchool(SchoolRequest schoolRequest, int userId);

	ResponseEntity<ResponseStructure<SchoolResponse>> updateSchool(int schoolId, SchoolRequest school);

	ResponseEntity<ResponseStructure<List<SchoolResponse>>> findAllSchool();

	ResponseEntity<ResponseStructure<SchoolResponse>> deleteSchool(int schoolId);

	ResponseEntity<ResponseStructure<SchoolResponse>> findSchoolById(int schoolId);

}
