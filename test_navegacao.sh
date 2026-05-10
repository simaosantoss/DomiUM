#!/usr/bin/env bash
set -uo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

G='\033[0;32m'; R='\033[0;31m'; Y='\033[1;33m'; B='\033[1m'; N='\033[0m'

OUTPUT_FILE="/tmp/domus_nav_$$.txt"
INPUT_FILE="/tmp/domus_nav_in_$$.txt"
APP="./app/build/install/app/bin/app"
FAILURES=0

trap 'rm -f "$INPUT_FILE"' EXIT

echo -e "${B}=== Teste de Navegação DomusControl ===${N}"
echo

# ── Build ──────────────────────────────────────────────────────────────────────
echo -e "${Y}[1/2] A compilar...${N}"
if ! ./gradlew installDist -q 2>/dev/null; then
    echo -e "${R}Falha na compilação. A terminar.${N}"; exit 1
fi
echo -e "${G}Compilação OK${N}"
echo

# ── Sequência de inputs ────────────────────────────────────────────────────────
#
# Cada linha é lida pela aplicação exactamente por ordem.
# Estrutura:
#   1. Setup  : utilizadores, casa, divisão, 7 tipos de dispositivos
#   2. Menu principal (opções 2–15)
#   3. Submenu 16 — operações de dispositivos (todas as 14 sub-opções + cancelar)
#   4. Submenu 17 — cenários  (1–5 + voltar)
#   5. Submenu 18 — automações (1–6 + voltar)
#   6. Submenu 19 — escalonamentos (1–5 + voltar)
#   7. Submenu 20 — estatísticas  (1–5 + voltar)
#   8. Submenu 21 — sugestões (1 + 2 com fallback seguro + voltar)
#   9. Guardar / Carregar estado (22–23)
#  10. Data/hora, avançar tempo (24–25)
#  11. Remover dispositivo (11) + Sair (0)

cat > "$INPUT_FILE" <<'INPUTS'
1
u1
Alice
1
u2
Bob
4
u1
c1
Casa Um
7
u1
c1
sala
10
u1
c1
sala
lampada
lmp1
Philips
Hue
0.5
10
u1
c1
sala
coluna
col1
Sony
SRS-XB33
0.2
10
u1
c1
sala
cortina
crt1
Somfy
Glydea
0.1
10
u1
c1
sala
arcondicionado
arc1
Daikin
FTX25K
1.5
10
u1
c1
sala
desumidificador
des1
DeLonghi
DD50P
0.3
10
u1
c1
sala
fechadura
fec1
Yale
Conexis
0.05
10
u1
c1
sala
portao
prt1
Sommer
Twist300
0.4
2
3
u1
c1
u2
2
5
6
c1
8
c1
sala
9
u1
c1
sala
22.0
50.0
300.0
12
u1
c1
lmp1
13
u1
c1
lmp1
14
c1
lmp1
15
c1
16
u1
c1
1
lmp1
16
u1
c1
3
lmp1
75
16
u1
c1
4
lmp1
4000
16
u1
c1
2
lmp1
16
u1
c1
5
col1
50
16
u1
c1
6
col1
playlist1
16
u1
c1
7
crt1
80
16
u1
c1
8
arc1
22.5
16
u1
c1
9
arc1
arrefecimento
16
u1
c1
10
des1
45.0
16
u1
c1
11
prt1
16
u1
c1
12
prt1
16
u1
c1
13
fec1
16
u1
c1
14
fec1
16
u1
c1
0
17
1
u1
c1
cen1
Cenario Um
2
u1
c1
cen1
1
lmp1
3
u1
c1
cen1
4
c1
5
u1
c1
cen1
0
18
1
u1
c1
aut1
Automacao Temperatura
sala
25.0
s
2
u1
c1
aut2
Automacao Humidade
sala
60.0
n
3
u1
c1
aut3
Automacao Luminosidade
sala
500.0
s
4
u1
c1
aut1
1
lmp1
5
c1
6
u1
c1
aut1
0
19
1
u1
c1
esc1
Escalonamento Um
08:00
20:00
2
u1
c1
esc1
1
lmp1
3
u1
c1
esc1
2
lmp1
4
c1
5
u1
c1
esc1
0
20
1
2
c1
3
c1
4
5
0
21
1
u1
2
u1
99
0
22
/tmp/domus_test.estado
23
/tmp/domus_test.estado
24
25
60
11
u1
c1
prt1
0
INPUTS

# ── Execução ───────────────────────────────────────────────────────────────────
echo -e "${Y}[2/2] A executar navegação completa...${N}"
"$APP" < "$INPUT_FILE" > "$OUTPUT_FILE" 2>&1 || true
echo -e "${G}Concluído${N} — output completo em: $OUTPUT_FILE"
echo

# ── Helpers ────────────────────────────────────────────────────────────────────
# Grep no ficheiro para evitar broken-pipe com pipefail.
check() {
    local label="$1" pattern="$2"
    if grep -qF "$pattern" "$OUTPUT_FILE"; then
        echo -e "  ${G}✓${N} $label"
    else
        echo -e "  ${R}✗${N} $label  [esperado: \"$pattern\"]"
        FAILURES=$((FAILURES + 1))
    fi
}

check_count() {
    local label="$1" pattern="$2" min="$3"
    local count
    count=$(grep -cF "$pattern" "$OUTPUT_FILE" 2>/dev/null || echo "0")
    if [ "$count" -ge "$min" ]; then
        echo -e "  ${G}✓${N} $label (${count}×)"
    else
        echo -e "  ${R}✗${N} $label  [esperado >=$min, encontrado $count]"
        FAILURES=$((FAILURES + 1))
    fi
}

# ── Verificações ───────────────────────────────────────────────────────────────
echo -e "${B}Utilizadores & Casas${N}"
check_count "Criar utilizadores (u1 + u2)"          "Utilizador criado."           2
check       "Criar casa c1"                          "Casa criada."
check       "Adicionar divisão sala"                 "Divisão adicionada."
check       "Gerir permissões (op 3)"                "Permissão atribuída."

echo -e "${B}Dispositivos — setup (7 tipos)${N}"
check_count "Adicionar 7 dispositivos"               "Dispositivo adicionado."      7

echo -e "${B}Menu principal — opções directas${N}"
check       "Atualizar ambiente divisão (op 9)"      "Ambiente atualizado."
check       "Ligar dispositivo (op 12)"              "Dispositivo ligado."
check       "Desligar dispositivo (op 13)"           "Dispositivo desligado."
check       "Remover dispositivo (op 11)"            "Dispositivo removido."

echo -e "${B}Submenu 16 — operações de dispositivos${N}"
check_count "Executar sub-ops 1–14 (todas)"          "Operação executada."         14
check       "Cancelar sub-op (op 0)"                 "Operação cancelada."

echo -e "${B}Submenu 17 — cenários${N}"
check       "Criar cenário"                          "Cenário criado."
check       "Adicionar comando a cenário"            "Comando adicionado ao cenário."
check       "Executar cenário"                       "Cenário executado."
check       "Remover cenário"                        "Cenário removido."

echo -e "${B}Submenu 18 — automações${N}"
check_count "Criar automações (temp + hum + lux)"    "Automação criada."            3
check       "Adicionar ação à automação"             "Ação adicionada à automação."
check       "Remover automação"                      "Automação removida."

echo -e "${B}Submenu 19 — escalonamentos${N}"
check       "Criar escalonamento"                    "Escalonamento criado."
check       "Adicionar ação de início"               "Ação de início adicionada."
check       "Adicionar ação de fim"                  "Ação de fim adicionada."
check       "Remover escalonamento"                  "Escalonamento removido."

echo -e "${B}Submenu 20 — estatísticas${N}"
check       "Resumo consumo casas (op 5)"            "Resumo"

echo -e "${B}Submenu 21 — sugestões${N}"
check       "Listar sugestões"                       "sugestões"

echo -e "${B}Persistência${N}"
check       "Guardar estado (op 22)"                 "Estado guardado."
check       "Carregar estado (op 23)"                "Estado carregado."

echo -e "${B}Tempo${N}"
check       "Avançar tempo (op 25)"                  "Tempo avançado."

echo -e "${B}Saída${N}"
check       "Aplicação terminada"                    "Aplicação terminada."

# ── Sumário ────────────────────────────────────────────────────────────────────
ERROR_LINES=$(grep "^Erro:" "$OUTPUT_FILE" || true)
ERROR_COUNT=0
if [ -n "$ERROR_LINES" ]; then
    ERROR_COUNT=$(echo "$ERROR_LINES" | wc -l | tr -d ' ')
fi

echo
echo "─────────────────────────────────────────────────────"
if [ "$FAILURES" -eq 0 ] && [ "$ERROR_COUNT" -eq 0 ]; then
    echo -e "${G}${B}✓  PASSOU${N} — todos os checks OK, sem erros de domínio."
elif [ "$FAILURES" -eq 0 ]; then
    echo -e "${Y}${B}⚠  ATENÇÃO${N} — checks OK mas $ERROR_COUNT linha(s) \"Erro:\" detectada(s):"
    echo "$ERROR_LINES" | sed 's/^/    /'
    echo "   (ver output completo para contexto)"
else
    echo -e "${R}${B}✗  FALHOU${N} — $FAILURES check(s) em falta."
    if [ -n "$ERROR_LINES" ]; then
        echo "   Erros de domínio ($ERROR_COUNT):"
        echo "$ERROR_LINES" | sed 's/^/    /'
    fi
fi
echo "Output completo: $OUTPUT_FILE"
