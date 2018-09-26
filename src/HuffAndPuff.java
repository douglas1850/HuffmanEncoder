import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class HuffAndPuff {

    private static final int ALPHABET_SIZE = 256;

    public HuffmanEncodedResult compress(final String data){
        final int[] freq = buildFrequencyTable(data);
        final Node root = buildHuffmanTree(freq);
        final Map<Character, String> lookupTable = buildLookupTable(root);

        return new HuffmanEncodedResult(generateEncodedDate(data, lookupTable), root);
    }

    private static String generateEncodedDate(String data, Map<Character, String> lookupTable) {
        final StringBuilder builder = new StringBuilder();
        for(final char character : data.toCharArray()){
            builder.append(lookupTable.get(character));
        }
        return builder.toString();
    }

    /*
    Map each char to a binary encoding. Length of
    binary encoding depends on frequency of char
    */
    private static Map<Character, String> buildLookupTable(final Node root){
        final Map<Character, String> lookUpTable = new HashMap<>();

        buildLookupTableImpl(root, "", lookUpTable); //populates lookup table

        return lookUpTable;
    }

    private static void buildLookupTableImpl(Node node, String s, Map<Character, String> lookUpTable) {
        if(!node.isLeaf()){
            //encode with 0 if traversing to the left
            buildLookupTableImpl(node.leftChild, s + '0', lookUpTable);
            //encode with 1 if traversing to the right
            buildLookupTableImpl(node.rightChild, s + '1', lookUpTable);
        }else {
            lookUpTable.put(node.character, s);
        }
    }

    private static Node buildHuffmanTree(int[] freq){

        final PriorityQueue<Node> priorityQueue = new PriorityQueue<>();
        //go through all possible characters
        for(char i = 0; i < ALPHABET_SIZE; i++){
            //if the character occurs in our encoded message
            if(freq[i] > 0){
                //create new leaf node of char i with no children
                priorityQueue.add(new Node(i, freq[i], null, null));
            }
        }

        //convert into non-leaf node
        if(priorityQueue.size() == 1){
            priorityQueue.add(new Node('\0', 1, null, null));
        }

        while(priorityQueue.size() > 1){
            //get next 2 leaf nodes from priority queue and merge them together
            final Node left = priorityQueue.poll();
            final Node right = priorityQueue.poll();
            final Node parent = new Node('\0', left.frequency + right.frequency, left, right); //null char to indicate not a leaf
            priorityQueue.add(parent);
        }

        return priorityQueue.poll(); //gives us root node
    }


    private static int[] buildFrequencyTable(final String data){
        //number of letters to support (all ascii values)
        final int[] freq = new int[ALPHABET_SIZE];

        //go through each character in string of data
        for(final char character : data.toCharArray()){
            freq[character]++;
        }

        return freq;
    }

    public String decompress(final HuffmanEncodedResult result){
        return null;
    }

    static class Node implements Comparable<Node>{
        private final char character;
        private final int frequency;
        private final Node leftChild;
        private final Node rightChild;

        private Node(final char character, final int frequency, final Node leftChild, final Node rightChild){
            this.character = character;
            this.frequency = frequency;
            this.leftChild = leftChild;
            this.rightChild = rightChild;
        }

        //has no left or right children
        boolean isLeaf(){
            return this.leftChild == null && this.rightChild == null;
        }

        @Override
        public int compareTo(Node o) {
            //compare Nodes
            final int frequencyComparison = Integer.compare(this.frequency, o.frequency);
            if(frequencyComparison != 0){
                return frequencyComparison;
            }

            return Integer.compare(this.character, o.character);
        }
    }

    static class HuffmanEncodedResult{
        final Node root;
        final String encodedData;

        HuffmanEncodedResult(final String encodedData, final Node root){
            this.encodedData = encodedData;
            this.root = root;
        }

    }

    public static void main(String[] args){
        final String test = "aaabbcdeffff";
        //creates table with # occurences of each letter
        final int[] ft = buildFrequencyTable(test);
        final Node hufTree = buildHuffmanTree(ft);
        final Map<Character, String> lookup = buildLookupTable(hufTree);
        System.out.println(hufTree);
        /*
        When done correctly, huffman encoding will save more used values higher in the tree
        so that traversal can be more efficient
         */
    }
}
