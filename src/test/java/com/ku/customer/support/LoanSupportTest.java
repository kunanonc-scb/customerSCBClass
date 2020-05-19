package com.ku.customer.support;

import com.ku.customer.model.GetLoanInfoResponse;

public class LoanSupportTest {


    public static GetLoanInfoResponse getNewLoanInfo() {
        GetLoanInfoResponse loanInfo = new GetLoanInfoResponse();
        loanInfo.setId(1L);
        loanInfo.setAccountPayable("102-222-2200");
        loanInfo.setAccountReceivable("102-333-2200");
        loanInfo.setStatus("OK");
        loanInfo.setPrincipalAmount(3400000.0);
        return loanInfo;
    }
}
