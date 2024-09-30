package com.projectx.resumes.services;

import com.projectx.common.exceptions.ResourceNotFoundException;
import com.projectx.common.payloads.EntityIdDto;
import com.projectx.common.payloads.EntityIdWithPageRequestDto;
import com.projectx.common.utils.Constants;
import com.projectx.common.utils.ResumeUtils;
import com.projectx.resumes.entities.EmployeeDetails;
import com.projectx.resumes.entities.StackItems;
import com.projectx.resumes.entities.TechnoStackDetails;
import com.projectx.resumes.payloads.*;
import com.projectx.resumes.repositories.TechnoStackRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class TechnoStackServiceImpl implements TechnoStackService{

    @Autowired
    private TechnoStackRepository technoStackRepository;

    @Autowired
    private EmployeeService employeeService;

    @Transactional
    @Override
    public Boolean insertUpdateEmployeeSkills(TechnoStackDto dto) throws ResourceNotFoundException {
        try {
            TechnoStackDetails technoStackDetails = null;
            EmployeeDetails employeeDetails = employeeService.getEmployeeDetails(dto.getEmployeeId());
            if (employeeDetails==null) {
                throw new ResourceNotFoundException(ResumeUtils.EMPLOYEE_DETAILS_NOT_FOUND);
            }
            if (dto.getId()==null) {
                 technoStackDetails = TechnoStackDetails.builder()
                         .stackName(dto.getStackName())
                         .stackItems(toList(dto.getSkillList()))
                         .insertedTime(new Date())
                         .updatedTime(new Date())
                         .employeeDetails(employeeDetails)
                         .build();
            } else {
                 technoStackDetails = technoStackRepository.getById(dto.getId());
                 if (technoStackDetails==null) {
                     throw new ResourceNotFoundException(ResumeUtils.TECHNO_STACK_NOT_EXISTS);
                 }
                 if (!dto.getStackName().equals(technoStackDetails.getStackName())) {
                     technoStackDetails.setStackName(dto.getStackName());
                 }
                 if (!dto.getEmployeeId().equals(technoStackDetails.getEmployeeDetails().getId())) {
                     technoStackDetails.setEmployeeDetails(employeeDetails);
                 }
                 technoStackDetails.getStackItems().clear();
                 technoStackDetails.setStackItems(toList(dto.getSkillList()));
                 technoStackDetails.setUpdatedTime(new Date());
            }
            return technoStackRepository.save(technoStackDetails)!=null?true:false;
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @Override
    public TechnoStackDto getById(EntityIdDto dto) throws ResourceNotFoundException {
        try {
            TechnoStackDetails details = technoStackRepository.getById(dto.getEntityId());
            if (details==null) {
                throw new ResourceNotFoundException(ResumeUtils.TECHNO_STACK_NOT_EXISTS);
            }
            Set<SkillDto> stackList = details.getStackItems()!=null
                    && !details.getStackItems().isEmpty()?
                    details.getStackItems().stream()
                            .map(data -> SkillDto.builder()
                                    .skillName(data.getStackName())
                                    .experience(data.getExperience())
                                    .build())
                            .collect(Collectors.toSet()):new HashSet<>();
            return TechnoStackDto.builder()
                    .id(details.getId())
                    .employeeId(details.getEmployeeDetails().getId())
                    .skillList(stackList)
                    .build();
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @Override
    public SkillsPageResponseDto getEmployeeSkills(EntityIdWithPageRequestDto dto) throws ResourceNotFoundException {
        String sortParameter ="";
        if (dto.getSortParam()!=null && dto.getSortParam().equals("srNo")) {
            sortParameter = "id";
        } else if (dto.getSortParam()!=null && dto.getSortParam().equals("stackName")) {
            sortParameter = "stack_name";
        } else if (dto.getSortParam()!=null && dto.getSortParam().equals("resumeName")) {
            sortParameter = "resume_id";
        } else if (dto.getSortParam()!=null && dto.getSortParam().equals("updatedDate")) {
            sortParameter = "updated_time";
        } else {
            sortParameter = "updated_time";
        }
        Sort sort = dto.getSortDir().equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortParameter).ascending()
                : Sort.by(sortParameter).descending();
        Pageable pageable = PageRequest.of(dto.getPageNumber()-1, dto.getPageSize(), sort);
        Page<TechnoStackDetails> technoStackDetails = null;
        if (dto.getEntityId()!=null) {
            technoStackDetails = technoStackRepository.getAllTechnoStacksByEmployeeId(dto.getEntityId(),pageable);
        } else {
            technoStackDetails = technoStackRepository.getAllTechnoStacks(pageable);
        }
        Integer pageNumber = dto.getPageNumber()-1;
        AtomicInteger index = new AtomicInteger(dto.getPageSize()*pageNumber);
        List<TechnoStackDetails> listOfStackDetails = technoStackDetails.getContent();
        List<ViewEmployeeSkillsDto> stackList = !listOfStackDetails.isEmpty()?listOfStackDetails.stream()
                .map(data -> ViewEmployeeSkillsDto.builder()
                        .srNo(index.incrementAndGet())
                        .stackId(data.getId())
                        .stackName(data.getStackName()!=null?data.getStackName(): Constants.DASH)
                        .employeeName(data.getEmployeeDetails()!=null
                                && data.getEmployeeDetails().getEmployeeName()!=null
                                ?data.getEmployeeDetails().getEmployeeName():Constants.DASH)
                        .updatedDate(data.getUpdatedTime()!=null?Constants.toExpenseDate(data.getUpdatedTime()): Constants.DASH)
                        .build()).collect(Collectors.toList())
                :new ArrayList<>();
        return !stackList.isEmpty()? SkillsPageResponseDto.builder()
                .pageNo(technoStackDetails.getNumber())
                .pageSize(technoStackDetails.getSize())
                .totalPages(technoStackDetails.getTotalPages())
                .totalElements(technoStackDetails.getTotalElements())
                .content(stackList)
                .build():new SkillsPageResponseDto();
    }

    @Override
    public List<ViewStackItemDto> getStackItems(EntityIdDto dto) {
        List<Object[]> fetchList = technoStackRepository.getStackItems(dto.getEntityId());
        AtomicInteger index = new AtomicInteger(0);
        return fetchList!=null && !fetchList.isEmpty()?fetchList.stream()
                .map(data -> ViewStackItemDto.builder()
                        .srNo(index.incrementAndGet())
                        .skillName(data[0]!=null?data[0].toString():Constants.DASH)
                        .experience(data[1]!=null?data[1].toString():Constants.DASH)
                        .build())
                .collect(Collectors.toList()):new ArrayList<>();
    }

    private Set<StackItems> toList(Set<SkillDto> skillList) {
        return skillList!=null && !skillList.isEmpty()?skillList.stream()
                .map(data -> StackItems.builder()
                        .stackName(data.getSkillName())
                        .experience(data.getExperience())
                        .build()).collect(Collectors.toSet()):new HashSet<>();
    }
}
