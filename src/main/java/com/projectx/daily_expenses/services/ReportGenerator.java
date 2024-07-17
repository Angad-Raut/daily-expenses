package com.projectx.daily_expenses.services;

import com.projectx.daily_expenses.commons.ReportUtils;
import com.projectx.daily_expenses.dtos.ViewExpenseItemsDto;
import com.projectx.daily_expenses.dtos.ViewReportDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReportGenerator {

    public static byte[] generateReport(List<ViewReportDto> reportData, String startDate, String endDate) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("<html>");
            sb.append("<body>");
            sb.append("<center><h4><b>Expense Report</b></h4></center>");
            sb.append("<table border='1' style='border-collapse:collapse;width:100%;'>");
            sb.append("<tr>");
            sb.append("<td>");
            sb.append("<div style='width: 50%; float:left;'>");
            sb.append("<b>From Date :</b>"+startDate);
            sb.append("</div>");
            sb.append("<div style='width: 50%; float:right;'>");
            sb.append("<b align='center'>To Date :</b>"+endDate);
            sb.append("</div>");
            sb.append("</td>");
            sb.append("</tr>");
            sb.append("</table>");
            for (ViewReportDto reportDto:reportData){
                sb.append("<table border='1' style='border-collapse:collapse;width:100%;'>");
                sb.append("<tr>");
                sb.append("<td colspan='4' align='center'><b>All Expenses On "+reportDto.getExpenseDate()+"</b></td>");
                sb.append("</tr>");
                sb.append("<tr>");
                sb.append("<td colspan='4'>");
                   sb.append("<table border='1' style='border-collapse:collapse;width:100%;'>");
                       sb.append("<tr>");
                       sb.append("<th>SrNo</th>");
                       sb.append("<th>Item Name</th>");
                       sb.append("<th>Payment Mode</th>");
                       sb.append("<th>Item Price</th>");
                       sb.append("</tr>");
                       for (ViewExpenseItemsDto itemsDto:reportDto.getItemsList()) {
                           sb.append("<tr>");
                           sb.append("<td>"+itemsDto.getSrNo()+"</td>");
                           sb.append("<td>"+itemsDto.getItemName()+"</td>");
                           sb.append("<td>"+itemsDto.getPaymentWith()+"</td>");
                           sb.append("<td>"+itemsDto.getItemPrice()+"</td>");
                           sb.append("</tr>");
                       }
                   sb.append("<tr>");
                       sb.append("<td></td>");
                       sb.append("<td></td>");
                       sb.append("<td colspan='3'><b>Total</b></td>");
                       sb.append("<td>"+reportDto.getTotalAmount()+"</td>");
                   sb.append("</tr>");
                   sb.append("</table>");
                sb.append("</td>");
                sb.append("</tr>");
                sb.append("</table><br><br>");
            }
            sb.append("<center><b>This is computer generated report.</b></center>");
            sb.append("</body>");
            sb.append("</html>");
            return ReportUtils.generatePdf(sb.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] generateReportByExpenseId(ViewReportDto reportDto) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("<html>");
            sb.append("<body>");
            sb.append("<center><h4><b>Expense Report</b></h4></center>");
            sb.append("<table border='1' style='border-collapse:collapse;width:100%;'>");
            sb.append("<tr>");
            sb.append("<td colspan='4' align='center'><b>All Expenses On "+reportDto.getExpenseDate()+"</b></td>");
            sb.append("</tr>");
            sb.append("<tr>");
            sb.append("<td colspan='4'>");
            sb.append("<table border='1' style='border-collapse:collapse;width:100%;'>");
            sb.append("<tr>");
            sb.append("<th>SrNo</th>");
            sb.append("<th>Item Name</th>");
            sb.append("<th>Payment Mode</th>");
            sb.append("<th>Item Price</th>");
            sb.append("</tr>");
            for (ViewExpenseItemsDto itemsDto:reportDto.getItemsList()) {
                sb.append("<tr>");
                sb.append("<td>"+itemsDto.getSrNo()+"</td>");
                sb.append("<td>"+itemsDto.getItemName()+"</td>");
                sb.append("<td>"+itemsDto.getPaymentWith()+"</td>");
                sb.append("<td>"+itemsDto.getItemPrice()+"</td>");
                sb.append("</tr>");
            }
            sb.append("<tr>");
            sb.append("<td></td>");
            sb.append("<td></td>");
            sb.append("<td colspan='3'><b>Total</b></td>");
            sb.append("<td>"+reportDto.getTotalAmount()+"</td>");
            sb.append("</tr>");
            sb.append("</table>");
            sb.append("</td>");
            sb.append("</tr>");
            sb.append("</table><br>");
            sb.append("<center><b>This is computer generated report.</b></center>");
            sb.append("</body>");
            sb.append("</html>");
            return ReportUtils.generatePdf(sb.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
