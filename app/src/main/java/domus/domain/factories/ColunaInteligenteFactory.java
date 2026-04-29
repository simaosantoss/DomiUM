package domus.domain.factories;

import domus.domain.devices.ColunaInteligente;
import domus.domain.devices.Dispositivo;

/**
 * Factory responsável pela criação de colunas inteligentes.
 */
public class ColunaInteligenteFactory implements DispositivoFactory {

    /**
     * Cria uma nova coluna inteligente com valores por defeito coerentes para
     * os atributos específicos deste tipo de dispositivo.
     *
     * @param id identificador do dispositivo
     * @param marca marca do dispositivo
     * @param modelo modelo do dispositivo
     * @param consumo consumo base por hora do dispositivo
     * @return nova coluna inteligente
     */
    @Override
    public Dispositivo criarDispositivo(String id, String marca, String modelo, double consumo) {
        return new ColunaInteligente(id, marca, modelo, consumo, 20);
    }

    /**
     * Compara esta factory com outro objeto.
     *
     * Como a factory não possui estado interno, duas instâncias da mesma classe
     * são consideradas equivalentes.
     *
     * @param o objeto a comparar
     * @return {@code true} se o objeto pertencer à mesma classe
     */
    @Override
    public boolean equals(Object o) {
        return this == o || (o != null && this.getClass() == o.getClass());
    }

    /**
     * Calcula um código de dispersão coerente com {@code equals()}.
     *
     * @return código de dispersão da factory
     */
    @Override
    public int hashCode() {
        return this.getClass().hashCode();
    }

    /**
     * Produz uma representação textual legível da factory.
     *
     * @return texto descritivo da factory
     */
    @Override
    public String toString() {
        return "ColunaInteligenteFactory{}";
    }

    /**
     * Cria uma cópia lógica desta factory.
     *
     * @return nova instância da mesma factory
     */
    @Override
    public ColunaInteligenteFactory clone() {
        return new ColunaInteligenteFactory();
    }
}
