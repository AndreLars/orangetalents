package br.com.zup.orangetalents.repository.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "VACINACAO")
public class Vacinacao {

    @EmbeddedId
    private VacinacaoPk pk;

    public Vacinacao() {
    }

    public Vacinacao(VacinacaoPk pk) {
        setPk(pk);
    }

    public VacinacaoPk getPk() {
        return pk;
    }

    public void setPk(VacinacaoPk pk) {
        this.pk = pk;
    }
}
