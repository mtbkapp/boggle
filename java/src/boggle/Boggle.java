package boggle;

import boggle.Board;
import boggle.Trie;
import boggle.Position;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Arrays;


public class Boggle {
  private static final String[] testWords = 
    {"and", "ant", "are", "bike", "bug", "buggy", "colorado", "utah"};

  public static void main(String[] args) throws IOException {
    Board board = Board.testBoard1();
    Trie index = indexWords(readWords("../corncob_lowercase.txt"));
    //Trie index = indexWords(Arrays.asList(testWords));
    Collection<String> words = findWords(board, index);
    for(String w : words) {
      System.out.println(w);
    }
  }

  private static Trie indexWords(Collection<String> words) {
    Trie t = new Trie();
    t.addWords(words);

    return t;
  }

  private static Collection<String> readWords(String file) throws IOException {
    ArrayList<String> words = new ArrayList<>();
    try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
      String line;
      while((line = reader.readLine()) != null) {
        words.add(line.trim());
      }
    }

    return words;
  }


  public static Collection<String> findWords(Board board, Trie index) {
    return board.allPositions()
            .stream()
            .map((pos) -> search(board, index, pos, new ArrayList<>()))
            .reduce(new HashSet<>(), (acc, set) -> {
              acc.addAll(set);
              return acc;
            });
  }

  private static Set<String> search(Board board, Trie index, Position pos) {
    Set<String> words = search(board, index, pos, new ArrayList<>());

    return words;
  }

  private static Set<String> search(Board board, Trie index, Position pos, List<Character> letters) {
    char letter = board.getChar(pos);

    if (index.containsChar(letter)) {
      Trie.TrieNode node = index.getNode(letter);
      List<Character> nextLetters = nextLetters(letters, letter); 

      Set<String> nextWords = pos.adjacentPositions()
        .stream()
        .map((nextPos) -> search(board, node.getTrie(), nextPos, nextLetters))
        .reduce(new HashSet<String>(), (acc, searchWords) -> {
          acc.addAll(searchWords);
          return acc; 
        });


      if (node.isWord()) {
        nextWords.add(lettersToWord(nextLetters));
      }

      return nextWords;
    }

    return new HashSet<>();
  }

  private static List<Character> nextLetters(List<Character> letters, char c) {
    List<Character> next = new ArrayList<>(letters);
    next.add(c);

    return next;
  }

  private static String lettersToWord(List<Character> letters) {
    StringBuffer buff = new StringBuffer();
    for(char c : letters) {
      buff.append(c);
    }

    return buff.toString();
  }
}
