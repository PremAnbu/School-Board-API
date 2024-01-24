package com.school.sba.serviceimpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.school.sba.entity.School;
import com.school.sba.entity.User;
import com.school.sba.enumuration.UserRole;
import com.school.sba.exception.InvalidUserException;
import com.school.sba.exception.SchoolAlreadyPresentForAdminException;
import com.school.sba.exception.SchoolNotFoundException;
import com.school.sba.exception.UserNotFoundByIdException;
import com.school.sba.repositary.SchoolRepo;
import com.school.sba.repositary.UserRepositary;
import com.school.sba.requestdto.SchoolRequest;
import com.school.sba.responsedto.SchoolResponse;
import com.school.sba.service.SchoolService;
import com.school.sba.util.ResponseStructure;

@Service
public class SchoolServiceImpl implements SchoolService {

	@Autowired
	private SchoolRepo schoolRepo;

	@Autowired
	private UserRepositary userRepositary;

	@Autowired
	private ResponseStructure<SchoolResponse> responseStructure;

	@Autowired
	private ResponseStructure<List<SchoolResponse>> structure;

	private School mapToSchoolRequest(SchoolRequest request) {
		System.err.println(request.getAddress());
		return School.builder().schoolName(request.getSchoolName()).contactNo(request.getContactNo())
				.emailId(request.getEmailId()).address(request.getAddress()).build();

	}

	private SchoolResponse mapToSchoolResponse(School school) {
		return SchoolResponse.builder().schoolName(school.getSchoolName()).contactNo(school.getContactNo())
				.emailId(school.getEmailId()).address(school.getAddress()).schoolId(school.getSchoolId()).build();
	}

	@Override
	public ResponseEntity<ResponseStructure<SchoolResponse>> createSchool(SchoolRequest schoolRequest, int userId) {
		User admin = userRepositary.findById(userId)
				.orElseThrow(() -> new UserNotFoundByIdException("User Id not exist"));
		if (admin.getUserRole() != UserRole.ADMIN) {
			throw new InvalidUserException("only ADMIN can create school");
		}
		if (admin.getSchool() != null) {
			throw new SchoolAlreadyPresentForAdminException("School already exists");
		}

		School school = schoolRepo.save(mapToSchoolRequest(schoolRequest));
		admin.setSchool(school);
		userRepositary.save(admin);
		responseStructure.setStatus(HttpStatus.CREATED.value());
		responseStructure.setMessage("School created for ADMIN");
		responseStructure.setData(mapToSchoolResponse(school));
		return new ResponseEntity<ResponseStructure<SchoolResponse>>(responseStructure, HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<ResponseStructure<SchoolResponse>> updateSchool(int schoolId, SchoolRequest school) {
		School save = schoolRepo.findById(schoolId).map(u -> {
			School mapToSchoolRequest = mapToSchoolRequest(school);
			mapToSchoolRequest.setSchoolId(schoolId);
			return schoolRepo.save(mapToSchoolRequest);
		}).orElseThrow(() -> new SchoolNotFoundException("School Not Found"));

		responseStructure.setStatus(HttpStatus.OK.value());
		responseStructure.setMessage("School Updated");
		responseStructure.setData(mapToSchoolResponse(save));

		return new ResponseEntity<ResponseStructure<SchoolResponse>>(responseStructure, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseStructure<List<SchoolResponse>>> findAllSchool() {
		List<School> findAll = schoolRepo.findAll();

		List<SchoolResponse> collect = findAll.stream().map(u -> mapToSchoolResponse(u)).collect(Collectors.toList());

		structure.setStatus(HttpStatus.FOUND.value());
		structure.setMessage("School Found");
		structure.setData(collect);

		return new ResponseEntity<ResponseStructure<List<SchoolResponse>>>(structure, HttpStatus.FOUND);
	}

	@Override
	public ResponseEntity<ResponseStructure<SchoolResponse>> deleteSchool(int schoolId) {
		School save = schoolRepo.findById(schoolId).orElseThrow(() -> new SchoolNotFoundException("School Not Found"));

		responseStructure.setStatus(HttpStatus.OK.value());
		responseStructure.setMessage("school deleted");
		responseStructure.setData(mapToSchoolResponse(save));

		return new ResponseEntity<ResponseStructure<SchoolResponse>>(responseStructure, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ResponseStructure<SchoolResponse>> findSchoolById(int schoolId) {
		School save = schoolRepo.findById(schoolId).orElseThrow(() -> new SchoolNotFoundException("School Not Found"));

		responseStructure.setStatus(HttpStatus.FOUND.value());
		responseStructure.setMessage("School found");
		responseStructure.setData(mapToSchoolResponse(save));

		return new ResponseEntity<ResponseStructure<SchoolResponse>>(responseStructure, HttpStatus.FOUND);

	}

}
