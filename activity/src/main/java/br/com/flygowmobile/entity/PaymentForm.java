package br.com.flygowmobile.entity;


import br.com.flygowmobile.database.RepositoryPaymentForm;

public class PaymentForm {

    public static String[] columns = new String[] {
            RepositoryPaymentForm.PaymentForms.COLUMN_NAME_PAYMENT_ID,
            RepositoryPaymentForm.PaymentForms.COLUMN_NAME_NAME,
            RepositoryPaymentForm.PaymentForms.COLUMN_NAME_DESCRIPTION
    };

    private int paymentFormId;
    private String name;
    private String description;

    public PaymentForm() {}

    public PaymentForm(int paymentFormId, String name, String description) {
        this.paymentFormId = paymentFormId;
        this.name = name;
        this.description = description;
    }

    public int getPaymentFormId() {
        return paymentFormId;
    }

    public void setPaymentFormId(int paymentFormId) {
        this.paymentFormId = paymentFormId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String toJSON() {
        return "{" +
                "\"paymentFormId\": "+ getPaymentFormId() + ", " +
                "\"name\": " + "\"" + getName() + "\", " +
                "\"description\": " + "\"" + getDescription() + "\"" +
                "}";
    }
}
