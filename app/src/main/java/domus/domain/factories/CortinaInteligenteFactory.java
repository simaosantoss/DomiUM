package domus.domain.factories;

import domus.domain.devices.CortinaInteligente;
import domus.domain.devices.Dispositivo;

/**
 * Factory responsável pela criação de cortinas inteligentes.
 */
public class CortinaInteligenteFactory implements DispositivoFactory {

    /**
     * Cria uma nova cortina inteligente com valores por defeito coerentes para
     * os atributos específicos deste tipo de dispositivo.
     *
     * @param id identificador do dispositivo
     * @param marca marca do dispositivo
     * @param modelo modelo do dispositivo
     * @param consumo consumo base por hora do dispositivo
     * @return nova cortina inteligente
     */
    @Override
    public Dispositivo criarDispositivo(String id, String marca, String modelo, double consumo) {
        return new CortinaInteligente(id, marca, modelo, consumo, 0);
    }
}
