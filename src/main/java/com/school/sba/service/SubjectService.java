package com.school.sba.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.school.sba.requestdto.SubjectRequestDTO;
import com.school.sba.responsedto.AcademicProgramResponse;
import com.school.sba.responsedto.SubjectResponse;
import com.school.sba.util.ResponseStructure;

public interface SubjectService {

	ResponseEntity<ResponseStructure<AcademicProgramResponse>> addSubject(SubjectRequestDTO request, int programId);
	ResponseEntity<ResponseStructure<List<SubjectResponse>>> findAllSubjects();

}
