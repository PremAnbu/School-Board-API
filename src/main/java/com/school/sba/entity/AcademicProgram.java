package com.school.sba.entity;

import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Component;

import com.school.sba.enumuration.ProgramType;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Component
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AcademicProgram {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int programId;
	private ProgramType programType;
	private String programName;
	private LocalTime beginsAt;
	private LocalTime endsAt;
	
	@ManyToOne
	private School school;
	
	@ManyToMany
	private List<Subject> subjectNames;

}
