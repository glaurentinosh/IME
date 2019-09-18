package huffman;

public class Caesar {
    static String cipher(String msg, int shift){
        String s = "";
        int len = msg.length();

        for(int x = 0; x < len; x++){
            if (Character.isLetter(msg.charAt(x))) {
                if (Character.isLowerCase(msg.charAt(x))){
                    s += (char)((msg.charAt(x) - 'a' + 26 + (shift % 26) ) % 26 + 'a');
                }
                if (Character.isUpperCase(msg.charAt(x))){
                    s += (char)((msg.charAt(x) - 'A' + 26 + (shift % 26) ) % 26 + 'A');
                }
            } else {
                s += msg.charAt(x);
            }

        }
        return s;
    }

    static String decipher(String code, int shift){
        return cipher(code, -shift);
    }

    public static void main(String[] args) {
        System.out.println(cipher("Maior amor, nem mais estranho existe/ que o meu...", 3));
        System.out.println(decipher(cipher("Maior amor, nem mais estranho existe/ que o meu...", 3), 3));
    }
}
