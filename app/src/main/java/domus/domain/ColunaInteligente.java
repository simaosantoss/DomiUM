package domus.domain;

import java.util.Objects;

/**
 * Representa uma coluna inteligente no sistema DomusControl.
 *
 * Esta classe especializa um dispositivo genérico com informação própria da
 * reprodução de áudio, como o volume e a playlist atual.
 */
public class ColunaInteligente extends Dispositivo {

    /**
     * Volume atualmente definido para a coluna.
     */
    private int volume;

    /**
     * Nome da playlist atualmente associada à coluna.
     */
    private String playlistAtual;

    /**
     * Cria uma nova coluna inteligente com os dados base do dispositivo e o
     * volume inicial.
     *
     * @param identificador identificador único da coluna
     * @param marca marca da coluna
     * @param modelo modelo da coluna
     * @param consumoPorHora consumo energético por hora
     * @param volume volume inicial da coluna
     */
    public ColunaInteligente(String identificador, String marca, String modelo,
                             double consumoPorHora, int volume) {
        super(identificador, marca, modelo, consumoPorHora);
        this.volume = volume;
        this.playlistAtual = null;
    }

    /**
     * Cria uma nova coluna inteligente por cópia de outra já existente.
     *
     * O estado comum do dispositivo é copiado diretamente pela superclasse e os
     * atributos próprios da reprodução de áudio são preservados nesta classe.
     *
     * @param outra coluna a copiar
     */
    private ColunaInteligente(ColunaInteligente outra) {
        super(outra);
        this.volume = outra.volume;
        this.playlistAtual = outra.playlistAtual;
    }

    /**
     * Dá acesso ao volume atual da coluna.
     *
     * @return volume configurado
     */
    public int getVolume() {
        return this.volume;
    }

    /**
     * Dá acesso à playlist atualmente associada à coluna.
     *
     * @return nome da playlist atual
     */
    public String getPlaylistAtual() {
        return this.playlistAtual;
    }

    /**
     * Altera o volume da coluna.
     *
     * @param valor novo volume a guardar
     */
    public void setVolume(int valor) {
        this.volume = valor;
    }

    /**
     * Altera a playlist atualmente associada à coluna.
     *
     * @param pl nova playlist a guardar
     */
    public void setPlaylistAtual(String pl) {
        this.playlistAtual = pl;
    }

    /**
     * Calcula o consumo atual da coluna.
     *
     * Nesta fase do projeto, o consumo corresponde diretamente ao consumo base
     * por hora herdado da superclasse.
     *
     * @return consumo atual da coluna
     */
    @Override
    public double getConsumo() {
        return getConsumoPorHora();
    }

    /**
     * Cria uma cópia manual desta coluna inteligente.
     *
     * A nova instância é criada através do construtor de cópia da própria classe,
     * apoiado no construtor de cópia da superclasse para preservar o estado
     * herdado e os atributos próprios da reprodução de áudio.
     *
     * @return nova coluna com o mesmo estado lógico
     */
    @Override
    public ColunaInteligente clone() {
        return new ColunaInteligente(this);
    }

    /**
     * Compara esta coluna com outro objeto com base no estado herdado e no
     * estado específico da classe.
     *
     * @param o objeto a comparar
     * @return {@code true} se ambos representarem a mesma coluna lógica
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        ColunaInteligente that = (ColunaInteligente) o;
        return this.volume == that.volume
                && Objects.equals(this.playlistAtual, that.playlistAtual);
    }

    /**
     * Calcula um código de dispersão coerente com o estado comparado em
     * {@code equals()}.
     *
     * @return código de dispersão da coluna
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.volume, this.playlistAtual);
    }

    /**
     * Produz uma representação textual legível da coluna, reutilizando a
     * informação textual da superclasse e acrescentando os atributos próprios.
     *
     * @return texto descritivo da coluna
     */
    @Override
    public String toString() {
        return "ColunaInteligente{"
                + getDescricaoEstadoBase()
                + ", volume=" + this.volume
                + ", playlistAtual='" + this.playlistAtual + '\''
                + '}';
    }
}
