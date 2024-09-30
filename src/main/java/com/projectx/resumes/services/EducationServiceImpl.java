package com.projectx.resumes.services;

import com.projectx.common.exceptions.AlreadyExistsException;
import com.projectx.common.exceptions.ResourceNotFoundException;
import com.projectx.common.payloads.EntityIdDto;
import com.projectx.common.payloads.EntityIdWithPageRequestDto;
import com.projectx.common.utils.Constants;
import com.projectx.common.utils.ResumeUtils;
import com.projectx.resumes.entities.EducationDetails;
import com.projectx.resumes.entities.EmployeeDetails;
import com.projectx.resumes.payloads.EducationDto;
import com.projectx.resumes.payloads.EducationPageResponseDto;
import com.projectx.resumes.payloads.ViewEducationDto;
import com.projectx.resumes.repositories.EducationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class EducationServiceImpl implements EducationService {

    @Autowired
    private EducationRepository educationRepository;

    @Autowired
    private EmployeeService employeeService;

    @Override
    public Boolean insertUpdate(EducationDto dto) throws ResourceNotFoundException, AlreadyExistsException, ParseException {
        EducationDetails educationDetails = null;
        EmployeeDetails employeeDetails = employeeService.getEmployeeDetails(dto.getEmployeeId());
        if (employeeDetails==null) {
            throw new ResourceNotFoundException(ResumeUtils.EMPLOYEE_DETAILS_NOT_FOUND);
        }
        if (dto.getId()==null) {
            educationDetails = EducationDetails.builder()
                    .degreeName(dto.getDegreeName())
                    .universityName(dto.getUniversityName())
                    .startDate(dto.getStartDate()!=null?Constants.getISODate(dto.getStartDate()):null)
                    .endDate(dto.getEndDate()!=null?Constants.getISODate(dto.getEndDate()):null)
                    .percentage(dto.getPercentage())
                    .employeeDetails(employeeDetails)
                    .insertedTime(new Date())
                    .updatedTime(new Date())
                    .build();
        } else {
            educationDetails = educationRepository.getById(dto.getId());
            if (educationDetails==null) {
                throw new ResourceNotFoundException(ResumeUtils.EDUCATION_DETAILS_NOT_FOUND);
            }
            if (!dto.getDegreeName().equals(educationDetails.getDegreeName())) {
                educationDetails.setDegreeName(dto.getDegreeName());
            }
            if (!dto.getUniversityName().equals(educationDetails.getUniversityName())){
                educationDetails.setUniversityName(dto.getUniversityName());
            }
            if (!dto.getPercentage().equals(educationDetails.getPercentage())) {
                educationDetails.setPercentage(dto.getPercentage());
            }
            if (!dto.getEmployeeId().equals(educationDetails.getEmployeeDetails().getId())) {
                educationDetails.setEmployeeDetails(employeeDetails);
            }
            if (!dto.getStartDate().equals(Constants.toExpenseDate(educationDetails.getStartDate()))) {
                educationDetails.setStartDate(dto.getStartDate()!=null?Constants.getISODate(dto.getStartDate()):null);
            }
            if (!dto.getEndDate().equals(Constants.toExpenseDate(educationDetails.getEndDate()))) {
                educationDetails.setEndDate(dto.getEndDate()!=null?Constants.getISODate(dto.getEndDate()):null);
            }
            educationDetails.setUpdatedTime(new Date());
        }
        try {
             return educationRepository.save(educationDetails)!=null?true:false;
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @Override
    public EducationDto getById(EntityIdDto dto) throws ResourceNotFoundException {
        try {
             EducationDetails details = educationRepository.getById(dto.getEntityId());
             if (details==null) {
                 throw new ResourceNotFoundException(ResumeUtils.EDUCATION_DETAILS_NOT_FOUND);
             }
             return EducationDto.builder()
                     .id(details.getId())
                     .employeeId(details.getEmployeeDetails().getId())
                     .degreeName(details.getDegreeName())
                     .universityName(details.getUniversityName())
                     .percentage(details.getPercentage())
                     .startDate(Constants.toExpenseDate(details.getStartDate()))
                     .endDate(Constants.toExpenseDate(details.getEndDate()))
                     .build();
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @Override
    public EducationPageResponseDto getEmployeeEducations(EntityIdWithPageRequestDto dto) throws ResourceNotFoundException {
        String sortParameter ="";
        if (dto.getSortParam()!=null && dto.getSortParam().equals("srNo")) {
            sortParameter = "id";
        } else if (dto.getSortParam()!=null && dto.getSortParam().equals("degreeName")) {
            sortParameter = "degree_name";
        } else if (dto.getSortParam()!=null && dto.getSortParam().equals("universityName")) {
            sortParameter = "university_name";
        } else if (dto.getSortParam()!=null && dto.getSortParam().equals("percentage")) {
            sortParameter = "percentage";
        } else if (dto.getSortParam()!=null && dto.getSortParam().equals("startDate")) {
            sortParameter = "start_date";
        } else if (dto.getSortParam()!=null && dto.getSortParam().equals("endDate")){
            sortParameter = "end_date";
        } else {
            sortParameter = "updated_time";
        }
        Sort sort = dto.getSortDir().equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortParameter).ascending()
                : Sort.by(sortParameter).descending();
        Pageable pageable = PageRequest.of(dto.getPageNumber()-1, dto.getPageSize(), sort);
        Page<EducationDetails> educationDetails = null;
        if (dto.getEntityId()!=null) {
            educationDetails = educationRepository.getAllEducationsByEmployeeId(dto.getEntityId(),pageable);
        } else {
            educationDetails = educationRepository.getAllEducations(pageable);
        }
        Integer pageNumber = dto.getPageNumber()-1;
        AtomicInteger index = new AtomicInteger(dto.getPageSize()*pageNumber);
        List<EducationDetails> listOfEducationDetails = educationDetails.getContent();
        List<ViewEducationDto> educationList = !listOfEducationDetails.isEmpty()?listOfEducationDetails.stream()
                .map(data -> ViewEducationDto.builder()
                        .srNo(index.incrementAndGet())
                        .id(data.getId())
                        .employeeId(data.getEmployeeDetails()!=null &&
                                data.getEmployeeDetails().getId()!=null?
                                data.getEmployeeDetails().getId():null)
                        .employeeName(data.getEmployeeDetails()!=null
                                && data.getEmployeeDetails().getEmployeeName()!=null
                                ?data.getEmployeeDetails().getEmployeeName():Constants.DASH)
                        .degreeName(data.getDegreeName()!=null?data.getDegreeName(): Constants.DASH)
                        .universityName(data.getUniversityName()!=null?data.getUniversityName():Constants.DASH)
                        .percentage(data.getPercentage()!=null?data.getPercentage():Constants.DASH)
                        .startDate(data.getStartDate()!=null?Constants.toExpenseDate(data.getStartDate()): Constants.DASH)
                        .endDate(data.getEndDate()!=null?Constants.toExpenseDate(data.getEndDate()):Constants.DASH)
                        .build()).collect(Collectors.toList())
                :new ArrayList<>();
        return !educationList.isEmpty()? EducationPageResponseDto.builder()
                .pageNo(educationDetails.getNumber())
                .pageSize(educationDetails.getSize())
                .totalPages(educationDetails.getTotalPages())
                .totalElements(educationDetails.getTotalElements())
                .content(educationList)
                .build():new EducationPageResponseDto();
    }
}
