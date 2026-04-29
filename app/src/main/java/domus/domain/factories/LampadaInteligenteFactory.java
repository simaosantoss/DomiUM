package domus.domain.factories;

import domus.domain.devices.Dispositivo;
import domus.domain.devices.LampadaInteligente;

/**
 * Factory responsável pela criação de lâmpadas inteligentes.
 */
public class LampadaInteligenteFactory implements DispositivoFactory {

    /**
     * Cria uma nova lâmpada inteligente com valores por defeito coerentes para
     * os atributos específicos deste tipo de dispositivo.
     *
     * @param id identificador do dispositivo
     * @param marca marca do dispositivo
     * @param modelo modelo do dispositivo
     * @param consumo consumo base por hora do dispositivo
     * @return nova lâmpada inteligente
     */
    @Override
    public Dispositivo criarDispositivo(String id, String marca, String modelo, double consumo) {
        return new LampadaInteligente(id, marca, modelo, consumo, 50, 3000);
    }
}
