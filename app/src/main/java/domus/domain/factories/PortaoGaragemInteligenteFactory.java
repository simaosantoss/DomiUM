package domus.domain.factories;

import domus.domain.devices.Dispositivo;
import domus.domain.devices.PortaoGaragemInteligente;

/**
 * Factory responsável pela criação de portões de garagem inteligentes.
 */
public class PortaoGaragemInteligenteFactory implements DispositivoFactory {

    /**
     * Cria um novo portão de garagem inteligente com os dados base fornecidos.
     *
     * @param id identificador do dispositivo
     * @param marca marca do dispositivo
     * @param modelo modelo do dispositivo
     * @param consumo consumo base por hora do dispositivo
     * @return novo portão de garagem inteligente
     */
    @Override
    public Dispositivo criarDispositivo(String id, String marca, String modelo, double consumo) {
        return new PortaoGaragemInteligente(id, marca, modelo, consumo);
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
        return "PortaoGaragemInteligenteFactory{}";
    }

    /**
     * Cria uma cópia lógica desta factory.
     *
     * @return nova instância da mesma factory
     */
    @Override
    public PortaoGaragemInteligenteFactory clone() {
        return new PortaoGaragemInteligenteFactory();
    }
}
