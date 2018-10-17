package boggle;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Trie {
  private Map<Character, TrieNode> map;

  public Trie() {
    map = new HashMap<>();
  }

  public void addWord(String word) {
    //TODO: refactor to call sub trie's add word?
    Trie trie = this;
    char[] cs = word.toCharArray();
    for(int i = 0; i < cs.length; i++) {
      char c = cs[i];
      if (i == cs.length - 1) {
        // not last character, must traverse
        TrieNode n = trie.containsChar(c) ? trie.getNode(c) : new TrieNode(false);
        trie = n.getTrie();
      } else {
        // last character
        if (trie.containsChar(c)) {
          trie.getNode(c).setIsWord(true);
        } else {
          trie.add(c, new TrieNode(true));
        }
      }
    }
  }

  public void addWords(Collection<String> words) {
    words.forEach((w) -> addWord(w));
  }

  public boolean containsChar(char c) {
    return map.containsKey(c);
  }

  public void add(char c, TrieNode node) {
    map.put(c, node);
  }

  public TrieNode getNode(char c) {
    return map.get(c);
  }


  public class TrieNode {
    private boolean isWord;
    private final Trie trie;

    public TrieNode(boolean isWord, Trie trie) {
      this.isWord = isWord;
      this.trie = trie;
    }

    public TrieNode(boolean isWord) {
      this(isWord, new Trie());
    }

    public boolean isWord() {
      return isWord;
    }

    public void setIsWord(boolean isWord) {
      this.isWord = isWord;
    }

    public Trie getTrie() {
      return trie;
    }
  }
}
