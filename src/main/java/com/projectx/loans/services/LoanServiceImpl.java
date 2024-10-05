package com.projectx.loans.services;

import com.projectx.common.exceptions.AlreadyExistsException;
import com.projectx.common.exceptions.ResourceNotFoundException;
import com.projectx.common.payloads.*;
import com.projectx.common.utils.Constants;
import com.projectx.common.utils.LoanUtils;
import com.projectx.loans.entities.EMIDetails;
import com.projectx.loans.entities.LoanDetails;
import com.projectx.loans.payloads.*;
import com.projectx.loans.repository.EMIDetailsRepository;
import com.projectx.loans.repository.LoanRepository;
import javax.transaction.Transactional;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class LoanServiceImpl implements LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private EMIDetailsRepository emiDetailsRepository;

    @Transactional
    @Override
    public Boolean insertOrUpdate(LoanDto dto) throws ResourceNotFoundException, AlreadyExistsException, ParseException {
        LoanDetails loanDetails = null;
        if (dto.getId()==null) {
                isLoanExists(dto.getBankName(), dto.getLoanType());
                loanDetails = LoanDetails.builder()
                        .loanType(dto.getLoanType()!=null?dto.getLoanType():null)
                        .bankName(dto.getBankName()!=null?dto.getBankName():null)
                        .loanAmount(dto.getLoanAmount()!=null?dto.getLoanAmount():null)
                        .remainingAmount(dto.getLoanAmount()!=null?dto.getLoanAmount():null)
                        .emiDetails(new ArrayList<>())
                        .status(true)
                        .startDate(dto.getStartDate()!=null?Constants.getISODate(dto.getStartDate()):null)
                        .endDate(dto.getEndDate()!=null?Constants.getISODate(dto.getEndDate()):null)
                        .build();
        } else {
                loanDetails = loanRepository.getById(dto.getId());
                if (loanDetails==null){
                    throw new ResourceNotFoundException(LoanUtils.LOAN_DETAILS_NOT_FOUND);
                }
                if (!loanDetails.getEmiDetails().isEmpty()){
                    throw new AlreadyExistsException(LoanUtils.LOAN_AMOUNT_CANNOT_BE_CHANGE);
                }
                if (!dto.getBankName().equals(loanDetails.getBankName())){
                    loanDetails.setBankName(dto.getBankName());
                }
                if (!dto.getLoanType().equals(loanDetails.getLoanType())){
                    loanDetails.setLoanType(dto.getLoanType());
                }
                if (!dto.getLoanAmount().equals(loanDetails.getLoanAmount())){
                    loanDetails.setLoanAmount(dto.getLoanAmount());
                    loanDetails.setRemainingAmount(dto.getLoanAmount());
                }
                if (!dto.getStartDate().equals(Constants.toExpenseDate(loanDetails.getStartDate()))){
                    loanDetails.setStartDate(Constants.getISODate(dto.getStartDate()));
                }
                if (!dto.getEndDate().equals(Constants.toExpenseDate(loanDetails.getEndDate()))) {
                    loanDetails.setEndDate(Constants.getISODate(dto.getEndDate()));
                }
        }
        try {
            return loanDetails!=null && loanRepository.save(loanDetails)!=null?true:false;
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (AlreadyExistsException e){
            throw new ResourceNotFoundException(e.getMessage());
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Transactional
    @Override
    public Boolean addLoanEMI(EMIDto dto) throws ResourceNotFoundException,
            AlreadyExistsException {
        try {
             LoanDetails loanDetails = loanRepository.getById(dto.getLoanId());
             if (loanDetails==null){
                 throw new ResourceNotFoundException(LoanUtils.LOAN_DETAILS_NOT_FOUND);
             }
             List<EMIDetails> emiDetailsList = loanDetails.getEmiDetails()!=null && !loanDetails.getEmiDetails().isEmpty()
                     ?loanDetails.getEmiDetails():new ArrayList<>();
             if (dto.getEmiId()==null) {//Insert Operation
                    isEMIExists(dto.getLoanId());
                    EMIDetails emiDetails = EMIDetails.builder()
                            .emiAmount(dto.getEmiAmount())
                            .paymentMode(dto.getPaymentMode())
                            .emiDate(dto.getEmiDate()!=null?Constants.getISODate(dto.getEmiDate()):new Date())
                            .loanDetails(loanDetails)
                            .build();
                    updateRemainingAmount(loanDetails.getId(),dto.getEmiAmount());
                    emiDetailsList.add(emiDetailsRepository.save(emiDetails));
             } else {// Update Operation
                    EMIDetails emiDetails = emiDetailsRepository.getByEMIIdAndLoanId(dto.getEmiId(),dto.getLoanId());
                    if (emiDetails==null) {
                        throw new ResourceNotFoundException(LoanUtils.LOAN_EMI_DETAILS_NOT_FOUND);
                    }
                    if (!dto.getEmiAmount().equals(emiDetails.getEmiAmount())) {
                        emiDetails.setEmiAmount(dto.getEmiAmount());
                    }
                    if (!dto.getPaymentMode().equals(emiDetails.getPaymentMode())) {
                        emiDetails.setPaymentMode(dto.getPaymentMode());
                    }
                    emiDetails.setEmiDate(new Date());
                    emiDetailsList.add(emiDetailsRepository.save(emiDetails));
             }
             if (!emiDetailsList.isEmpty()) {
                 loanDetails.setEmiDetails(emiDetailsList);
             }
            return loanDetails.getEmiDetails()!=null && !loanDetails.getEmiDetails().isEmpty() && loanRepository.save(loanDetails)!=null?true:false;
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (RuntimeException | ParseException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public LoanDto getById(EntityIdDto dto) throws ResourceNotFoundException {
        try {
            LoanDetails details = loanRepository.getById(dto.getEntityId());
            if (details==null) {
                throw new ResourceNotFoundException(LoanUtils.LOAN_DETAILS_NOT_FOUND);
            }
            return LoanDto.builder()
                    .id(details.getId())
                    .bankName(details.getBankName()!=null?details.getBankName():null)
                    .loanType(details.getLoanType()!=null?details.getLoanType():null)
                    .loanAmount(details.getLoanAmount()!=null?details.getLoanAmount():null)
                    .startDate(details.getStartDate()!=null?Constants.toExpenseDate(details.getStartDate()):null)
                    .endDate(details.getEndDate()!=null?Constants.toExpenseDate(details.getEndDate()):null)
                    .build();
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @Override
    public EMIDto getEMIById(EntityIdDto dto) throws ResourceNotFoundException {
        try {
            EMIDetails details = emiDetailsRepository.getById(dto.getEntityId());
            if (details==null) {
                throw new ResourceNotFoundException(LoanUtils.LOAN_EMI_DETAILS_NOT_FOUND);
            }
            return EMIDto.builder()
                    .emiId(details.getId())
                    .loanId(details.getLoanDetails().getId())
                    .emiAmount(details.getEmiAmount()!=null?details.getEmiAmount():null)
                    .paymentMode(details.getPaymentMode()!=null?details.getPaymentMode():null)
                    .emiDate(details.getEmiDate()!=null?Constants.toExpenseDate(details.getEmiDate()):null)
                    .build();
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @Override
    public List<EntityTypeAndValueDto> getLoanTypes() {
        List<EntityTypeAndValueDto> responseList = new ArrayList<>();
        responseList.add(new EntityTypeAndValueDto(LoanUtils.PERSONAL_LOAN_TYPE,LoanUtils.PERSONAL_LOAN));
        responseList.add(new EntityTypeAndValueDto(LoanUtils.HOME_LOAN_TYPE,LoanUtils.HOME_LOAN));
        responseList.add(new EntityTypeAndValueDto(LoanUtils.CREDIT_CARD_LOAN_TYPE,LoanUtils.CREDIT_CARD));
        return responseList;
    }

    @Override
    public List<EntityNameAndValueDto> getPaymentModes() {
        List<EntityNameAndValueDto> responseList = new ArrayList<>();
        responseList.add(new EntityNameAndValueDto(Constants.PAYTM,Constants.PAYTM));
        responseList.add(new EntityNameAndValueDto(Constants.PHONE_PAY,Constants.PHONE_PAY));
        responseList.add(new EntityNameAndValueDto(Constants.GOOGLE_PAY,Constants.GOOGLE_PAY));
        responseList.add(new EntityNameAndValueDto(Constants.AUTO_DEBITED,Constants.AUTO_DEBITED));
        return responseList;
    }

    @Transactional
    @Override
    public Boolean updateStatus(EntityIdDto dto) throws ResourceNotFoundException {
        try {
            LoanDetails details = loanRepository.getById(dto.getEntityId());
            if (details==null) {
                throw new ResourceNotFoundException(LoanUtils.LOAN_DETAILS_NOT_FOUND);
            }
            Boolean status = details.getStatus()!=null && details.getStatus()?false:true;
            Integer count = loanRepository.updateStatus(details.getId(),status);
            return count!=null && count==1?true:false;
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @Transactional
    @Override
    public Boolean updateRemainingAmount(Long loanId, Double amount)
            throws ResourceNotFoundException, AlreadyExistsException {
        try {
            Double remainingAmount = loanRepository.getRemainingAmount(loanId);
            if (remainingAmount==null || remainingAmount==0) {
                throw new ResourceNotFoundException(LoanUtils.LOAN_DETAILS_NOT_FOUND);
            }
            if (amount>remainingAmount) {
                throw new AlreadyExistsException(LoanUtils.LOAN_ALREADY_CLOSED);
            }
            Double finalAmount = (remainingAmount-amount);
            Integer count = loanRepository.updateRemainingAmount(loanId,finalAmount);
            return count!=null && count==1?true:false;
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (AlreadyExistsException e) {
            throw new AlreadyExistsException(e.getMessage());
        }
    }

    @Override
    public LoanPageResponseDto getAllLoans(PageRequestDto dto) {
        String sortParameter = "";
        if (dto.getSortParam()!=null && dto.getSortParam().equals("srNo")) {
            sortParameter = "start_date";
        } else if (dto.getSortParam()!=null && dto.getSortParam().equals("bankName")) {
            sortParameter = "bank_name";
        } else if (dto.getSortParam()!=null && dto.getSortParam().equals("loanType")) {
            sortParameter = "loan_type";
        } else if (dto.getSortParam()!=null && dto.getSortParam().equals("loanAmount")) {
            sortParameter = "loan_amount";
        } else if (dto.getSortParam()!=null && dto.getSortParam().equals("remainingAmount")) {
            sortParameter = "remaining_amount";
        } else if (dto.getSortParam()!=null && dto.getSortParam().equals("startDate")) {
            sortParameter = "start_date";
        } else if (dto.getSortParam()!=null && dto.getSortParam().equals("endDate")) {
            sortParameter = "end_date";
        } else {
            sortParameter = "start_date";
        }
        Sort sort = dto.getSortDir().equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortParameter).ascending()
                : Sort.by(sortParameter).descending();
        Pageable pageable = PageRequest.of(dto.getPageNumber()-1, dto.getPageSize(), sort);
        Page<LoanDetails> loans = loanRepository.getAllLoanDetails(true,pageable);
        Integer pageNumber = dto.getPageNumber()-1;
        AtomicInteger index = new AtomicInteger(dto.getPageSize()*pageNumber);
        List<LoanDetails> listOfLoans = loans.getContent();
        List<ViewLoansDto> loanList = !listOfLoans.isEmpty()?listOfLoans.stream()
                .map(data -> ViewLoansDto.builder()
                        .srNo(index.incrementAndGet())
                        .loanId(data.getId())
                        .bankName(data.getBankName()!=null?data.getBankName():Constants.DASH)
                        .loanAmount(data.getLoanAmount()!=null?Constants.toINRFormat(data.getLoanAmount()):Constants.DASH)
                        .remainingAmount(data.getRemainingAmount()!=null?Constants.toINRFormat(data.getRemainingAmount()):Constants.DASH)
                        .loanType(data.getLoanType()!=null?LoanUtils.setLoanType(data.getLoanType()):Constants.DASH)
                        .startDate(data.getStartDate()!=null?Constants.toExpenseDate(data.getStartDate()):Constants.DASH)
                        .endDate(data.getEndDate()!=null?Constants.toExpenseDate(data.getEndDate()):Constants.DASH)
                        .build()).collect(Collectors.toList())
                :new ArrayList<>();
        return !loanList.isEmpty()? LoanPageResponseDto.builder()
                .pageNo(loans.getNumber())
                .pageSize(loans.getSize())
                .totalPages(loans.getTotalPages())
                .totalElements(loans.getTotalElements())
                .content(loanList)
                .build():new LoanPageResponseDto();
    }

    @Override
    public EMIPageResponseDto getAllLoanEMISByLoanId(EntityIdWithPageRequestDto dto) {
        String sortParameter = "";
        if (dto.getSortParam()!=null && dto.getSortParam().equals("srNo")) {
            sortParameter = "emi_date";
        } else if (dto.getSortParam()!=null && dto.getSortParam().equals("emiDate")) {
            sortParameter = "emi_date";
        } else if (dto.getSortParam()!=null && dto.getSortParam().equals("emiAmount")) {
            sortParameter = "emi_amount";
        } else if (dto.getSortParam()!=null && dto.getSortParam().equals("paymentMode")) {
            sortParameter = "payment_mode";
        } else {
            sortParameter = "emi_date";
        }
        Sort sort = dto.getSortDir().equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortParameter).ascending()
                : Sort.by(sortParameter).descending();
        Pageable pageable = PageRequest.of(dto.getPageNumber()-1, dto.getPageSize(), sort);
        Page<EMIDetails> emiDetails = emiDetailsRepository.getAllEMIByLoanId(dto.getEntityId(),pageable);
        Integer pageNumber = dto.getPageNumber()-1;
        AtomicInteger index = new AtomicInteger(dto.getPageSize()*pageNumber);
        List<EMIDetails> listOfEMIS = emiDetails.getContent();
        List<ViewEMIsDto> emiList = !listOfEMIS.isEmpty()?listOfEMIS.stream()
                .map(data -> ViewEMIsDto.builder()
                        .srNo(index.incrementAndGet())
                        .loanId(data.getLoanDetails().getId())
                        .emiId(data.getId())
                        .emiAmount(data.getEmiAmount()!=null?Constants.toINRFormat(data.getEmiAmount()):Constants.DASH)
                        .emiDate(data.getEmiDate()!=null?Constants.toExpenseDate(data.getEmiDate()):Constants.DASH)
                        .paymentMode(data.getPaymentMode()!=null?data.getPaymentMode():Constants.DASH)
                        .build()).collect(Collectors.toList())
                :new ArrayList<>();
        return !emiList.isEmpty()? EMIPageResponseDto.builder()
                .pageNo(emiDetails.getNumber())
                .pageSize(emiDetails.getSize())
                .totalPages(emiDetails.getTotalPages())
                .totalElements(emiDetails.getTotalElements())
                .content(emiList)
                .build():new EMIPageResponseDto();
    }

    private void isLoanExists(String bankName,Integer type){
        if (loanRepository.existsLoanDetailsByBankNameAndLoanType(bankName,type)){
            throw new AlreadyExistsException(LoanUtils.LOAN_ALREADY_PRESENT);
        }
    }
    private void isEMIExists(Long loanId) {
        Date startDate = Constants.firstDayOfMonth();
        Date enDate = Constants.lastDayOfMonth();
        Integer count = emiDetailsRepository.isEMIExists(loanId,startDate,enDate);
        if (count>0){
            throw new AlreadyExistsException(LoanUtils.LOAN_EMI_ALREADY_PRESENT);
        }
    }
}
