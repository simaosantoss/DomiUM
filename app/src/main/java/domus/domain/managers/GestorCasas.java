package domus.domain.managers;

import domus.domain.automation.Automacao;
import domus.domain.commands.Command;
import domus.domain.conditions.Condicao;
import domus.domain.core.Casa;
import domus.domain.core.Divisao;
import domus.domain.devices.ArCondicionadoInteligente;
import domus.domain.devices.ColunaInteligente;
import domus.domain.devices.CortinaInteligente;
import domus.domain.devices.DesumidificadorInteligente;
import domus.domain.devices.Dispositivo;
import domus.domain.devices.FechaduraInteligente;
import domus.domain.devices.LampadaInteligente;
import domus.domain.devices.OperacaoDispositivo;
import domus.domain.devices.PortaoGaragemInteligente;
import domus.domain.environment.AmbienteInterior;
import domus.domain.factories.DispositivoRegistry;
import domus.domain.scheduling.Escalonamento;
import domus.domain.scenarios.Cenario;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Gere as casas registadas no domínio DomusControl.
 *
 * Este gestor centraliza a criação e atualização das casas, divisões,
 * dispositivos, cenários, automações e escalonamentos, mantendo as estruturas
 * internas protegidas por cópias defensivas.
 */
public class GestorCasas implements Serializable {

    /**
     * Casas registadas, indexadas pelo respetivo identificador.
     */
    private final Map<String, Casa> casas;

    /**
     * Registry responsável pela criação dos tipos de dispositivos suportados.
     */
    private final DispositivoRegistry dispositivoRegistry;

    /**
     * Cria um gestor de casas vazio.
     */
    public GestorCasas() {
        this.casas = new HashMap<String, Casa>();
        this.dispositivoRegistry = new DispositivoRegistry();
    }

    /**
     * Cria e regista uma nova casa.
     *
     * Se os dados forem inválidos ou já existir uma casa com o mesmo
     * identificador, a operação é ignorada.
     *
     * @param casaId identificador da casa
     * @param nome nome da casa
     */
    public void criarCasa(String casaId, String nome) {
        if (casaId == null || nome == null || this.casas.containsKey(casaId)) {
            return;
        }

        this.casas.put(casaId, new Casa(casaId, nome));
    }

    /**
     * Verifica se existe uma casa com o identificador indicado.
     *
     * @param casaId identificador da casa
     * @return {@code true} se a casa existir
     */
    public boolean existeCasa(String casaId) {
        return casaId != null && this.casas.containsKey(casaId);
    }

    /**
     * Obtém uma casa a partir do seu identificador.
     *
     * @param casaId identificador da casa
     * @return cópia da casa encontrada, ou {@code null} se não existir
     */
    public Casa getCasa(String casaId) {
        Casa casa = this.casas.get(casaId);
        if (casa == null) {
            return null;
        }
        return casa.clone();
    }

    /**
     * Disponibiliza um iterador sobre uma cópia protegida das casas.
     *
     * @return iterador sobre uma cópia das casas registadas
     */
    public Iterator<Casa> getIteradorCasas() {
        List<Casa> copia = new ArrayList<Casa>();
        for (Casa casa : this.casas.values()) {
            copia.add(casa.clone());
        }
        return Collections.unmodifiableList(copia).iterator();
    }

    /**
     * Adiciona uma divisão a uma casa.
     *
     * @param casaId identificador da casa
     * @param divisaoNome nome da divisão
     * @return {@code true} se a divisão tiver sido adicionada
     */
    public boolean adicionarDivisao(String casaId, String divisaoNome) {
        if (casaId == null || divisaoNome == null) {
            return false;
        }

        Casa casa = this.casas.get(casaId);
        if (casa == null) {
            return false;
        }

        Casa casaAtualizada = casa.clone();
        if (casaAtualizada.contemDivisao(divisaoNome)) {
            return false;
        }

        casaAtualizada.adicionarDivisao(new Divisao(divisaoNome));
        this.casas.put(casaId, casaAtualizada);
        return true;
    }

    /**
     * Adiciona um dispositivo a uma divisão de uma casa.
     *
     * @param casaId identificador da casa
     * @param divisaoNome nome da divisão de destino
     * @param tipo tipo textual do dispositivo
     * @param dispositivoId identificador do dispositivo
     * @param marca marca do dispositivo
     * @param modelo modelo do dispositivo
     * @param consumoPorHora consumo base por hora
     * @return {@code true} se o dispositivo tiver sido adicionado
     */
    public boolean adicionarDispositivo(String casaId, String divisaoNome, String tipo,
                                        String dispositivoId, String marca, String modelo,
                                        double consumoPorHora) {
        if (casaId == null || divisaoNome == null || tipo == null || dispositivoId == null
                || marca == null || modelo == null) {
            return false;
        }

        Casa casa = this.casas.get(casaId);
        if (casa == null) {
            return false;
        }

        Casa casaAtualizada = casa.clone();
        if (!casaAtualizada.contemDivisao(divisaoNome) || casaAtualizada.contemDispositivo(dispositivoId)) {
            return false;
        }

        Dispositivo dispositivo = this.dispositivoRegistry.criar(
                tipo, dispositivoId, marca, modelo, consumoPorHora
        );

        if (dispositivo == null) {
            return false;
        }

        if (casaAtualizada.adicionarDispositivoADivisao(divisaoNome, dispositivo)) {
            this.casas.put(casaId, casaAtualizada);
            return true;
        }

        return false;
    }

    /**
     * Liga um dispositivo numa casa.
     *
     * @param casaId identificador da casa
     * @param dispositivoId identificador do dispositivo
     * @return {@code true} se o dispositivo tiver sido ligado
     */
    public boolean ligarDispositivo(String casaId, String dispositivoId) {
        if (casaId == null || dispositivoId == null) {
            return false;
        }

        Casa casa = this.casas.get(casaId);
        if (casa == null) {
            return false;
        }

        Casa casaAtualizada = casa.clone();
        if (casaAtualizada.ligarDispositivo(dispositivoId)) {
            this.casas.put(casaId, casaAtualizada);
            return true;
        }

        return false;
    }

    /**
     * Desliga um dispositivo numa casa.
     *
     * @param casaId identificador da casa
     * @param dispositivoId identificador do dispositivo
     * @return {@code true} se o dispositivo tiver sido desligado
     */
    public boolean desligarDispositivo(String casaId, String dispositivoId) {
        if (casaId == null || dispositivoId == null) {
            return false;
        }

        Casa casa = this.casas.get(casaId);
        if (casa == null) {
            return false;
        }

        Casa casaAtualizada = casa.clone();
        if (casaAtualizada.desligarDispositivo(dispositivoId)) {
            this.casas.put(casaId, casaAtualizada);
            return true;
        }

        return false;
    }

    /**
     * Define a intensidade luminosa de uma lâmpada.
     *
     * @param casaId identificador da casa
     * @param dispositivoId identificador do dispositivo
     * @param intensidade nova intensidade luminosa
     * @return {@code true} se a operação tiver sido aplicada
     */
    public boolean definirIntensidadeLampada(String casaId, String dispositivoId, int intensidade) {
        return aplicarOperacaoEmCasa(casaId, dispositivoId, dispositivo -> {
            if (!(dispositivo instanceof LampadaInteligente)) {
                return false;
            }

            ((LampadaInteligente) dispositivo).setIntensidade(intensidade);
            return true;
        });
    }

    /**
     * Define a temperatura de cor de uma lâmpada.
     *
     * @param casaId identificador da casa
     * @param dispositivoId identificador do dispositivo
     * @param corK nova temperatura de cor, em Kelvin
     * @return {@code true} se a operação tiver sido aplicada
     */
    public boolean definirCorLampada(String casaId, String dispositivoId, int corK) {
        return aplicarOperacaoEmCasa(casaId, dispositivoId, dispositivo -> {
            if (!(dispositivo instanceof LampadaInteligente)) {
                return false;
            }

            ((LampadaInteligente) dispositivo).setCorK(corK);
            return true;
        });
    }

    /**
     * Define a percentagem de abertura de uma cortina.
     *
     * @param casaId identificador da casa
     * @param dispositivoId identificador do dispositivo
     * @param percentagemAbertura nova percentagem de abertura
     * @return {@code true} se a operação tiver sido aplicada
     */
    public boolean definirAberturaCortina(String casaId, String dispositivoId,
                                          int percentagemAbertura) {
        return aplicarOperacaoEmCasa(casaId, dispositivoId, dispositivo -> {
            if (!(dispositivo instanceof CortinaInteligente)) {
                return false;
            }

            ((CortinaInteligente) dispositivo).setPercentagemAbertura(percentagemAbertura);
            return true;
        });
    }

    /**
     * Define o volume de uma coluna.
     *
     * @param casaId identificador da casa
     * @param dispositivoId identificador do dispositivo
     * @param volume novo volume
     * @return {@code true} se a operação tiver sido aplicada
     */
    public boolean definirVolumeColuna(String casaId, String dispositivoId, int volume) {
        return aplicarOperacaoEmCasa(casaId, dispositivoId, dispositivo -> {
            if (!(dispositivo instanceof ColunaInteligente)) {
                return false;
            }

            ((ColunaInteligente) dispositivo).setVolume(volume);
            return true;
        });
    }

    /**
     * Define a playlist atual de uma coluna.
     *
     * @param casaId identificador da casa
     * @param dispositivoId identificador do dispositivo
     * @param playlist nova playlist atual
     * @return {@code true} se a operação tiver sido aplicada
     */
    public boolean definirPlaylistColuna(String casaId, String dispositivoId, String playlist) {
        return aplicarOperacaoEmCasa(casaId, dispositivoId, dispositivo -> {
            if (!(dispositivo instanceof ColunaInteligente)) {
                return false;
            }

            ((ColunaInteligente) dispositivo).setPlaylistAtual(playlist);
            return true;
        });
    }

    /**
     * Define a temperatura alvo de um ar condicionado.
     *
     * @param casaId identificador da casa
     * @param dispositivoId identificador do dispositivo
     * @param temperatura nova temperatura alvo
     * @return {@code true} se a operação tiver sido aplicada
     */
    public boolean definirTemperaturaArCondicionado(String casaId, String dispositivoId,
                                                    double temperatura) {
        return aplicarOperacaoEmCasa(casaId, dispositivoId, dispositivo -> {
            if (!(dispositivo instanceof ArCondicionadoInteligente)) {
                return false;
            }

            ((ArCondicionadoInteligente) dispositivo).setTemperaturaAlvo(temperatura);
            return true;
        });
    }

    /**
     * Define o modo de funcionamento de um ar condicionado.
     *
     * @param casaId identificador da casa
     * @param dispositivoId identificador do dispositivo
     * @param modo novo modo de funcionamento
     * @return {@code true} se a operação tiver sido aplicada
     */
    public boolean definirModoArCondicionado(String casaId, String dispositivoId, String modo) {
        return aplicarOperacaoEmCasa(casaId, dispositivoId, dispositivo -> {
            if (!(dispositivo instanceof ArCondicionadoInteligente)) {
                return false;
            }

            ((ArCondicionadoInteligente) dispositivo).setModo(modo);
            return true;
        });
    }

    /**
     * Tranca uma fechadura.
     *
     * @param casaId identificador da casa
     * @param dispositivoId identificador do dispositivo
     * @return {@code true} se a operação tiver sido aplicada
     */
    public boolean trancarFechadura(String casaId, String dispositivoId) {
        return aplicarOperacaoEmCasa(casaId, dispositivoId, dispositivo -> {
            if (!(dispositivo instanceof FechaduraInteligente)) {
                return false;
            }

            ((FechaduraInteligente) dispositivo).trancar();
            return true;
        });
    }

    /**
     * Destranca uma fechadura.
     *
     * @param casaId identificador da casa
     * @param dispositivoId identificador do dispositivo
     * @return {@code true} se a operação tiver sido aplicada
     */
    public boolean destrancarFechadura(String casaId, String dispositivoId) {
        return aplicarOperacaoEmCasa(casaId, dispositivoId, dispositivo -> {
            if (!(dispositivo instanceof FechaduraInteligente)) {
                return false;
            }

            ((FechaduraInteligente) dispositivo).destrancar();
            return true;
        });
    }

    /**
     * Define a humidade alvo de um desumidificador.
     *
     * @param casaId identificador da casa
     * @param dispositivoId identificador do dispositivo
     * @param humidade nova humidade alvo
     * @return {@code true} se a operação tiver sido aplicada
     */
    public boolean definirHumidadeDesumidificador(String casaId, String dispositivoId,
                                                  double humidade) {
        return aplicarOperacaoEmCasa(casaId, dispositivoId, dispositivo -> {
            if (!(dispositivo instanceof DesumidificadorInteligente)) {
                return false;
            }

            ((DesumidificadorInteligente) dispositivo).setHumidadeAlvo(humidade);
            return true;
        });
    }

    /**
     * Abre um portão de garagem.
     *
     * @param casaId identificador da casa
     * @param dispositivoId identificador do dispositivo
     * @return {@code true} se a operação tiver sido aplicada
     */
    public boolean abrirPortao(String casaId, String dispositivoId) {
        return aplicarOperacaoEmCasa(casaId, dispositivoId, dispositivo -> {
            if (!(dispositivo instanceof PortaoGaragemInteligente)) {
                return false;
            }

            ((PortaoGaragemInteligente) dispositivo).abrir();
            return true;
        });
    }

    /**
     * Fecha um portão de garagem.
     *
     * @param casaId identificador da casa
     * @param dispositivoId identificador do dispositivo
     * @return {@code true} se a operação tiver sido aplicada
     */
    public boolean fecharPortao(String casaId, String dispositivoId) {
        return aplicarOperacaoEmCasa(casaId, dispositivoId, dispositivo -> {
            if (!(dispositivo instanceof PortaoGaragemInteligente)) {
                return false;
            }

            ((PortaoGaragemInteligente) dispositivo).fechar();
            return true;
        });
    }

    /**
     * Cria um cenário numa casa.
     *
     * @param casaId identificador da casa
     * @param cenarioId identificador do cenário
     * @param nome nome do cenário
     * @return {@code true} se o cenário tiver sido criado
     */
    public boolean criarCenario(String casaId, String cenarioId, String nome) {
        if (casaId == null || cenarioId == null || nome == null) {
            return false;
        }

        Casa casa = this.casas.get(casaId);
        if (casa == null) {
            return false;
        }

        Casa casaAtualizada = casa.clone();
        if (casaAtualizada.getCenario(cenarioId) != null) {
            return false;
        }

        casaAtualizada.adicionarCenario(cenarioId, new Cenario(nome));
        this.casas.put(casaId, casaAtualizada);
        return true;
    }

    /**
     * Acrescenta um comando a um cenário de uma casa.
     *
     * @param casaId identificador da casa
     * @param cenarioId identificador do cenário
     * @param cmd comando a acrescentar
     * @return {@code true} se o comando tiver sido acrescentado
     */
    public boolean adicionarComandoACenario(String casaId, String cenarioId, Command cmd) {
        if (casaId == null || cenarioId == null || cmd == null) {
            return false;
        }

        Casa casa = this.casas.get(casaId);
        if (casa == null) {
            return false;
        }

        Casa casaAtualizada = casa.clone();
        if (casaAtualizada.adicionarComandoACenario(cenarioId, cmd)) {
            this.casas.put(casaId, casaAtualizada);
            return true;
        }

        return false;
    }

    /**
     * Obtém um cenário de uma casa.
     *
     * @param casaId identificador da casa
     * @param cenarioId identificador do cenário
     * @return cópia do cenário encontrado, ou {@code null} se não existir
     */
    public Cenario getCenario(String casaId, String cenarioId) {
        Casa casa = this.casas.get(casaId);
        if (casa == null) {
            return null;
        }
        return casa.getCenario(cenarioId);
    }

    /**
     * Atualiza o ambiente interior de uma divisão de uma casa.
     *
     * @param casaId identificador da casa
     * @param divisaoNome nome da divisão
     * @param temperatura nova temperatura interior
     * @param humidade nova humidade interior
     * @param luminosidade nova luminosidade interior
     * @return {@code true} se o ambiente tiver sido atualizado
     */
    public boolean atualizarAmbienteDivisao(String casaId, String divisaoNome,
                                            double temperatura, double humidade,
                                            double luminosidade) {
        if (casaId == null || divisaoNome == null) {
            return false;
        }

        Casa casa = this.casas.get(casaId);
        if (casa == null) {
            return false;
        }

        Casa casaAtualizada = casa.clone();
        Divisao divisaoAtualizada = casaAtualizada.getDivisao(divisaoNome);
        if (divisaoAtualizada == null) {
            return false;
        }

        divisaoAtualizada.atualizarAmbienteInterior(temperatura, humidade, luminosidade);
        casaAtualizada.adicionarDivisao(divisaoAtualizada);
        this.casas.put(casaId, casaAtualizada);
        return true;
    }

    /**
     * Obtém o ambiente interior de uma divisão de uma casa.
     *
     * @param casaId identificador da casa
     * @param divisaoNome nome da divisão
     * @return cópia do ambiente interior, ou {@code null} se não existir
     */
    public AmbienteInterior getAmbienteInteriorDivisao(String casaId, String divisaoNome) {
        Casa casa = this.casas.get(casaId);
        if (casa == null) {
            return null;
        }

        Divisao divisao = casa.getDivisao(divisaoNome);
        if (divisao == null) {
            return null;
        }

        return divisao.getAmbienteInterior();
    }

    /**
     * Disponibiliza um iterador sobre uma cópia protegida das automações de uma
     * casa.
     *
     * @param casaId identificador da casa
     * @return iterador sobre uma cópia das automações da casa
     */
    public Iterator<Automacao> getIteradorAutomacoes(String casaId) {
        Casa casa = this.casas.get(casaId);
        if (casa == null) {
            return Collections.<Automacao>unmodifiableList(new ArrayList<Automacao>()).iterator();
        }
        return casa.getIteradorAutomacoes();
    }

    /**
     * Cria uma automação numa casa.
     *
     * @param casaId identificador da casa
     * @param automacaoId identificador da automação
     * @param nome nome da automação
     * @param divisaoNome nome da divisão associada
     * @param condicao condição que ativa a automação
     * @return {@code true} se a automação tiver sido criada
     */
    public boolean criarAutomacao(String casaId, String automacaoId, String nome,
                                  String divisaoNome, Condicao condicao) {
        if (casaId == null || automacaoId == null || nome == null || divisaoNome == null
                || condicao == null) {
            return false;
        }

        Casa casa = this.casas.get(casaId);
        if (casa == null) {
            return false;
        }

        Casa casaAtualizada = casa.clone();
        if (!casaAtualizada.contemDivisao(divisaoNome) || casaAtualizada.getAutomacao(automacaoId) != null) {
            return false;
        }

        casaAtualizada.adicionarAutomacao(automacaoId, new Automacao(nome, divisaoNome, condicao));
        this.casas.put(casaId, casaAtualizada);
        return true;
    }

    /**
     * Acrescenta uma ação a uma automação de uma casa.
     *
     * @param casaId identificador da casa
     * @param automacaoId identificador da automação
     * @param cmd comando a acrescentar
     * @return {@code true} se a ação tiver sido acrescentada
     */
    public boolean adicionarAcaoAAutomacao(String casaId, String automacaoId, Command cmd) {
        if (casaId == null || automacaoId == null || cmd == null) {
            return false;
        }

        Casa casa = this.casas.get(casaId);
        if (casa == null) {
            return false;
        }

        Casa casaAtualizada = casa.clone();
        if (casaAtualizada.adicionarAcaoAAutomacao(automacaoId, cmd)) {
            this.casas.put(casaId, casaAtualizada);
            return true;
        }

        return false;
    }

    /**
     * Cria um escalonamento numa casa.
     *
     * @param casaId identificador da casa
     * @param escalonamentoId identificador do escalonamento
     * @param nome nome do escalonamento
     * @param horaInicio hora de início
     * @param horaFim hora de fim
     * @return {@code true} se o escalonamento tiver sido criado
     */
    public boolean criarEscalonamento(String casaId, String escalonamentoId, String nome,
                                      LocalTime horaInicio, LocalTime horaFim) {
        if (casaId == null || escalonamentoId == null || nome == null
                || horaInicio == null || horaFim == null) {
            return false;
        }

        Casa casa = this.casas.get(casaId);
        if (casa == null) {
            return false;
        }

        Casa casaAtualizada = casa.clone();
        if (casaAtualizada.getEscalonamento(escalonamentoId) != null) {
            return false;
        }

        casaAtualizada.adicionarEscalonamento(
                escalonamentoId, new Escalonamento(nome, horaInicio, horaFim)
        );
        this.casas.put(casaId, casaAtualizada);
        return true;
    }

    /**
     * Acrescenta uma ação de início a um escalonamento de uma casa.
     *
     * @param casaId identificador da casa
     * @param escalonamentoId identificador do escalonamento
     * @param cmd comando a acrescentar
     * @return {@code true} se a ação tiver sido acrescentada
     */
    public boolean adicionarAcaoInicioAEscalonamento(String casaId, String escalonamentoId,
                                                     Command cmd) {
        if (casaId == null || escalonamentoId == null || cmd == null) {
            return false;
        }

        Casa casa = this.casas.get(casaId);
        if (casa == null) {
            return false;
        }

        Casa casaAtualizada = casa.clone();
        if (casaAtualizada.adicionarAcaoInicioAEscalonamento(escalonamentoId, cmd)) {
            this.casas.put(casaId, casaAtualizada);
            return true;
        }

        return false;
    }

    /**
     * Acrescenta uma ação de fim a um escalonamento de uma casa.
     *
     * @param casaId identificador da casa
     * @param escalonamentoId identificador do escalonamento
     * @param cmd comando a acrescentar
     * @return {@code true} se a ação tiver sido acrescentada
     */
    public boolean adicionarAcaoFimAEscalonamento(String casaId, String escalonamentoId,
                                                  Command cmd) {
        if (casaId == null || escalonamentoId == null || cmd == null) {
            return false;
        }

        Casa casa = this.casas.get(casaId);
        if (casa == null) {
            return false;
        }

        Casa casaAtualizada = casa.clone();
        if (casaAtualizada.adicionarAcaoFimAEscalonamento(escalonamentoId, cmd)) {
            this.casas.put(casaId, casaAtualizada);
            return true;
        }

        return false;
    }

    /**
     * Aplica uma operação a um dispositivo de uma casa.
     *
     * A casa é alterada sobre uma cópia e só volta a ser guardada se a operação
     * for aplicada com sucesso.
     *
     * @param casaId identificador da casa
     * @param dispositivoId identificador do dispositivo
     * @param operacao operação a aplicar ao dispositivo
     * @return {@code true} se a operação tiver sido aplicada
     */
    private boolean aplicarOperacaoEmCasa(String casaId, String dispositivoId,
                                          OperacaoDispositivo operacao) {
        if (casaId == null || dispositivoId == null || operacao == null) {
            return false;
        }

        Casa casa = this.casas.get(casaId);
        if (casa == null) {
            return false;
        }

        Casa casaAtualizada = casa.clone();
        if (casaAtualizada.aplicarOperacaoDispositivo(dispositivoId, operacao)) {
            this.casas.put(casaId, casaAtualizada);
            return true;
        }

        return false;
    }

    /**
     * Compara este gestor com outro objeto com base no seu estado relevante.
     *
     * @param o objeto a comparar
     * @return {@code true} se ambos representarem o mesmo gestor lógico
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        GestorCasas that = (GestorCasas) o;
        return Objects.equals(this.casas, that.casas)
                && Objects.equals(this.dispositivoRegistry, that.dispositivoRegistry);
    }

    /**
     * Calcula um código de dispersão coerente com o estado comparado em
     * {@code equals()}.
     *
     * @return código de dispersão do gestor
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.casas, this.dispositivoRegistry);
    }

    /**
     * Produz uma representação textual legível do gestor.
     *
     * @return texto descritivo do gestor
     */
    @Override
    public String toString() {
        return "GestorCasas{"
                + "casas=" + this.casas
                + ", dispositivoRegistry=" + this.dispositivoRegistry
                + '}';
    }

    /**
     * Cria uma cópia profunda deste gestor.
     *
     * @return novo gestor com o mesmo estado lógico
     */
    public GestorCasas clone() {
        return new GestorCasas(this.casas, this.dispositivoRegistry.clone());
    }

    /**
     * Cria um gestor a partir de casas existentes.
     *
     * @param casas casas a copiar
     * @param dispositivoRegistry registry a associar
     */
    private GestorCasas(Map<String, Casa> casas, DispositivoRegistry dispositivoRegistry) {
        this.casas = new HashMap<String, Casa>();
        this.dispositivoRegistry = dispositivoRegistry;

        for (Map.Entry<String, Casa> entry : casas.entrySet()) {
            this.casas.put(entry.getKey(), entry.getValue().clone());
        }
    }
}
