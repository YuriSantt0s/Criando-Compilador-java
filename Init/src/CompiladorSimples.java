import java.util.ArrayList;
import java.util.List;

public class CompiladorSimples {

    static class Token {
        String tipo;
        String valor;

        Token(String tipo, String valor) {
            this.tipo = tipo;
            this.valor = valor;
        }

        @Override
        public String toString() {
            return "(" + tipo + ", " + valor + ")";
        }
    }

    // Analisador Léxico (lexer)
    static class Lexer {
        private String input;
        private int pos = 0;

        Lexer(String input) {
            this.input = input;
        }


        Token nextToken() {

            while (pos < input.length() && Character.isWhitespace(input.charAt(pos))) {
                pos++;
            }


            if (pos >= input.length()) {
                return new Token("EOF", "");
            }

            char c = input.charAt(pos);


            if (Character.isLetter(c)) {
                StringBuilder id = new StringBuilder();
                while (pos < input.length() && Character.isLetterOrDigit(input.charAt(pos))) {
                    id.append(input.charAt(pos++));
                }
                return new Token("ID", id.toString());
            }

            if (Character.isDigit(c)) {
                StringBuilder num = new StringBuilder();
                while (pos < input.length() && Character.isDigit(input.charAt(pos))) {
                    num.append(input.charAt(pos++));
                }
                return new Token("NUM", num.toString());
            }

            if (c == '+') {
                pos++;
                return new Token("OP", "+");
            }

            if (c == '=') {
                pos++;
                return new Token("ASSIGN", "=");
            }

            if (c == ';') {
                pos++;
                return new Token("SEMI", ";");
            }

            throw new RuntimeException("Caractere inválido: " + c);
        }
    }

    abstract static class ASTNode {
        abstract void gerarCodigo(List<String> saida);
    }

    static class AssignNode extends ASTNode {
        String var;
        ASTNode expr;

        AssignNode(String var, ASTNode expr) {
            this.var = var;
            this.expr = expr;
        }

        @Override
        void gerarCodigo(List<String> saida) {
            expr.gerarCodigo(saida);
            saida.add("STORE " + var);
        }
    }

    static class BinaryOpNode extends ASTNode {
        String op;
        ASTNode left, right;

        BinaryOpNode(String op, ASTNode left, ASTNode right) {
            this.op = op;
            this.left = left;
            this.right = right;
        }

        @Override
        void gerarCodigo(List<String> saida) {
            left.gerarCodigo(saida);
            right.gerarCodigo(saida);
            if (op.equals("+")) {
                saida.add("ADD");
            }

        }
    }

    static class NumNode extends ASTNode {
        int valor;

        NumNode(int valor) {
            this.valor = valor;
        }

        @Override
        void gerarCodigo(List<String> saida) {
            saida.add("LOAD " + valor);
        }
    }

    static class Parser {
        private Lexer lexer;
        private Token tokenAtual;

        Parser(Lexer lexer) {
            this.lexer = lexer;
            this.tokenAtual = lexer.nextToken();
        }

        private void avancar() {
            tokenAtual = lexer.nextToken();
        }


        ASTNode parseExpr() {
            ASTNode node = parseTerm();
            while (tokenAtual != null && "OP".equals(tokenAtual.tipo) && "+".equals(tokenAtual.valor)) {
                avancar();
                node = new BinaryOpNode("+", node, parseTerm());
            }
            return node;
        }


        ASTNode parseTerm() {
            if ("NUM".equals(tokenAtual.tipo)) {
                int valor = Integer.parseInt(tokenAtual.valor);
                avancar();
                return new NumNode(valor);
            } else {
                throw new RuntimeException("Esperado número, encontrado: " + tokenAtual.valor);
            }
        }


        ASTNode parseProgram() {
            if ("ID".equals(tokenAtual.tipo)) {
                String var = tokenAtual.valor;
                avancar();
                if ("ASSIGN".equals(tokenAtual.tipo)) {
                    avancar();
                    ASTNode expr = parseExpr();
                    if ("SEMI".equals(tokenAtual.tipo)) {
                        avancar();
                        return new AssignNode(var, expr);
                    }
                }
            }
            throw new RuntimeException("Esperado atribuição (ex: a = 10 + 5;)");
        }
    }

    static class CodeGenerator {
        List<String> gerar(ASTNode ast) {
            List<String> bytecode = new ArrayList<>();
            ast.gerarCodigo(bytecode);
            return bytecode;
        }
    }

    public static void main(String[] args) {

        String codigoFonte = "a = 10 + 9 ;";
        System.out.println("> Compilador Simples para Atribuições Aritméticas");
        System.out.println("Código-fonte: " + codigoFonte);

        System.out.println("\n--- Etapa 1: Análise Léxica (Tokens) ---");

        Lexer lexer = new Lexer(codigoFonte);
        List<Token> tokens = new ArrayList<>();
        Token t;
        do {
            t = lexer.nextToken();
            tokens.add(t);
            if (!"EOF".equals(t.tipo)) {
                System.out.println(t);
            }
        } while (!"EOF".equals(t.tipo));


        System.out.println("\n--- Etapa 2: Análise Sintática (AST) ---");
        Parser parser = new Parser(new Lexer(codigoFonte));
        ASTNode ast;
        try {
            ast = parser.parseProgram();
            System.out.println("✓ AST construída com sucesso!");
        } catch (Exception e) {
            System.err.println("✗ Erro de parsing: " + e.getMessage());
            return;
        }

        System.out.println("\n--- Etapa 3: Geração de Código (Bytecode) ---");
        CodeGenerator gerador = new CodeGenerator();
        List<String> bytecode = gerador.gerar(ast);

        for (String instrucao : bytecode) {
            System.out.println(instrucao);
        }

        System.out.println("\n✓ Compilação concluída com sucesso!");
    }
}
