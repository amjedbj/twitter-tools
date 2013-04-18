package cc.twittertools.search.retrieval;

import java.util.List;

import cc.twittertools.thrift.gen.TResult;

public class ResultFormater {

  public static final String FORMAT_TEXT = "text";
  public static final String FORMAT_CSV = "csv";
  public static final String FORMAT_TSV = "tsv";
  public static final String FORMAT_JSON = "json";
  public static final String FORMAT_XML = "xml";
  public static final String FORMAT_HASHTAGS = "hashtags";

  public static String Format(TResult result, String format) {
    if (format.equals(ResultFormater.FORMAT_TEXT)) {
      return ResultFormater.ToText(result);
    } else if (format.equals(ResultFormater.FORMAT_CSV)) {
      return ResultFormater.ToCsv(result);
    } else if (format.equals(ResultFormater.FORMAT_TSV)) {
      return ResultFormater.ToTsv(result);
    } else if (format.equals(ResultFormater.FORMAT_JSON)) {
      return ResultFormater.ToJson(result);
    } else if (format.equals(ResultFormater.FORMAT_XML)) {
      return ResultFormater.ToXml(result);
    } else if (format.equals(ResultFormater.FORMAT_HASHTAGS)) {
      return ResultFormater.ToHashtagList(result);
    }
    return null;
  }

  public static String ToHashtagList(TResult result) {
    StringBuilder sb = new StringBuilder();
    sb.append(result.id);
    sb.append("\t");
    List<String> hashtags = EntitiesExctractor.GetHashtags(result);
    boolean first = true;
    for (String hashtag : hashtags) {
      if (!first)
        sb.append(" ");
      sb.append(hashtag);
      first = false;
    }
    return sb.toString();
  }

  public static String ToText(TResult result) {
    StringBuilder sb = new StringBuilder();
    if (result.text != null) {
      String text = result.text;
      text = text.replace("\n", " ");
      sb.append(text);
    }
    return sb.toString();
  }

  public static String ToCsv(TResult result) {
    StringBuilder sb = new StringBuilder();

    sb.append(result.id);
    sb.append(",");

    sb.append(result.rsv);
    sb.append(",");

    if (result.screen_name != null) {
      sb.append("\"" + result.screen_name + "\"");
    }
    sb.append(",");

    if (result.created_at != null) {
      sb.append("\"" + result.created_at + "\"");
    }
    sb.append(",");

    if (result.text != null) {
      String text = result.text;
      text = text.replace("\n", " ");
      text = text.replace("\"", "\\\"");
      sb.append("\"" + text + "\"");
    }
    return sb.toString();
  }

  public static String ToTsv(TResult result) {
    StringBuilder sb = new StringBuilder();

    sb.append(result.id);
    sb.append("\t");

    sb.append(result.rsv);
    sb.append("\t");

    if (result.screen_name != null) {
      sb.append(result.screen_name);
    }
    sb.append("\t");

    if (result.created_at != null) {
      sb.append(result.created_at);
    }
    sb.append("\t");

    if (result.text != null) {
      String text = result.text;
      text = text.replace("\n", " ");
      text = text.replace("\t", " ");
      sb.append(text);
    }
    return sb.toString();
  }

  public static String ToJson(TResult result) {
    StringBuilder sb = new StringBuilder("{");
    boolean first = true;

    sb.append("\"id\":");
    sb.append(result.id);
    first = false;

    if (!first)
      sb.append(", ");
    sb.append("\"rsv\":");
    sb.append(result.rsv);
    first = false;

    if (!first)
      sb.append(", ");
    sb.append("\"screen_name\":");
    if (result.screen_name == null) {
      sb.append("null");
    } else {
      sb.append("\"" + result.screen_name + "\"");
    }
    first = false;

    if (!first)
      sb.append(", ");
    sb.append("\"created_at\":");
    if (result.created_at == null) {
      sb.append("null");
    } else {
      sb.append("\"" + result.created_at + "\"");
    }
    first = false;

    if (!first)
      sb.append(", ");
    sb.append("\"text\":");
    if (result.text == null) {
      sb.append("null");
    } else {
      String text = result.text;
      text = text.replace("\n", " ");
      text = text.replace("\"", "\\\"");
      sb.append("\"" + text + "\"");
    }

    sb.append("}");
    return sb.toString();
  }

  public static String ToXml(TResult result) {
    StringBuilder sb = new StringBuilder("<result>");
    sb.append("<id>");
    sb.append(result.id);
    sb.append("</id>");

    sb.append("<rsv>");
    sb.append(result.rsv);
    sb.append("</rsv>");

    if (result.screen_name != null) {
      sb.append("<screen_name>");
      sb.append(result.screen_name);
      sb.append("</screen_name>");
    }
    if (result.created_at != null) {
      sb.append("<created_at>");
      sb.append(result.created_at);
      sb.append("</created_at>");
    }
    if (result.text != null) {
      sb.append("<text>");
      String text = result.text;
      text = text.replace("\n", " ");
      sb.append(text);
      sb.append("</text>");
    }
    sb.append("</result>");
    return sb.toString();
  }
}
