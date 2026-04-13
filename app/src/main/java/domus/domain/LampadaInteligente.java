package domus.domain;

/**
 * Representa uma lâmpada inteligente no sistema DomusControl.
 *
 * Esta classe especializa um dispositivo genérico com informação própria da
 * iluminação, nomeadamente a intensidade luminosa e a temperatura de cor.
 */
public class LampadaInteligente extends Dispositivo {

    /**
     * Intensidade luminosa atualmente definida para a lâmpada.
     */
    private int intensidade;

    /**
     * Temperatura de cor da lâmpada, expressa em Kelvin.
     */
    private int corK;

    /**
     * Cria uma nova lâmpada inteligente com os dados base do dispositivo e os
     * atributos específicos da iluminação.
     *
     * @param identificador identificador único da lâmpada
     * @param marca marca da lâmpada
     * @param modelo modelo da lâmpada
     * @param consumoPorHora consumo energético por hora
     * @param intensidade intensidade luminosa configurada
     * @param corK temperatura de cor em Kelvin
     */
    public LampadaInteligente(String identificador, String marca, String modelo,
                              double consumoPorHora, int intensidade, int corK) {
        super(identificador, marca, modelo, consumoPorHora);
        this.intensidade = intensidade;
        this.corK = corK;
    }

    /**
     * Cria uma nova lâmpada inteligente por cópia de outra já existente.
     *
     * O estado comum do dispositivo é copiado diretamente pela superclasse e os
     * atributos específicos da iluminação são preservados nesta classe.
     *
     * @param outra lâmpada a copiar
     */
    private LampadaInteligente(LampadaInteligente outra) {
        super(outra);
        this.intensidade = outra.intensidade;
        this.corK = outra.corK;
    }

    /**
     * Dá acesso à intensidade atual da lâmpada.
     *
     * @return intensidade luminosa configurada
     */
    public int getIntensidade() {
        return this.intensidade;
    }

    /**
     * Altera a intensidade luminosa da lâmpada.
     *
     * @param intensidade nova intensidade a guardar
     */
    public void setIntensidade(int intensidade) {
        this.intensidade = intensidade;
    }

    /**
     * Dá acesso à temperatura de cor da lâmpada.
     *
     * @return temperatura de cor em Kelvin
     */
    public int getCorK() {
        return this.corK;
    }

    /**
     * Altera a temperatura de cor da lâmpada.
     *
     * @param corK nova temperatura de cor em Kelvin
     */
    public void setCorK(int corK) {
        this.corK = corK;
    }

    /**
     * Calcula o consumo atual da lâmpada.
     *
     * Nesta fase do projeto, o consumo corresponde diretamente ao consumo base
     * por hora herdado da superclasse.
     *
     * @return consumo atual da lâmpada
     */
    @Override
    public double getConsumo() {
        return getConsumoPorHora();
    }

    /**
     * Cria uma cópia manual desta lâmpada inteligente.
     *
     * A nova instância é criada através do construtor de cópia da própria classe,
     * apoiado no construtor de cópia da superclasse para preservar o estado
     * herdado e os atributos específicos da lâmpada.
     *
     * @return nova lâmpada com o mesmo estado lógico
     */
    @Override
    public LampadaInteligente clone() {
        return new LampadaInteligente(this);
    }

    /**
     * Compara esta lâmpada com outro objeto com base no estado herdado e no
     * estado específico da classe.
     *
     * @param o objeto a comparar
     * @return {@code true} se ambos representarem a mesma lâmpada lógica
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
        LampadaInteligente that = (LampadaInteligente) o;
        return this.intensidade == that.intensidade
                && this.corK == that.corK;
    }

    /**
     * Produz uma representação textual legível da lâmpada, reutilizando a
     * informação textual da superclasse e acrescentando os atributos próprios.
     *
     * @return texto descritivo da lâmpada
     */
    @Override
    public String toString() {
        return "LampadaInteligente{"
                + getDescricaoEstadoBase()
                + ", intensidade=" + this.intensidade
                + ", corK=" + this.corK
                + '}';
    }
}
