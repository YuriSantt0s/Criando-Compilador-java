package Sin.Asc;

import java.util.*;

public class AnalisadorSintatico {
    static List<String> tokens;
    static int pos = 0;
    private static Scanner sc;

    public static void main(String[] args){
        sc = new Scanner(System.in);
        System.out.println("Digite os Tokens Separados po Espaço ( ex: x + 2 * ( y - 1 )):");

        tokens = Arrays.asList(sc.nextLine().split("\\s+"));

        pos = 0;

        if(expr() && pos == tokens.size()){
            System.out.println("Expressão Sintaticamente Válida");
        } else {
            System.out.println("Erro de sintaxe na Expressão");
        }


    }

    static boolean expr(){
        return termo() && exprLinha();
    }

    static boolean exprLinha(){
        if (match("+")|| match("-")){
            return termo() && exprLinha();
        }
        return true;
    }

    static boolean termo(){
        return fator() && termoLinha();
    }

    static boolean termoLinha(){
        if(match("*") || match("/")){
            return fator() && termoLinha();
        }
        return true;
    }

    static boolean fator(){
        if (match("(")){
            if (expr()) return match(")");
            else return false;
        }

        return matchTipo("IDENTIFICADOR") || matchTipo("NUMERO");
    }

    static boolean match(String esperado){
        if(pos < tokens.size() && tokens.get(pos).equals(esperado)){
            pos++;
            return true;
        }
        return false;
    }

    static boolean matchTipo(String tipo) {
        if (pos >= tokens.size()) return false;

        String token = tokens.get(pos);

        if (tipo.equals("NUMERO") && token.matches("\\d+")){
            pos++;
            return true;
        }
        else if (tipo.equals("IDENTIFICADOR") && token.matches("[a-zA-Z_][a-zA-Z0-9]")){
            pos ++;
            return true;
        }
        return false;
    }

}
