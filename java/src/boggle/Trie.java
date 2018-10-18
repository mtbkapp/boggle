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
        // last character
        if (trie.containsChar(c)) {
          trie.getNode(c).setIsWord(true);
        } else {
          trie.add(c, new TrieNode(true));
        }
      } else {
        // not last character, must traverse
        TrieNode n;
        if (trie.containsChar(c)) {
          n = trie.getNode(c);
        } else {
          n = new TrieNode(false);
          trie.add(c, n);
        }
        trie = n.getTrie();
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

  @Override
  public String toString() {
    StringBuffer buff = new StringBuffer();
    toString(buff, 0);
    return buff.toString();
  }

  public void toString(StringBuffer buff, int levels) {
    for (char c : map.keySet()) {
      TrieNode n = map.get(c);
      for(int i = 0; i < levels * 4; i++) {
        buff.append(' ');
      }
      buff.append(c);
      buff.append(' ');
      buff.append("isWord = ");
      buff.append(n.isWord());
      buff.append('\n');
      n.getTrie().toString(buff, levels + 1);
    }
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
