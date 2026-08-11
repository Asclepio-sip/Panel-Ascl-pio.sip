package Asclepio.Pedido.Enum;

public enum FormaDePagamento {

    DINHEIRO("Dinheiro"),
    PIX("Pix"),
    CARTAO_CREDITO("Cartão de Crédito"),
    CARTAO_DEBITO("Cartão de Débito"),
    BOLETO("Boleto Bancário"),
    VALE_REFEICAO("Vale-Refeição"),
    VALE_ALIMENTACAO("Vale-Alimentação"),
//    PICPAY("PicPay"),
//    MERCADO_PAGO("Mercado Pago"),
//    PAYPAL("PayPal"),
//    GOOGLE_PAY("Google Pay"),
//    APPLE_PAY("Apple Pay"),
//    TRANSFERENCIA_BANCARIA("Transferência Bancária"),
//    CREDIARIO("Crediário"),
    CHEQUE("Cheque"),
    OUTRO("Outro");

    private final String descricao;

    FormaDePagamento(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}