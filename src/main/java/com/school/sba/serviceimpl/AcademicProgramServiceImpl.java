package com.school.sba.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.school.sba.entity.AcademicProgram;
import com.school.sba.entity.School;
import com.school.sba.exception.SchoolNotFoundException;
import com.school.sba.repositary.AcademicProgramRepo;
import com.school.sba.repositary.SchoolRepo;
import com.school.sba.requestdto.AcademicProgramRequest;
import com.school.sba.responsedto.AcademicProgramResponse;
import com.school.sba.service.AcademicProgramService;
import com.school.sba.util.ResponseStructure;

@Service
public class AcademicProgramServiceImpl implements AcademicProgramService{

	@Autowired
	private ResponseStructure<AcademicProgramResponse> responseStructure;
	
	@Autowired
	private SchoolRepo schoolRepo;
	
	@Autowired
	private AcademicProgramRepo academicProgramRepo;
	
	public AcademicProgram mapToAcademicProgramRequest(AcademicProgramRequest request) {
		return AcademicProgram.builder()
				.beginsAt(request.getBeginsAt())
				.endsAt(request.getEndsAt())
				.programName(request.getProgramName())
				.programType(request.getProgramType())
				.build();
	}
	
	public AcademicProgramResponse mapToAcademicProgramResponse(AcademicProgram academicProgram) {
		return AcademicProgramResponse.builder()
				.beginsAt(academicProgram.getBeginsAt())
				.endsAt(academicProgram.getEndsAt())
				.programId(academicProgram.getProgramId())
				.programName(academicProgram.getProgramName())
				.programType(academicProgram.getProgramType())
				.build();
	}

	@Override
	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> createAcademicProgram(
			AcademicProgramRequest request, int schoolId) {
		
		
//			return schoolRepo.findById(schoolId).map(school->{
//				AcademicProgram academicProgram=academicProgramRepo.save(mapToAcademicProgramRequest(request));
//				school.getAcademicPrograms().add(academicProgram);
//				System.out.println(school.getAcademicPrograms());
//				responseStructure.setStatus(HttpStatus.CREATED.value());
//				responseStructure.setMessage("Academic program created for school");
//				responseStructure.setData(mapToAcademicProgramResponse(academicProgram));
//				return new ResponseEntity<ResponseStructure<AcademicProgramResponse>>(responseStructure,HttpStatus.CREATED);
//			}).orElseThrow(()->new SchoolNotFoundException("School not found in the given Id"));	
		
		
		
		School school=schoolRepo.findById(schoolId).orElseThrow(()->new SchoolNotFoundException("School not found in the given Id"));
		AcademicProgram academicProgram =mapToAcademicProgramRequest(request);
		academicProgram.setSchool(school);
		academicProgramRepo.save(academicProgram);
		responseStructure.setStatus(HttpStatus.CREATED.value());
		responseStructure.setMessage("Academic program created for school");
		responseStructure.setData(mapToAcademicProgramResponse(academicProgram));
		return new ResponseEntity<ResponseStructure<AcademicProgramResponse>>(responseStructure,HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<ResponseStructure<List<AcademicProgramResponse>>> findAllAcademicPrograms(int schoolId) {
		return schoolRepo.findById(schoolId).map(school->{
			List<AcademicProgram> list=school.getListAcademicPrograms();
			ResponseStructure<List<AcademicProgramResponse>> rs=new ResponseStructure<>();
			List<AcademicProgramResponse> l=new ArrayList<>();
			
			for(AcademicProgram obj:list) {
				l.add(mapToAcademicProgramResponse(obj));
			}
			rs.setStatus(HttpStatus.FOUND.value());
			rs.setMessage("Academic program's found");
			rs.setData(l);
			return new ResponseEntity<ResponseStructure<List<AcademicProgramResponse>>>(rs,HttpStatus.FOUND);
		}).orElseThrow(()->new SchoolNotFoundException("School not found in the given Id"));
	}
	
	
}
