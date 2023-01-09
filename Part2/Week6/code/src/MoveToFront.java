import java.util.LinkedList;
import java.util.List;

public class MoveToFront {
    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode()
    {
//        String file_name = ".\\burrows\\abra.txt";
//        BinaryIn in = new BinaryIn(file_name);
//        String decoded_string  = in.readString();
        int R = 256;
        // initialize character sequence
        List<Character> char_seq = new LinkedList<Character>();
        for(int i = 0; i < R; i++) {
            char_seq.add((char)i);
        }
        int char_pos;
        char c;
        while (!BinaryStdIn.isEmpty()){
            c = BinaryStdIn.readChar(8);
            char_pos = char_seq.indexOf(c);
            BinaryStdOut.write(char_pos,8);
            BinaryStdOut.flush();
            char_seq.remove(char_pos);
            char_seq.add(0, c);
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode()
    {
        int R = 256;
        // initialize character sequence
        List<Character> char_seq = new LinkedList<Character>();
        for(int i = 0; i < R; i++) {
            char_seq.add((char)i);
        }
        int char_pos;
        char c;
        while (!BinaryStdIn.isEmpty()){
            char_pos = BinaryStdIn.readChar(8);
            c = char_seq.get(char_pos);
            BinaryStdOut.write(c,8);
            BinaryStdOut.flush();
            char_seq.remove(char_pos);
            char_seq.add(0, c);
        }
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args)
    {
        if (args[0]== null ) throw new IllegalArgumentException();
        if (args[0].equals("-")) encode();
        if (args[0].equals("+")) decode();
    }
}
