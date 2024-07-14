package com.projectx.daily_expenses.respositoriestests;

import com.projectx.daily_expenses.commonUtils.ExpenseUtils;
import com.projectx.daily_expenses.commons.Constants;
import com.projectx.daily_expenses.entities.ExpenseItems;
import com.projectx.daily_expenses.entities.ExpensesDetails;
import com.projectx.daily_expenses.repositories.ExpensesRepository;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class ExpensesRepositoryTest {

    @Autowired
    private ExpensesRepository expensesRepository;

    @BeforeEach
    public void clearData() {
        expensesRepository.deleteAll();
    }

    @Test
    public void getExpensesByIdTest() {
        ExpensesDetails details = expensesRepository.save(ExpenseUtils.toCreateExpenses());
        assertEquals(ExpenseUtils.toCreateExpenses().getTotalAmount(),details.getTotalAmount());
        assertEquals(ExpenseUtils.toCreateExpenses().getStatus(),details.getStatus());
    }

    @Test
    public void expenseExistsTest() {
        ExpensesDetails details = expensesRepository.save(ExpenseUtils.toCreateExpenses());
        Integer count = expensesRepository.expenseExists(Constants.atStartOfDay(),Constants.atEndOfDay());
        assertEquals(1,count);
    }

    @Test
    public void existsByExpenseNameTest() {
        ExpensesDetails details = expensesRepository.save(ExpenseUtils.toCreateExpenses());
        Integer count = expensesRepository.existsByExpenseName(details.getId(),"Milk");
        assertEquals(1,count);
    }

    @Test
    public void updateStatusTest() {
        ExpensesDetails details = expensesRepository.save(ExpenseUtils.toCreateExpenses());
        Integer status = expensesRepository.updateStatus(details.getId(),false);
        assertEquals(status,1);
    }

    @Test
    public void getMonthAllExpensesTest() {
        ExpensesDetails details = expensesRepository.save(ExpenseUtils.toCreateExpenses());
        ExpensesDetails details1 = expensesRepository.save(ExpenseUtils.toCreateExpensesTwo());
        List<ExpensesDetails> fetchList = expensesRepository.getMonthAllExpenses(Constants.firstDayOfMonth(),Constants.lastDayOfMonth());
        ExpensesDetails first = fetchList.get(0);
        ExpensesDetails second = fetchList.get(1);
        assertEquals(first.getId()!=null?first.getId():null,details.getId()!=null?details.getId():null);
        assertEquals(first.getTotalAmount()!=null?first.getTotalAmount():0.0,details.getTotalAmount()!=null?details.getTotalAmount():0.0);
        assertEquals(second.getId()!=null?second.getId():null,details1.getId()!=null?details1.getId():null);
        assertEquals(second.getTotalAmount()!=null?second.getTotalAmount():0.0,details1.getTotalAmount()!=null?details1.getTotalAmount():0.0);
    }

    @Test
    public void getAllExpensesTest() {
        ExpensesDetails details = expensesRepository.save(ExpenseUtils.toCreateExpenses());
        ExpensesDetails details1 = expensesRepository.save(ExpenseUtils.toCreateExpensesTwo());
        List<ExpensesDetails> fetchList = expensesRepository.getAllExpenses();
        ExpensesDetails first = fetchList.get(0);
        ExpensesDetails second = fetchList.get(1);
        assertEquals(first.getId()!=null?first.getId():null,details.getId()!=null?details.getId():null);
        assertEquals(first.getTotalAmount()!=null?first.getTotalAmount():0.0,details.getTotalAmount()!=null?details.getTotalAmount():0.0);
        assertEquals(second.getId()!=null?second.getId():null,details1.getId()!=null?details1.getId():null);
        assertEquals(second.getTotalAmount()!=null?second.getTotalAmount():0.0,details1.getTotalAmount()!=null?details1.getTotalAmount():0.0);
    }

    @Test
    public void getExpenseItemsByExpenseIdTest() {
        ExpensesDetails details = expensesRepository.save(ExpenseUtils.toCreateExpenses());
        Set<ExpenseItems> items = details.getExpenseItems();
        List<Object[]> fetchList = expensesRepository.getExpenseItemsByExpenseId(details.getId());
        Object[] first = fetchList.get(0);
        Object[] second = fetchList.get(1);
        ExpenseItems item = items.stream().toList().get(0);
        ExpenseItems item1 = items.stream().toList().get(1);
        assertEquals(first[0]!=null?first[0].toString():"-",item.getItemName()!=null?item.getItemName():"-");
        assertEquals(first[1]!=null?Double.parseDouble(first[1].toString()):0.0,item.getItemPrice()!=null?item.getItemPrice():0.0);
        assertEquals(first[2]!=null?first[2].toString():"-",item.getPaymentType()!=null?item.getPaymentType():"-");
        assertEquals(second[0]!=null?second[0].toString():"-",item1.getItemName()!=null?item1.getItemName():"-");
        assertEquals(second[1]!=null?Double.parseDouble(second[1].toString()):0.0,item1.getItemPrice()!=null?item1.getItemPrice():0.0);
        assertEquals(second[2]!=null?second[2].toString():"-",item1.getPaymentType()!=null?item1.getPaymentType():"-");
    }
}
