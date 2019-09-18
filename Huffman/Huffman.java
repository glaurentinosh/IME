package huffman;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


public class Huffman {

    private static Map<Character, String> charPrefixHashMap = new HashMap<>();
    static HuffmanNode root;

    // Main para testes
    public static void main(String[] args) throws FileNotFoundException {
        int shift;
        Scanner sc = new Scanner(System.in);
        System.out.printf("Write down the path of the text:\n-> ");
        String path = sc.nextLine();
        System.out.println("Path " + path);
        // A --- Encryption
        //
        // Get text to be compressed

        String textPath = path;
        String text = readFile(textPath);
        System.out.printf("\nText:\n*\n*\n" + text);
        // Cipher the text
        System.out.printf("\n\nCipher the text. Insert the shift for Caesar:\n-> ");
        shift = sc.nextInt();
        text = Caesar.cipher(text, shift);
        System.out.printf("\nText:\n*\n*\n" + text + "\n\n");
        // Apply Huffman
        String s = encode(text);
        // Write in file encoded text
        writeFile(s,"code.txt");

        // B --- Decryption
        //
        // Read encoded text
        String code = readFile("code.txt");
        //System.out.println("Encoded: " + code);
        // Decode encoded text
        String textAns = decode(code);
        textAns = Caesar.decipher(textAns, shift);
        System.out.printf("\n\nFinal text:\n*\n*\n" + textAns);

        // Compare sizes
        File rawFile = new File(path);
        File encodedFile = new File("code.txt");
        float size1 = (float) rawFile.length();
        float size2 = (float) encodedFile.length();

        System.out.printf("\n\nSizes:\n");
        System.out.println("- Raw file: "+size1);
        System.out.println("- Encoded file: "+size2);
        System.out.println("--> Factor of compression: "+size2/size1);

    }

    private static HuffmanNode buildTree(Map<Character, Integer> freq) {

        PriorityQueue<HuffmanNode> priorityQueue = new PriorityQueue<>();
        Set<Character> keySet = freq.keySet();
        for (Character c : keySet) {

            HuffmanNode huffmanNode = new HuffmanNode();
            huffmanNode.data = c;
            huffmanNode.frequency = freq.get(c);
            huffmanNode.left = null;
            huffmanNode.right = null;
            priorityQueue.offer(huffmanNode);
        }
        assert priorityQueue.size() > 0;

        while (priorityQueue.size() > 1) {

            HuffmanNode x = priorityQueue.peek();
            priorityQueue.poll();

            HuffmanNode y = priorityQueue.peek();
            priorityQueue.poll();

            HuffmanNode sum = new HuffmanNode();

            sum.frequency = x.frequency + y.frequency;
            sum.data = '-';

            sum.left = x;

            sum.right = y;
            root = sum;

            priorityQueue.offer(sum);
        }

        return priorityQueue.poll();
    }


    private static void setPrefixCodes(HuffmanNode node, StringBuilder prefix) {

        if (node != null) {
            if (node.left == null && node.right == null) {
                charPrefixHashMap.put(node.data, prefix.toString());

            } else {
                prefix.append('0');
                setPrefixCodes(node.left, prefix);
                prefix.deleteCharAt(prefix.length() - 1);

                prefix.append('1');
                setPrefixCodes(node.right, prefix);
                prefix.deleteCharAt(prefix.length() - 1);
            }
        }

    }

    private static String encode(String text) {
        Map<Character, Integer> freq = new HashMap<>();
        for (int i = 0; i < text.length(); i++) {
            if (!freq.containsKey(text.charAt(i))) {
                freq.put(text.charAt(i), 0);
            }
            freq.put(text.charAt(i), freq.get(text.charAt(i)) + 1);
        }

        System.out.println("Character Frequency Map = " + freq);
        root = buildTree(freq);

        setPrefixCodes(root, new StringBuilder());
        System.out.println("Character Prefix Map = " + charPrefixHashMap);
        StringBuilder s = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            s.append(charPrefixHashMap.get(c));
        }

        return s.toString();
    }

    private static String decode(String s) throws NumberFormatException {

        StringBuilder stringBuilder = new StringBuilder();

        HuffmanNode temp = root;

        System.out.println("Encoded: " + s);

        //System.out.println(s.trim().length());
        for (int i = 0; i < s.trim().length(); i++) {
            //System.out.println(i + " " + s.charAt(i));
            int j = Integer.parseInt(String.valueOf(s.charAt(i)));

            if (j == 0) {
                temp = temp.left;
                if (temp.left == null && temp.right == null) {
                    stringBuilder.append(temp.data);
                    temp = root;
                }
            }
            if (j == 1) {
                temp = temp.right;
                if (temp.left == null && temp.right == null) {
                    stringBuilder.append(temp.data);
                    temp = root;
                }
            }
        }
        String ans = stringBuilder.toString();
        System.out.printf("Decoded string is:\n*\n*\n" + ans);
        return ans;

    }

    public static String readFile(String path) {

        //path = "C:\\Users\\Gabriel Laurentino\\IdeaProjects\\LabProg2\\src\\huffman\\sample.txt";

        try {

            // default StandardCharsets.UTF_8
            String content = Files.readString(Paths.get(path));
            //content = content.replaceAll("[\\D]", "");
            //System.out.println(content);

            return content;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }


    }

    public static void writeFile(String content, String arq) throws FileNotFoundException {


        try (PrintWriter out = new PrintWriter(arq)) {
            out.println(content);
        }


    }
}

class HuffmanNode implements Comparable<HuffmanNode> {
    int frequency;
    char data;
    HuffmanNode left, right;

    public int compareTo(HuffmanNode node) {
        return frequency - node.frequency;
    }
}