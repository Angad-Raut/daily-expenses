package com.projectx.resumes.services;

import com.projectx.common.exceptions.AlreadyExistsException;
import com.projectx.common.exceptions.ResourceNotFoundException;
import com.projectx.common.payloads.*;
import com.projectx.common.utils.Constants;
import com.projectx.common.utils.ResumeUtils;
import com.projectx.companies.entities.CompanyDetails;
import com.projectx.companies.services.CompanyService;
import com.projectx.resumes.entities.EmployeeDetails;
import com.projectx.resumes.payloads.*;
import com.projectx.resumes.repositories.EmployeeRepository;
import javax.transaction.Transactional;
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
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CompanyService companyService;

    @Override
    public Boolean createEmployee(EmployeeDto dto) throws ResourceNotFoundException, AlreadyExistsException {
        EmployeeDetails resumeDetails = null;
        if (dto.getId()==null) {
              isMobileExists(dto.getEmployeeMobile());
              isEmailExists(dto.getEmployeeEmail());
              resumeDetails = EmployeeDetails.builder()
                      .employeeName(dto.getEmployeeName())
                      .employeeMobile(dto.getEmployeeMobile())
                      .employeeEmail(dto.getEmployeeEmail())
                      .linkedInUrl(dto.getLinkedInUrl()!=null?dto.getLinkedInUrl():null)
                      .gitHubUrl(dto.getGitHubUrl()!=null?dto.getGitHubUrl():null)
                      .profileSummary(dto.getProfileSummary())
                      .totalExperience(dto.getTotalExperience())
                      .status(true)
                      .insertedDate(new Date())
                      .updatedDate(new Date())
                      .companyList(new HashSet<>())
                      .educationDetails(new ArrayList<>())
                      .skillDetails(new ArrayList<>())
                      .projectDetails(new ArrayList<>())
                      .build();
        } else {
              resumeDetails = employeeRepository.getById(dto.getId());
              if (resumeDetails==null) {
                  throw new ResourceNotFoundException(ResumeUtils.EMPLOYEE_DETAILS_NOT_FOUND);
              }
              if (!dto.getEmployeeName().equals(resumeDetails.getEmployeeName())) {
                  resumeDetails.setEmployeeName(dto.getEmployeeName());
              }
              if (!dto.getEmployeeMobile().equals(resumeDetails.getEmployeeMobile())) {
                  isMobileExists(dto.getEmployeeMobile());
                  resumeDetails.setEmployeeMobile(dto.getEmployeeMobile());
              }
              if (!dto.getEmployeeEmail().equals(resumeDetails.getEmployeeEmail())) {
                  isEmailExists(dto.getEmployeeEmail());
                  resumeDetails.setEmployeeEmail(dto.getEmployeeEmail());
              }
              if (!dto.getProfileSummary().equals(resumeDetails.getProfileSummary())) {
                  resumeDetails.setProfileSummary(dto.getProfileSummary());
              }
              if (!dto.getTotalExperience().equals(resumeDetails.getTotalExperience())) {
                  resumeDetails.setTotalExperience(dto.getTotalExperience());
              }
              if (dto.getLinkedInUrl()!=null && resumeDetails.getLinkedInUrl()!=null) {
                  if (!dto.getLinkedInUrl().equals(resumeDetails.getLinkedInUrl())) {
                      resumeDetails.setLinkedInUrl(dto.getLinkedInUrl());
                  }
              } else if (dto.getLinkedInUrl()!=null && resumeDetails.getLinkedInUrl()==null) {
                  resumeDetails.setLinkedInUrl(dto.getLinkedInUrl());
              } else if (dto.getLinkedInUrl()==null && resumeDetails.getLinkedInUrl()!=null) {
                  resumeDetails.setLinkedInUrl(null);
              }
            if (dto.getGitHubUrl()!=null && resumeDetails.getGitHubUrl()!=null) {
                if (!dto.getGitHubUrl().equals(resumeDetails.getGitHubUrl())) {
                    resumeDetails.setGitHubUrl(dto.getGitHubUrl());
                }
            } else if (dto.getGitHubUrl()!=null && resumeDetails.getGitHubUrl()==null) {
                resumeDetails.setGitHubUrl(dto.getGitHubUrl());
            } else if (dto.getGitHubUrl()==null && resumeDetails.getGitHubUrl()!=null) {
                resumeDetails.setGitHubUrl(null);
            }
            resumeDetails.setUpdatedDate(new Date());
        }
        try {
             return employeeRepository.save(resumeDetails)!=null?true:false;
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (AlreadyExistsException e) {
            throw new AlreadyExistsException(e.getMessage());
        }
    }

    @Override
    public EmployeeDto getById(EntityIdDto dto) throws ResourceNotFoundException {
        try {
            EmployeeDetails details = employeeRepository.getById(dto.getEntityId());
            if (details==null) {
                throw new ResourceNotFoundException(ResumeUtils.EMPLOYEE_DETAILS_NOT_FOUND);
            }
            return EmployeeDto.builder()
                    .id(details.getId())
                    .employeeName(details.getEmployeeName())
                    .employeeMobile(details.getEmployeeMobile())
                    .employeeEmail(details.getEmployeeEmail())
                    .linkedInUrl(details.getLinkedInUrl()!=null?details.getLinkedInUrl():null)
                    .gitHubUrl(details.getGitHubUrl()!=null?details.getGitHubUrl():null)
                    .profileSummary(details.getProfileSummary())
                    .totalExperience(details.getTotalExperience())
                    .build();
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @Override
    public EmployeePageResponseDto getAllEmployees(PageRequestDto dto) {
        String sortParameter ="";
        if (dto.getSortParam()!=null && dto.getSortParam().equals("srNo")) {
            sortParameter = "updated_date";
        } else if (dto.getSortParam()!=null && dto.getSortParam().equals("employeeName")) {
            sortParameter = "employee_name";
        } else if (dto.getSortParam()!=null && dto.getSortParam().equals("employeeMobile")) {
            sortParameter = "employee_mobile";
        } else if (dto.getSortParam()!=null && dto.getSortParam().equals("employeeEmail")) {
            sortParameter = "employee_email";
        } else if (dto.getSortParam()!=null && dto.getSortParam().equals("totalExperience")) {
            sortParameter = "total_experience";
        } else {
            sortParameter = "updated_date";
        }
        Sort sort = dto.getSortDir().equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortParameter).ascending()
                : Sort.by(sortParameter).descending();
        Pageable pageable = PageRequest.of(dto.getPageNumber()-1, dto.getPageSize(), sort);
        Page<EmployeeDetails> employeeDetails = employeeRepository.getAllEmployees(pageable);
        Integer pageNumber = dto.getPageNumber()-1;
        AtomicInteger index = new AtomicInteger(dto.getPageSize()*pageNumber);
        List<EmployeeDetails> listOfEmployees = employeeDetails.getContent();
        List<ViewEmployeesDto> employeeList = !listOfEmployees.isEmpty()?listOfEmployees.stream()
                .map(data -> ViewEmployeesDto.builder()
                        .srNo(index.incrementAndGet())
                        .employeeId(data.getId()!=null?data.getId():null)
                        .employeeName(data.getEmployeeName()!=null?data.getEmployeeName(): Constants.DASH)
                        .employeeMobile(data.getEmployeeMobile()!=null?data.getEmployeeMobile():null)
                        .employeeEmail(data.getEmployeeEmail()!=null?data.getEmployeeEmail():Constants.DASH)
                        .totalExperience(data.getTotalExperience()!=null?data.getTotalExperience():Constants.DASH)
                        .build()).collect(Collectors.toList())
                :new ArrayList<>();
        return !employeeList.isEmpty()? EmployeePageResponseDto.builder()
                .pageNo(employeeDetails.getNumber())
                .pageSize(employeeDetails.getSize())
                .totalPages(employeeDetails.getTotalPages())
                .totalElements(employeeDetails.getTotalElements())
                .content(employeeList)
                .build():new EmployeePageResponseDto();
    }

    @Override
    public EmployeeDetails getEmployeeDetails(Long resumeId) throws ResourceNotFoundException {
        EmployeeDetails details = employeeRepository.getById(resumeId);
        return details!=null?details:null;
    }

    @Override
    public Boolean updateStatus(EntityIdDto dto) throws ResourceNotFoundException {
        try {
            EmployeeDetails details = employeeRepository.getById(dto.getEntityId());
            if (details==null) {
                throw new ResourceNotFoundException(ResumeUtils.EMPLOYEE_DETAILS_NOT_FOUND);
            }
            Boolean status = details.getStatus()?false:true;
            Integer count = employeeRepository.updateStatus(details.getId(),status);
            return count==1?true:false;
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @Transactional
    @Override
    public Boolean insertUpdateEmployeeCompany(EmployeeCompanyIdDto dto) throws ResourceNotFoundException {
        try {
            EmployeeDetails employeeDetails = employeeRepository.getById(dto.getEmployeeId());
            if (employeeDetails==null) {
                throw new ResourceNotFoundException(ResumeUtils.EMPLOYEE_DETAILS_NOT_FOUND);
            }
            Set<Long> companyList = employeeDetails.getCompanyList();
            companyList.add(dto.getCompanyId());
            employeeDetails.setCompanyList(companyList);
            employeeDetails.setUpdatedDate(new Date());
            return employeeRepository.save(employeeDetails)!=null?true:false;
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @Override
    public List<ViewEmployeeCompanyDto> getEmployeeCompanies(EntityIdDto dto) {
        EmployeeDetails resumeDetails = employeeRepository.getById(dto.getEntityId());
        List<ViewEmployeeCompanyDto> responseList =new ArrayList<>();
        Set<Long> companyIdList = resumeDetails.getCompanyList();
        if (resumeDetails!=null && companyIdList!=null && !companyIdList.isEmpty()){
            AtomicInteger index = new AtomicInteger(0);
            for (Long companyId:companyIdList) {
                CompanyDetails companyDetails = companyService.getCompanyDetailsById(companyId);
                Date startDate = companyDetails.getStartDate()!=null?companyDetails.getStartDate():null;
                Date endDate = companyDetails.getEndDate()!=null?companyDetails.getEndDate():null;
                responseList.add(ViewEmployeeCompanyDto.builder()
                        .srNo(index.incrementAndGet())
                        .companyName(companyDetails.getCompanyName())
                        .companyId(companyDetails.getId())
                        .companyAddress(companyDetails.getCompanyAddress())
                        .startDate(startDate!=null?Constants.toExpenseDate(startDate):Constants.DASH)
                        .endDate(endDate!=null?Constants.toExpenseDate(endDate):Constants.DASH)
                        .experience(ResumeUtils.setExperience(startDate,endDate))
                        .build());
            }
        }
        return responseList;
    }

    @Override
    public List<EntityIdAndValueDto> getEmployeeDropDown() {
        List<Object[]> fetchList = employeeRepository.getEmployeeDropDown();
        return fetchList!=null && !fetchList.isEmpty()?fetchList.stream()
                .map(data -> EntityIdAndValueDto.builder()
                        .entityId(data[0]!=null?Long.parseLong(data[0].toString()):null)
                        .entityValue(data[1]!=null?data[1].toString():Constants.DASH)
                        .build()).collect(Collectors.toList()):new ArrayList<>();
    }

    @Override
    public List<EntityIdAndValueDto> getEmployeeCompanyDropDown(EntityIdDto dto) {
        List<Long> fetchList = employeeRepository.getEmployeeCompanies(dto.getEntityId());
        return fetchList!=null && !fetchList.isEmpty()?fetchList.stream()
                .map(data -> EntityIdAndValueDto.builder()
                        .entityId(data)
                        .entityValue(companyService.getCompanyName(data))
                        .build())
                .collect(Collectors.toList()):new ArrayList<>();
    }

    private void isMobileExists(Long mobile) {
        if (employeeRepository.existsEmployeeDetailsByEmployeeMobile(mobile)) {
            throw new AlreadyExistsException(ResumeUtils.EMPLOYEE_DETAILS_ALREADY_WITH_MOBILE_EXISTS);
        }
    }
    private void isEmailExists(String email) {
        if (employeeRepository.existsEmployeeDetailsByEmployeeEmail(email)) {
            throw new AlreadyExistsException(ResumeUtils.EMPLOYEE_DETAILS_ALREADY_WITH_EMAIL_EXISTS);
        }
    }
}
