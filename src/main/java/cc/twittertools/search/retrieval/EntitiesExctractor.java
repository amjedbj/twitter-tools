package cc.twittertools.search.retrieval;

import java.util.List;

import cc.twittertools.thrift.gen.TResult;

import com.twitter.Extractor;

public class EntitiesExctractor {
  public static List<String> GetMentions(TResult result) {
    Extractor extractor = new Extractor();
    return extractor.extractMentionedScreennames(result.text);
  }

  public static List<String> GetURLs(TResult result) {
    Extractor extractor = new Extractor();
    return extractor.extractURLs(result.text);
  }

  public static List<String> GetHashtags(TResult result) {
    Extractor extractor = new Extractor();
    return extractor.extractHashtags(result.text);
  }
}
