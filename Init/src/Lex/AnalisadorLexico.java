package Lex;

import java.util.*;
import java.util.regex.*;

public class AnalisadorLexico {

    static class Token{
        String tipo, valor;

        Token(String tipo, String valor){
            this.tipo = tipo;
            this.valor = valor;
        }

        public String toString(){
            return String.format("<%s,\"%s\">",tipo,valor);
        }
    }

    public static List<Token> analisador(String codigo){
        List<Token> tokens = new ArrayList<>();

        String padraoEspaco = "\\s+";
        String padraoNumero = "\\d+";
        String padraoIdentificador = "[a-zA-Z_][a-zA-Z0-9_]";
        String padraoOperador = "[+\\-*/=]";

        Pattern padrao = Pattern.compile(
                String.format("(%s)|(%s)|(%s)|(%s)", padraoEspaco,padraoNumero,padraoIdentificador, padraoOperador)
        );

        Matcher matcher = padrao.matcher(codigo);

        while (matcher.find()){
            String token = matcher.group();

            if(token.matches(padraoEspaco)) continue;
            else if (token.matches(padraoNumero)) tokens.add(new Token("NUMERO",token));
            else if (token.matches(padraoIdentificador)) tokens.add( new Token("IDENTIFICADOR", token));
            else if (token.matches(padraoOperador)) tokens.add(new Token("OPERADOR", token));
            else tokens.add(new Token("DESCONHECIDO", token));
        }
        return tokens;

    }

    public static void main(String[] args){
        var scanner = new Scanner(System.in);
        System.out.println("Digite o código de Entrada");
        String entrada = scanner.nextLine();

        List<Token> resultado = analisador(entrada);
        for (Token t : resultado){
            System.out.println(t);
        }
    }

}
