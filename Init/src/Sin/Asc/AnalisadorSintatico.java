package Sin.Asc;

import java.util.*;

public class AnalisadorSintatico {

    private static Map<String, String> action = new HashMap<>();
    private static Map<String, Integer> goTo = new HashMap<>();


    private static final String[] produces = {
            "S' + E",
            "E + E + T",
            "E + T",
            "T + id"
    };

    public static void main(String[] args) {
        inicializarTabelas();

        List<String> tokens = Arrays.asList("id", "+", "id", "$");
        System.out.println("Sentença de entrada: " + tokens);

        Stack<Integer> pilhaEstados = new Stack<>();
        Stack<String> pilhaSimbolos = new Stack<>();


        pilhaEstados.push(0);
        pilhaSimbolos.push("$");

        int pos = 0;
        String a = tokens.get(pos);

        System.out.println("\n== Análise Sintática Ascendente (LR Simulado) ===");
        System.out.printf("%-10s %-10s %-10s %-15s%n", "ESTADOS", "SÍMBOLOS", "ENTRADA", "AÇÃO");

        while (true) {
            Integer estadoAtual = pilhaEstados.peek();
            String chave = estadoAtual + "," + a;
            String acao = action.get(chave);

            System.out.printf("%-10s %-10s %-10s ", pilhaEstados, pilhaSimbolos, tokens.subList(pos, tokens.size()));

            if (acao == null) {
                System.out.println("ERRO: Sem ação definida para estado " + estadoAtual + " e símbolo '" + a + "'");
                break;
            }

            if (acao.startsWith("s")) {
                int novoEstado = Integer.parseInt(acao.substring(1));
                pilhaEstados.push(novoEstado);
                pilhaSimbolos.push(a);
                pos++;
                a = tokens.get(pos);
                System.out.println("SHIFT " + novoEstado + " sobre '" + (pilhaSimbolos.peek()) + "'");
            }
            else if (acao.startsWith("r")) {
                int regra = Integer.parseInt(acao.substring(1));
                String producao = produces[regra];
                System.out.println("REDUCE " + regra + " (" + producao + ")");

                switch (regra) {
                    case 1:
                        pilhaSimbolos.pop(); pilhaEstados.pop();
                        pilhaSimbolos.pop(); pilhaEstados.pop();
                        pilhaSimbolos.pop(); pilhaEstados.pop();
                        pilhaSimbolos.push("E");
                        break;
                    case 2:
                        pilhaSimbolos.pop(); pilhaEstados.pop();
                        pilhaSimbolos.push("E");
                        break;
                    case 3:
                        pilhaSimbolos.pop(); pilhaEstados.pop();
                        pilhaSimbolos.push("T");
                        break;
                }

                Integer topoEstado = pilhaEstados.peek();
                String naoTerminal = pilhaSimbolos.peek();
                Integer novoEstado = goTo.get(topoEstado + "," + naoTerminal);
                if (novoEstado != null) {
                    pilhaEstados.push(novoEstado);
                } else {
                    System.out.println("ERROR: Sem GOTO para estado " + topoEstado + " e não-terminal " + naoTerminal);
                    break;
                }
            }
            else if (acao.equals("acc")) {
                System.out.println("ACEITO!");
                System.out.println("\n☑ A sentença é válida sintaticamente!");
                break;
            }
        }
    }

    private static void inicializarTabelas() {

        action.put("0,id", "s3");
        action.put("0,E", "g1");
        action.put("0,T", "g2");

        action.put("1,$", "acc");
        action.put("1,+", "s4");

        action.put("2,+", "r2");
        action.put("2,$", "r2");

        action.put("3,+", "r3");
        action.put("3,$", "r3");

        action.put("4,id", "s3");

        action.put("5,$", "r1");

        goTo.put("0,E", 1);
        goTo.put("0,T", 2);
        goTo.put("4,T", 5);
        goTo.put("5,E", 1);
    }
}
