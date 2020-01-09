package biz.belcorp.consultoras.common.model.debt;

/**
 *
 */
public class DebtResumeModel {

    private String clientName;
    private String consultantName;

    private String currentDebt;
    private String resiudalDebt;

    private String totalDebt;

    private String debtDescription;

    private String deadLine;

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getConsultantName() {
        return consultantName;
    }

    public void setConsultantName(String consultantName) {
        this.consultantName = consultantName;
    }

    public String getCurrentDebt() {
        return currentDebt;
    }

    public void setCurrentDebt(String currentDebt) {
        this.currentDebt = currentDebt;
    }

    public String getResiudalDebt() {
        return resiudalDebt;
    }

    public void setResiudalDebt(String resiudalDebt) {
        this.resiudalDebt = resiudalDebt;
    }

    public String getTotalDebt() {
        return totalDebt;
    }

    public void setTotalDebt(String totalDebt) {
        this.totalDebt = totalDebt;
    }

    public String getDebtDescription() {
        return debtDescription;
    }

    public void setDebtDescription(String debtDescription) {
        this.debtDescription = debtDescription;
    }

    public String getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(String deadLine) {
        this.deadLine = deadLine;
    }
}
