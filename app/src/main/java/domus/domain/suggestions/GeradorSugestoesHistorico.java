package domus.domain.suggestions;

import domus.domain.history.RegistoInteracao;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Gera sugestões de escalonamento a partir do histórico de interações.
 */
public class GeradorSugestoesHistorico implements Serializable {

    /**
     * Gera sugestões de escalonamento para ações repetidas no histórico.
     *
     * @param utilizadorId identificador do utilizador
     * @param historico iterador sobre o histórico do utilizador
     * @param minimoOcorrencias número mínimo de ocorrências para sugerir
     * @param limite número máximo de sugestões
     * @return iterador sobre uma lista protegida de sugestões
     */
    public Iterator<SugestaoEscalonamento> gerarSugestoesEscalonamento(String utilizadorId, Iterator<RegistoInteracao> historico, int minimoOcorrencias, int limite) {
        if (utilizadorId == null || historico == null || minimoOcorrencias <= 0 || limite <= 0) {
            return Collections.<SugestaoEscalonamento>emptyList().iterator();
        }

        Map<ChaveSugestao, Integer> contagens = new HashMap<ChaveSugestao, Integer>();
        while (historico.hasNext()) {
            RegistoInteracao registo = historico.next();
            if (registo.getDataHora() == null) {
                continue;
            }

            LocalTime hora = LocalTime.of(registo.getDataHora().getHour(), registo.getDataHora().getMinute());
            ChaveSugestao chave = new ChaveSugestao(
                    valorSeguro(registo.getCasaId()),
                    valorSeguro(registo.getDispositivoId()),
                    valorSeguro(registo.getAcao()),
                    hora
            );
            Integer ocorrencias = contagens.get(chave);
            if (ocorrencias == null) {
                contagens.put(chave, 1);
            } else {
                contagens.put(chave, ocorrencias + 1);
            }
        }

        Comparator<SugestaoEscalonamento> comparador = Comparator
                .comparingInt(SugestaoEscalonamento::getOcorrencias)
                .reversed()
                .thenComparing(SugestaoEscalonamento::getCasaId)
                .thenComparing(SugestaoEscalonamento::getDispositivoId)
                .thenComparing(SugestaoEscalonamento::getAcao)
                .thenComparing(SugestaoEscalonamento::getHoraSugerida);

        List<SugestaoEscalonamento> resultado = contagens.entrySet().stream()
                .filter(entry -> entry.getValue() >= minimoOcorrencias)
                .map(entry -> criarSugestao(utilizadorId, entry))
                .sorted(comparador)
                .limit(limite)
                .map(SugestaoEscalonamento::clone)
                .collect(Collectors.toList());

        return Collections.unmodifiableList(resultado).iterator();
    }

    /**
     * Cria uma sugestão a partir de uma entrada de contagem.
     *
     * @param utilizadorId identificador do utilizador
     * @param entry entrada com a chave de sugestão e o número de ocorrências
     * @return sugestão correspondente à entrada
     */
    private SugestaoEscalonamento criarSugestao(String utilizadorId, Map.Entry<ChaveSugestao, Integer> entry) {
        ChaveSugestao chave = entry.getKey();
        return new SugestaoEscalonamento(
                utilizadorId,
                chave.casaId,
                chave.dispositivoId,
                chave.acao,
                chave.horaSugerida,
                entry.getValue(),
                criarMensagem(chave)
        );
    }

    /**
     * Substitui valores nulos por texto vazio para agrupamento e ordenação.
     *
     * @param valor valor original
     * @return valor seguro para comparação
     */
    private String valorSeguro(String valor) {
        if (valor == null) {
            return "";
        }
        return valor;
    }

    /**
     * Cria a mensagem textual de uma sugestão.
     *
     * @param chave chave da sugestão
     * @return mensagem em PT-PT
     */
    private String criarMensagem(ChaveSugestao chave) {
        return "O utilizador executou frequentemente a ação '" + chave.acao
                + "' no dispositivo '" + chave.dispositivoId
                + "' por volta das " + chave.horaSugerida
                + ". Pode ser útil criar um escalonamento para esta ação.";
    }

    /**
     * Chave de agrupamento para sugestões de escalonamento.
     */
    private static final class ChaveSugestao {

        /**
         * Identificador da casa.
         */
        private final String casaId;

        /**
         * Identificador do dispositivo.
         */
        private final String dispositivoId;

        /**
         * Ação executada.
         */
        private final String acao;

        /**
         * Hora sugerida.
         */
        private final LocalTime horaSugerida;

        /**
         * Cria uma chave de sugestão.
         *
         * @param casaId identificador da casa
         * @param dispositivoId identificador do dispositivo
         * @param acao ação executada
         * @param horaSugerida hora sugerida
         */
        private ChaveSugestao(String casaId, String dispositivoId, String acao, LocalTime horaSugerida) {
            this.casaId = casaId;
            this.dispositivoId = dispositivoId;
            this.acao = acao;
            this.horaSugerida = horaSugerida;
        }

        /**
         * Compara esta chave com outro objeto com base no seu estado.
         *
         * @param o objeto a comparar
         * @return {@code true} se ambos representarem a mesma chave
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            ChaveSugestao that = (ChaveSugestao) o;
            return Objects.equals(this.casaId, that.casaId)
                    && Objects.equals(this.dispositivoId, that.dispositivoId)
                    && Objects.equals(this.acao, that.acao)
                    && Objects.equals(this.horaSugerida, that.horaSugerida);
        }

        /**
         * Calcula um código de dispersão coerente com {@code equals()}.
         *
         * @return código de dispersão da chave
         */
        @Override
        public int hashCode() {
            return Objects.hash(this.casaId, this.dispositivoId, this.acao, this.horaSugerida);
        }
    }
}
