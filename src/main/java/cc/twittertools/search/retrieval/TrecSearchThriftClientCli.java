package cc.twittertools.search.retrieval;

import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import cc.twittertools.thrift.gen.TQuery;
import cc.twittertools.thrift.gen.TResult;

public class TrecSearchThriftClientCli {
	// Defaults: if user doesn't specify an actual query, run MB01 as a demo.
	private static final String DEFAULT_QID = "MB01";
	private static final String DEFAULT_Q = "BBC World Service staff cuts";
	private static final long DEFAULT_MAX_ID = 34952194402811905L;
	private static final int DEFAULT_NUM_RESULTS = 10;
	private static final String DEFAULT_RUNTAG = "lucene4lm";
	private static final String DEFAULT_FORMAT = "TREC";

	private static final String HELP_OPTION = "h";
	private static final String HOST_OPTION = "host";
	private static final String PORT_OPTION = "port";
	private static final String QID_OPTION = "qid";
	private static final String QUERY_OPTION = "q";
	private static final String RUNTAG_OPTION = "runtag";
	private static final String MAX_ID_OPTION = "max_id";
	private static final String NUM_RESULTS_OPTION = "num_results";
	private static final String FORMAT_OPTION = "format";

	public static String toJson(TResult result) {
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
			sb.append("\""+result.screen_name+"\"");
		}
		first = false;
		if (!first)
			sb.append(", ");
		sb.append("\"created_at\":");
		if (result.created_at == null) {
			sb.append("null");
		} else {
			sb.append("\""+result.created_at+"\"");
		}
		first = false;
		if (!first)
			sb.append(", ");
		sb.append("\"text\":");
		if (result.text == null) {
			sb.append("null");
		} else {
			String text=result.text;
			text=text.replace("\n", "");
			text=text.replace("\"", "\\\"");
			sb.append("\""+text+"\"");
		}
		first = false;
		sb.append("}");
		return sb.toString();
	}
	
	public static String toXml(TResult result) {
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
			String text=result.text;
			text=text.replace("\n", " ");
			sb.append(text);
			sb.append("/<text>");
		}
		sb.append("</result>");
		return sb.toString();
	}

	@SuppressWarnings("static-access")
	public static void main(String[] args) throws Exception {
		Options options = new Options();

		options.addOption(new Option(HELP_OPTION, "show help"));
		options.addOption(OptionBuilder.withArgName("string").hasArg()
				.withDescription("host").create(HOST_OPTION));
		options.addOption(OptionBuilder.withArgName("port").hasArg()
				.withDescription("port").create(PORT_OPTION));
		options.addOption(OptionBuilder.withArgName("string").hasArg()
				.withDescription("query id").create(QID_OPTION));
		options.addOption(OptionBuilder.withArgName("string").hasArg()
				.withDescription("query text").create(QUERY_OPTION));
		options.addOption(OptionBuilder.withArgName("string").hasArg()
				.withDescription("runtag").create(RUNTAG_OPTION));
		options.addOption(OptionBuilder.withArgName("num").hasArg()
				.withDescription("maxid").create(MAX_ID_OPTION));
		options.addOption(OptionBuilder.withArgName("num").hasArg()
				.withDescription("number of results")
				.create(NUM_RESULTS_OPTION));
		options.addOption(OptionBuilder.withArgName("format").hasArg()
				.withDescription("format").create(FORMAT_OPTION));

		CommandLine cmdline = null;
		CommandLineParser parser = new GnuParser();
		try {
			cmdline = parser.parse(options, args);
		} catch (ParseException exp) {
			System.err.println("Error parsing command line: "
					+ exp.getMessage());
			System.exit(-1);
		}

		if (cmdline.hasOption(HELP_OPTION) || !cmdline.hasOption(HOST_OPTION)
				|| !cmdline.hasOption(PORT_OPTION)) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(TrecSearchThriftClientCli.class.getName(),
					options);
			System.exit(-1);
		}

		String qid = cmdline.hasOption(QID_OPTION) ? cmdline
				.getOptionValue(QID_OPTION) : DEFAULT_QID;
		String query = cmdline.hasOption(QUERY_OPTION) ? cmdline
				.getOptionValue(QUERY_OPTION) : DEFAULT_Q;
		String runid = cmdline.hasOption(RUNTAG_OPTION) ? cmdline
				.getOptionValue(RUNTAG_OPTION) : DEFAULT_RUNTAG;
		long max_id = cmdline.hasOption(MAX_ID_OPTION) ? Long.parseLong(cmdline
				.getOptionValue(MAX_ID_OPTION)) : DEFAULT_MAX_ID;
		int num_results = cmdline.hasOption(NUM_RESULTS_OPTION) ? Integer
				.parseInt(cmdline.getOptionValue(NUM_RESULTS_OPTION))
				: DEFAULT_NUM_RESULTS;
		String format = cmdline.hasOption(FORMAT_OPTION) ? cmdline
				.getOptionValue(FORMAT_OPTION) : DEFAULT_FORMAT;

		TrecSearchThriftClient client = new TrecSearchThriftClient(
				cmdline.getOptionValue(HOST_OPTION), Integer.parseInt(cmdline
						.getOptionValue(PORT_OPTION)));

		System.err.println("qid: " + qid);
		System.err.println("q: " + query);
		System.err.println("max_id: " + max_id);
		System.err.println("num_results: " + num_results);
		System.err.println("format: " + format);

		TQuery q = new TQuery();
		q.text = query;
		q.max_id = max_id;
		q.num_results = num_results;

		List<TResult> results = client.search(q);
		int i = 1;
		for (TResult result : results) {
			if (format.equals("txt")) {
				System.out.println(result.toString());
			}else if(format.equals("json")) {
				System.out.println(TrecSearchThriftClientCli.toJson(result));
			}else if(format.equals("xml")) {
				System.out.println(TrecSearchThriftClientCli.toXml(result));
			} else {
				System.out.println(qid + " Q0 " + result.id + " " + i + " "
						+ result.rsv + " " + runid);
			}
			i++;
		}

		client.close();
	}
}