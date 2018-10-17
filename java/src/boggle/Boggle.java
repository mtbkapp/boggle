package boggle;

import boggle.Trie;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Boggle {

  public static void main(String[] args) throws IOException {
    for (int i = 0; i < 50; i++) {
      long start = System.nanoTime();
      indexWords(readWords("../corncob_lowercase.txt"));
      double dur = (System.nanoTime() - start) / 1000000.0;
      System.out.println(dur + "ms");
    }
  }

  private static Trie indexWords(Collection<String> words) {
    Trie t = new Trie();
    t.addWords(words);

    return t;
  }

  private static Collection<String> readWords(String file) throws IOException {
    List<String> words = new ArrayList<>();
    try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
      String line;
      while((line = reader.readLine()) != null) {
        words.add(line.trim());
      }
    }

    return words;
  }
}
