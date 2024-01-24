package com.school.sba.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.school.sba.entity.Subject;
import com.school.sba.repositary.AcademicProgramRepo;
import com.school.sba.repositary.SubjectRepo;
import com.school.sba.requestdto.SubjectRequestDTO;
import com.school.sba.responsedto.AcademicProgramResponse;
import com.school.sba.responsedto.SubjectResponse;
import com.school.sba.service.SubjectService;
import com.school.sba.util.ResponseStructure;

@Service
public class SubjectServiceImpl implements SubjectService{

	@Autowired
	private ResponseStructure<AcademicProgramResponse> structure;
	
	@Autowired
	private ResponseStructure<List<SubjectResponse>> structureList;
	
	@Autowired
	private AcademicProgramRepo academicProgramRepo;
	
	@Autowired
	private SubjectRepo subjectRepo;
	
	@Autowired
	private AcademicProgramServiceImpl academicProgramServiceImpl ;
	

	private SubjectResponse mapToSubjectResponse(Subject subject)
	{
		return SubjectResponse.builder()
				.subjectId(subject.getSublectId())
				.subjectNames(subject.getSubjectName())
				.build();

	}
	
	@Override
	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> addSubject(SubjectRequestDTO subjectRequestDTO,
			int programId) {
		
		return academicProgramRepo.findById(programId).map(program ->{ //found academic program
			List<Subject> subjects = (program.getSubjectNames() != null)?program.getSubjectNames(): new ArrayList<Subject>();
			
			// to add new Subjects that are specified by the client 
			subjectRequestDTO.getSubjectNames().forEach(name -> {
				boolean isPresent = false;
				for(Subject subject : subjects) {
					isPresent = (name.equalsIgnoreCase(subject.getSubjectName())) ?true : false;
					if(isPresent)break;
				}
			if( !isPresent)subjects.add(subjectRepo.findBySubjectName(name)
					.orElseGet(() -> subjectRepo.save(Subject.builder().subjectName(name).build())));
				
		});
			//to remove subject that are not specified by the client
			List<Subject> toBeRemoved =new ArrayList<Subject>();
			subjects .forEach(subject -> {
				boolean isPresent = false;
				for(String name : subjectRequestDTO.getSubjectNames()) {
					isPresent = (subject.getSubjectName().equalsIgnoreCase(name)) ?true : false;
					if(!isPresent)break;
				}
				if(!isPresent)toBeRemoved.add(subject);
			});
			subjects.removeAll(toBeRemoved);
			
			program.setSubjectNames(subjects);
			academicProgramRepo.save(program);
			structure.setStatus(HttpStatus.CREATED.value());
			structure.setMessage("Add Subject list to Academic program");
			structure.setData(academicProgramServiceImpl.mapToAcademicProgramResponse(program));
			return new ResponseEntity<ResponseStructure<AcademicProgramResponse>>(structure,HttpStatus.CREATED);
		}).orElseThrow();
	
	}
	@Override
	public ResponseEntity<ResponseStructure<List<SubjectResponse>>> findAllSubjects() 
	{
		List<Subject> findAll = subjectRepo.findAll();

		List<SubjectResponse> collect = findAll.stream()
				                               .map(u->mapToSubjectResponse(u))
			                                   .collect(Collectors.toList());

		structureList.setStatus(HttpStatus.FOUND.value());
		structureList.setMessage(" sujects found successfully ");
		structureList.setData(collect);

		return new ResponseEntity<ResponseStructure<List<SubjectResponse>>>(structureList,HttpStatus.FOUND);
	}

}
