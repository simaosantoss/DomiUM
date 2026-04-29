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
}
