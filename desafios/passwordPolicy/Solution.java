class Solution{
    public static void main(String args[]){

        if(args.length == 0){
            System.out.println("Informe uma senha");
            return;
        }
        
        String senha = args[0];

        boolean deveTerNoMinimoOitoCaracteres = senha.length() >= 8;
        boolean deveConterPeloMenosUmNumero = contemPeloMenosUmNumero(senha);
        boolean deveConterPeloMenosUmaLetraMaiuscula = contemPeloMenosUmaLetraMaiuscula(senha);
        boolean deveConterPeloMenosUmaLetraMinuscula = contemPeloMenosUmaLetraMinuscula(senha);
        boolean deveConterPeloMenosUmCaractereEspecial = contemPeloMenosUmCaractereEspecial(senha);
        boolean naoContemSequenciasFracas = naoContemTresNumerosConsecutivos(senha);

        boolean senhaValida = deveTerNoMinimoOitoCaracteres && deveConterPeloMenosUmaLetraMaiuscula && deveConterPeloMenosUmNumero && deveConterPeloMenosUmaLetraMinuscula && deveConterPeloMenosUmCaractereEspecial && naoContemSequenciasFracas;

        if(senhaValida){
            System.out.println("Senha válida");
        }else{
            System.out.println("Senha inválida");
        }
    }

    static boolean contemPeloMenosUmNumero(String senha){
        return (senha.contains("1") || senha.contains("2") || senha.contains("3") || senha.contains("4") || senha.contains("5") || senha.contains("6") || senha.contains("7") || senha.contains("8") || senha.contains("9") || senha.contains("0"));
    } 

    static boolean contemPeloMenosUmaLetraMaiuscula(String senha){
        boolean result = (senha.contains("A") || senha.contains("B") || senha.contains("C") || senha.contains("D") || senha.contains("E") || senha.contains("F") || senha.contains("G") || senha.contains("H") || senha.contains("I") || senha.contains("J") || senha.contains("K") || senha.contains("L") || senha.contains("M") || senha.contains("N") || senha.contains("O") || senha.contains("P") || senha.contains("Q") || senha.contains("R") || senha.contains("S") || senha.contains("T") || senha.contains("U") || senha.contains("V") || senha.contains("W") || senha.contains("X") || senha.contains("Y") || senha.contains("Z"));
        
        return result;
    }

    static boolean contemPeloMenosUmaLetraMinuscula(String senha){
        boolean result = (senha.contains("a") || senha.contains("b") || senha.contains("c") || senha.contains("d") || senha.contains("e") || senha.contains("f") || senha.contains("g") || senha.contains("h") || senha.contains("i") || senha.contains("j") || senha.contains("k") || senha.contains("l") || senha.contains("m") || senha.contains("n") || senha.contains("o") || senha.contains("p") || senha.contains("q") || senha.contains("r") || senha.contains("s") || senha.contains("t") || senha.contains("u") || senha.contains("v") || senha.contains("w") || senha.contains("x") || senha.contains("y") || senha.contains("z"));
        
        return result;
    }

    static boolean contemPeloMenosUmCaractereEspecial(String senha){
        boolean result = (senha.contains("!") || senha.contains("@") || senha.contains("#") || senha.contains("$") || senha.contains("%") || senha.contains("^") || senha.contains("&") || senha.contains("*") || senha.contains("(") || senha.contains(")") || senha.contains("-") || senha.contains("+") || senha.contains("="));
        
        return result;
    }

    static boolean naoContemTresNumerosConsecutivos(String senha){
        char[] charsSenha = senha.toCharArray();
        boolean result = true;
        for (int i = 0; i < (charsSenha.length - 2); i++) {
            if (Character.isDigit(charsSenha[i])) {
                int primeiroNumero = Character.getNumericValue(charsSenha[i]);
                if (Character.isDigit(charsSenha[i+1])) {
                    int segundoNumero = Character.getNumericValue(charsSenha[i+1]);
                    if (segundoNumero == primeiroNumero+1 || segundoNumero == primeiroNumero-1) {
                        if (Character.isDigit(charsSenha[i+2])) {
                            int terceiroNumero = Character.getNumericValue(charsSenha[i+2]);
                            if ((terceiroNumero == primeiroNumero+2 && terceiroNumero == segundoNumero+1) || (terceiroNumero == primeiroNumero-2 && terceiroNumero == segundoNumero-1)) {
                               result = false;
                               return result;
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

}
