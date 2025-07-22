package Sin.Desc;

import java.util.*;

public class AnalisadorSintatico {
    // Lista de tokens da entrada
    private List<String> tokens;
    // Índice do token atual sendo analisado
    private int pos = 0;

    public AnalisadorSintatico(List<String> tokens) {
        this.tokens = tokens;
    }

    // Método principal que inicia a análise
    public boolean analisar() {
        try {
            E(); // Começa pelo símbolo inicial E
            return pos == tokens.size(); // Todos os tokens devem ser consumidos
        } catch (Exception e) {
            System.out.println("Erro de sintaxe");
            return false;
        }
    }

    // E -> T E'
    private void E() {
        System.out.println("E -> T E'");
        T();
        E_linha();
    }

    // E' -> + T E' | ε
    private void E_linha() {
        System.out.println("E' -> + T E' | ε");
        if (pos < tokens.size() && tokens.get(pos).equals("+")) {
            pos++; // Consome '+'
            T();
            E_linha();
        }
        // Se não for '+', assume ε (não faz nada)
    }

    // T -> F T'
    private void T() {
        System.out.println("T -> F T'");
        F();
        T_linha();
    }

    // T' -> * F T' | ε
    private void T_linha() {
        System.out.println("T' -> * F T' | ε");
        if (pos < tokens.size() && tokens.get(pos).equals("*")) {
            pos++; // Consome '*'
            F();
            T_linha();
        }
        // Se não for '*', assume ε (não faz nada)
    }

    // F -> ( E ) | id
    private void F() {
        System.out.println("F -> ( E ) | id");
        if (pos < tokens.size()) {
            String atual = tokens.get(pos);
            if (atual.equals("id")) {
                pos++; // Consome 'id'
            } else if (atual.equals("(")) {
                pos++; // Consome '('
                E(); // Chama E
                if (pos < tokens.size() && tokens.get(pos).equals(")")) {
                    pos++; // Consome ')'
                } else {
                    throw new RuntimeException("Faltando ')'");
                }
            } else {
                throw new RuntimeException("Token inesperado: " + atual);
            }
        } else {
            throw new RuntimeException("Faltando token (EOF inesperado)");
        }
    }

    // Método principal para testar
    public static void main(String[] args) {
        // Exemplo de entrada: id + id * id
        List<String> tokens = new ArrayList<>(Arrays.asList("id", "+", "id", "*", "id"));
        AnalisadorSintatico parser = new AnalisadorSintatico(tokens);
        boolean resultado = parser.analisar();

        if (resultado) {
            System.out.println("Análise sintática bem-sucedida!");
        } else {
            System.out.println("Análise sintática falhou.");
        }
    }
}
