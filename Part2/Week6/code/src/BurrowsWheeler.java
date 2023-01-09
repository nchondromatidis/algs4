public class BurrowsWheeler {

    // apply Burrows-Wheeler encoding, reading from standard input and writing to standard output
    public static void encode()
    {
        String decoded_string = BinaryStdIn.readString();
        CircularSuffixArray4 csa = new CircularSuffixArray4(decoded_string);
        decoded_string = decoded_string + decoded_string;
        for(int i = 0; i < csa.length(); i++) {
            if(csa.index(i) == 0) {
                BinaryStdOut.write(i);
                break;
            }
        }
        for(int i = 0; i < csa.length(); i++) {
            BinaryStdOut.write(decoded_string.charAt(csa.index(i) + csa.length() - 1));
        }
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler decoding, reading from standard input and writing to standard output
    public static void decode()
    {
//        String file_name = "abra.bwt";
//        BinaryIn in = new BinaryIn(file_name);
//        int first = in.readInt();
//        String encoded_string  = in.readString();
        int first = BinaryStdIn.readInt();
        String encoded_string = BinaryStdIn.readString();
        //start decoding
        char[] t = new char[encoded_string.length()];
        for(int i = 0; i < encoded_string.length() ; i++) {
            t[i] = encoded_string.charAt(i);
        }
        // sort by key-indexed counting
        int R = 256;
        int N = encoded_string.length();
        // compute frequency counts
        int[] count = new int[R+1];
        for (int i = 0; i < N; i++)
            count[t[i] + 1]++;
        // compute cumulates
        for (int r = 0; r < R; r++)
            count[r+1] += count[r];
        // move data to st (sorted t[]) and create next[]
        char[] st = new char[N];
        int[] next = new int[N];
        for (int i = 0; i < N; i++){
            next[count[t[i]]] = i;
            st[count[t[i]]++] = t[i];
        }
        // decode encoded_string using next and first
        int next_char_index = first;
        int counter = 0;
        while (counter++ < N)
        {
            BinaryStdOut.write(st[next_char_index]);
            next_char_index = next[next_char_index];
        }
        BinaryStdOut.close();
    }

    // if args[0] is '-', apply Burrows-Wheeler encoding
    // if args[0] is '+', apply Burrows-Wheeler decoding
    public static void main(String[] args)
    {
        if (args[0]== null ) throw new IllegalArgumentException();
        if (args[0].equals("-")) encode();
        if (args[0].equals("+")) decode();
    }
}