package sem;

import java.util.*;

public class AnalisadorSemantico {

    static Map<String,String> tabelaSimbolos = new HashMap<>();

    public static void main (String[] args){
         List<String> linhas = Arrays.asList(
                "int x = 2;",
                "float y = x + 3.5;",
                "z = x + y;",
                "boolean b = x + y;"
        );

        System.out.println("Inicando Análise semântica");

        for (String linha : linhas){
            analisadorlinha(linha.trim());
        }
    }

    static void analisadorlinha(String linha){

        if(linha.endsWith(";")) linha = linha.substring(0, linha.length() - 1 );

        if(linha.matches("(int|float|boolean)\\s+\\w+\\s*=.+")){
            String[] partes = linha.split("=",2);
            String ladoEsq = partes[0].trim();
            String ladoDir = partes[1].trim();

            String[] tipoVar = ladoEsq.split("\\s+");
            String tipo = tipoVar[0];
            String var = tipoVar[1];

            String tipoExpr = inferirTipo(ladoDir);

            if(tipo.equals(tipoExpr)|| (tipo.equals("float") && tipoExpr.equals("int"))){
                tabelaSimbolos.put(var,tipo);
                System.out.println("Variável "+var+" declarada como "+tipo);
            }else{
                System.out.println("Erro semântico: tipo incompatível ao declarar "+var+"'");
            }}
        else if(linha.matches("\\w+\\s*=.+")) {
            String[] partes = linha.split("=", 2);
            String var = partes[0].trim();
            String expr = partes[1].trim();

            String tipoExpr = inferirTipo(expr);

            if (tabelaSimbolos.containsKey(var)) {
                String tipoVar = tabelaSimbolos.get(var);
                if (tipoVar.equals(tipoExpr) || (tipoVar.equals("float") && tipoExpr.equals("int"))) {
                    System.out.println("Atribuição válida para" + var + "'");
                } else {
                    System.out.println("Erro semântico: atribuição não compatível em variável" + var + "'");
                }
            } else {
                System.out.println("Erro semântico: variável " + var + " não declarada");
            }
        }
        else{System.out.println("Linha não rechecida "+linha+"'");}

        }




    static String inferirTipo(String expr){
        expr = expr.replaceAll("[()]","");

        String[] tokens = expr.split("[\\s+\\-*/]+");
        String tipoFinal = "int";

        for(String token : tokens){
            token = token.trim();

            if(token.matches("\\d+")) continue;
            else if(token.matches("\\d+\\.\\d+")) tipoFinal = "float";
            else if (tabelaSimbolos.containsKey(token)) {
                String tipoaVar = tabelaSimbolos.get(token);
                if(tipoaVar.equals("float")) tipoFinal = "float";
                else if (tipoaVar.equals("boolean")) tipoFinal = "boolean";
            } else{
                return "erro";
            }

        }
        return tipoFinal;
    }}






