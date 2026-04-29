package domus.domain.factories;

import domus.domain.devices.Dispositivo;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Mantém o registo dos tipos de dispositivos suportados pelo sistema.
 *
 * O registry associa um identificador textual estável a uma factory capaz de
 * criar instâncias do tipo correspondente.
 */
public class DispositivoRegistry implements Serializable {

    /**
     * Factories registadas no sistema, indexadas pelo identificador do tipo.
     */
    private final Map<String, DispositivoFactory> factories;

    /**
     * Cria um registry vazio e regista automaticamente os tipos base já
     * suportados pelo sistema.
     */
    public DispositivoRegistry() {
        this.factories = new HashMap<String, DispositivoFactory>();
        registarTipo("lampada", new LampadaInteligenteFactory());
        registarTipo("cortina", new CortinaInteligenteFactory());
        registarTipo("coluna", new ColunaInteligenteFactory());
        registarTipo("arcondicionado", new ArCondicionadoInteligenteFactory());
        registarTipo("fechadura", new FechaduraInteligenteFactory());
        registarTipo("desumidificador", new DesumidificadorInteligenteFactory());
        registarTipo("portao", new PortaoGaragemInteligenteFactory());
    }

    /**
     * Regista uma factory para um determinado tipo textual de dispositivo.
     *
     * Se o tipo ou a factory forem {@code null}, o pedido é ignorado. Quando o
     * registo é efetuado, a factory é guardada por cópia lógica.
     *
     * @param tipo identificador textual do tipo
     * @param factory factory associada ao tipo
     */
    public void registarTipo(String tipo, DispositivoFactory factory) {
        if (tipo != null && factory != null) {
            this.factories.put(tipo, factory.clone());
        }
    }

    /**
     * Cria um dispositivo do tipo indicado, usando a factory registada.
     *
     * @param tipo identificador textual do tipo
     * @param id identificador do dispositivo
     * @param marca marca do dispositivo
     * @param modelo modelo do dispositivo
     * @param consumo consumo base por hora do dispositivo
     * @return dispositivo criado, ou {@code null} se o tipo não existir
     */
    public Dispositivo criar(String tipo, String id, String marca, String modelo, double consumo) {
        DispositivoFactory factory = this.factories.get(tipo);
        if (factory == null) {
            return null;
        }
        return factory.criarDispositivo(id, marca, modelo, consumo);
    }

    /**
     * Compara este registry com outro objeto com base no seu estado relevante.
     *
     * @param o objeto a comparar
     * @return {@code true} se ambos representarem o mesmo registry lógico
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        DispositivoRegistry that = (DispositivoRegistry) o;
        return Objects.equals(this.factories, that.factories);
    }

    /**
     * Calcula um código de dispersão coerente com o estado comparado em
     * {@code equals()}.
     *
     * @return código de dispersão do registry
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.factories);
    }

    /**
     * Produz uma representação textual legível do registry.
     *
     * @return texto descritivo do registry
     */
    @Override
    public String toString() {
        return "DispositivoRegistry{"
                + "factories=" + this.factories
                + '}';
    }

    /**
     * Cria uma cópia profunda deste registry.
     *
     * As factories são copiadas individualmente para garantir independência
     * entre o objeto original e a cópia.
     *
     * @return novo registry com o mesmo estado lógico
     */
    public DispositivoRegistry clone() {
        DispositivoRegistry copia = new DispositivoRegistry();
        copia.factories.clear();

        for (Map.Entry<String, DispositivoFactory> entry : this.factories.entrySet()) {
            copia.factories.put(entry.getKey(), entry.getValue().clone());
        }

        return copia;
    }
}
