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
}
