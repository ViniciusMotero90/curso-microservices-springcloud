package io.github.cursodsousa.msvaliadorcredito.ex;

public class ErroComunicacaoMicroservicesException extends Exception{
    private Integer status;

    public ErroComunicacaoMicroservicesException(String msg, Integer status) {
        super(msg);
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }
}
