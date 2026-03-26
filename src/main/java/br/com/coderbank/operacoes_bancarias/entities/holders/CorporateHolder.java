package br.com.coderbank.operacoes_bancarias.entities.holders;

import java.util.Objects;

public class CorporateHolder extends Holder {

private final String cnpj;

    public CorporateHolder(String name, String address,  String email, String cnpj) {
        super(name, address, email);
        this.cnpj = Objects.requireNonNull(cnpj, "CNPJ cannot be null");
    }

    public String getCnpj() {
        return cnpj;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CorporateHolder that = (CorporateHolder) o;
        return Objects.equals(cnpj, that.cnpj);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(cnpj);
    }

    @Override
    public String toString() {
        return "CorporateHolder{" +
                "cnpj='" + cnpj + '\'' +
                '}';
    }
}
