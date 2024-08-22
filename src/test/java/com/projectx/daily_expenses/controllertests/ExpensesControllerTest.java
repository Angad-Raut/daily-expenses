package com.projectx.daily_expenses.controllertests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectx.common.payloads.EntityIdDto;
import com.projectx.daily_expenses.commonUtils.ExpenseUtils;
import com.projectx.daily_expenses.controllers.ExpenseController;
import com.projectx.daily_expenses.dtos.ExpenseDto;
import com.projectx.daily_expenses.services.ExpenseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({ RestDocumentationExtension.class})
@WebMvcTest(ExpenseController.class)
@AutoConfigureRestDocs
public class ExpensesControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private ExpenseService expenseService;

    public static String toJson(Object object) throws JsonProcessingException {
        return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(object);
    }

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider restDocumentation) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    public void create_expense_test() throws Exception {
        ExpenseDto expenseDto = ExpenseUtils.toExpenseDto();
        expenseDto.setId(null);
        when(expenseService.createExpense(expenseDto)).thenReturn(true);
        mockMvc.perform(post("/api/expenses/createExpense")
                        .content(toJson(expenseDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document("{class-name}/{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    public void update_expense_test() throws Exception {
        when(expenseService.updateExpense(ExpenseUtils.toExpenseDto())).thenReturn(true);
        mockMvc.perform(post("/api/expenses/updateExpense")
                        .content(toJson(ExpenseUtils.toExpenseDto()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{class-name}/{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    public void get_expense_by_id_test() throws Exception {
        EntityIdDto entityIdDto = EntityIdDto.builder().entityId(1L).build();
        when(expenseService.getById(entityIdDto)).thenReturn(ExpenseUtils.toCreateExpenses());
        mockMvc.perform(post("/api/expenses/getExpenseById")
                        .content(toJson(entityIdDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{class-name}/{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    public void update_expense_status_by_id_test() throws Exception {
        EntityIdDto entityIdDto = EntityIdDto.builder().entityId(1L).build();
        when(expenseService.updateStatus(entityIdDto)).thenReturn(true);
        mockMvc.perform(post("/api/expenses/updateExpenseStatusById")
                        .content(toJson(entityIdDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{class-name}/{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    public void get_all_expenses_of_month_test() throws Exception {
        when(expenseService.getAllExpensesOfMonths()).thenReturn(ExpenseUtils.viewExpensesList());
        mockMvc.perform(get("/api/expenses/getAllExpensesOfMonth")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{class-name}/{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    public void get_all_expenses_test() throws Exception {
        when(expenseService.getAllExpenses()).thenReturn(ExpenseUtils.viewExpensesList());
        mockMvc.perform(get("/api/expenses/getAllExpenses")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{class-name}/{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }

    @Test
    public void get_expense_items_by_expense_id_test() throws Exception {
        EntityIdDto entityIdDto = EntityIdDto.builder().entityId(1L).build();
        when(expenseService.getExpensesItemsByExpenseId(entityIdDto)).thenReturn(ExpenseUtils.toExpenseItemsList());
        mockMvc.perform(post("/api/expenses/getExpenseItemsByExpenseId")
                        .content(toJson(entityIdDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("{class-name}/{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())));
    }
}
