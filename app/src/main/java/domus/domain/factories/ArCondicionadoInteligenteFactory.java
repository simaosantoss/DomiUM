package domus.domain.factories;

import domus.domain.devices.ArCondicionadoInteligente;
import domus.domain.devices.Dispositivo;

/**
 * Factory responsável pela criação de aparelhos de ar condicionado inteligentes.
 */
public class ArCondicionadoInteligenteFactory implements DispositivoFactory {

    /**
     * Cria um novo ar condicionado inteligente com valores por defeito coerentes
     * para os atributos específicos deste tipo de dispositivo.
     *
     * @param id identificador do dispositivo
     * @param marca marca do dispositivo
     * @param modelo modelo do dispositivo
     * @param consumo consumo base por hora do dispositivo
     * @return novo ar condicionado inteligente
     */
    @Override
    public Dispositivo criarDispositivo(String id, String marca, String modelo, double consumo) {
        return new ArCondicionadoInteligente(id, marca, modelo, consumo, 22.0);
    }
}
