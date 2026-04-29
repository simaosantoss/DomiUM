package domus.domain.factories;

import domus.domain.devices.DesumidificadorInteligente;
import domus.domain.devices.Dispositivo;

/**
 * Factory responsável pela criação de desumidificadores inteligentes.
 */
public class DesumidificadorInteligenteFactory implements DispositivoFactory {

    /**
     * Cria um novo desumidificador inteligente com valores por defeito coerentes
     * para os atributos específicos deste tipo de dispositivo.
     *
     * @param id identificador do dispositivo
     * @param marca marca do dispositivo
     * @param modelo modelo do dispositivo
     * @param consumo consumo base por hora do dispositivo
     * @return novo desumidificador inteligente
     */
    @Override
    public Dispositivo criarDispositivo(String id, String marca, String modelo, double consumo) {
        return new DesumidificadorInteligente(id, marca, modelo, consumo, 50.0);
    }
}
