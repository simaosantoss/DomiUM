package domus.domain.time;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

/**
 * Representa o relógio simulado do sistema.
 *
 * O relógio mantém uma data e hora internas e permite avançar o tempo em
 * minutos, servindo de referência para escalonamentos do domínio.
 */
public class RelogioSistema implements Serializable {

    /**
     * Data e hora atuais do relógio simulado.
     */
    private LocalDateTime dataHoraAtual;

    /**
     * Cria um relógio iniciado na data e hora atuais do sistema.
     */
    public RelogioSistema() {
        this(LocalDateTime.now());
    }

    /**
     * Cria um relógio iniciado na data e hora indicadas.
     *
     * @param dataHoraAtual data e hora iniciais
     */
    public RelogioSistema(LocalDateTime dataHoraAtual) {
        if (dataHoraAtual == null) {
            this.dataHoraAtual = LocalDateTime.now();
        } else {
            this.dataHoraAtual = dataHoraAtual;
        }
    }

    /**
     * Dá acesso à data e hora atuais.
     *
     * @return data e hora atuais do relógio
     */
    public LocalDateTime getDataHoraAtual() {
        return this.dataHoraAtual;
    }

    /**
     * Dá acesso apenas à hora atual.
     *
     * @return hora atual do relógio
     */
    public LocalTime getHoraAtual() {
        return this.dataHoraAtual.toLocalTime();
    }

    /**
     * Avança o relógio no número de minutos indicado.
     *
     * Se o valor indicado não for positivo, a operação é ignorada.
     *
     * @param minutos minutos a avançar
     */
    public void avancarTempo(int minutos) {
        if (minutos > 0) {
            this.dataHoraAtual = this.dataHoraAtual.plusMinutes(minutos);
        }
    }

    /**
     * Compara este relógio com outro objeto com base no seu estado.
     *
     * @param o objeto a comparar
     * @return {@code true} se ambos representarem o mesmo relógio lógico
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        RelogioSistema that = (RelogioSistema) o;
        return Objects.equals(this.dataHoraAtual, that.dataHoraAtual);
    }

    /**
     * Calcula um código de dispersão coerente com {@code equals()}.
     *
     * @return código de dispersão do relógio
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.dataHoraAtual);
    }

    /**
     * Produz uma representação textual legível do relógio.
     *
     * @return texto descritivo do relógio
     */
    @Override
    public String toString() {
        return "RelogioSistema{"
                + "dataHoraAtual=" + this.dataHoraAtual
                + '}';
    }

    /**
     * Cria uma cópia independente deste relógio.
     *
     * @return novo relógio com a mesma data e hora
     */
    public RelogioSistema clone() {
        return new RelogioSistema(this.dataHoraAtual);
    }
}
