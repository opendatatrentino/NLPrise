package eu.trentorise.nlprise.identifiers.date;

import java.util.HashMap;

import org.parboiled.Parboiled;
import org.parboiled.parserunners.RecoveringParseRunner;
import org.parboiled.support.ParsingResult;
import org.pojava.datetime.DateTime;

import com.mdimension.jchronic.Chronic;

import eu.trentorise.nlprise.identifiers.date.Response.DateParser;
import eu.trentorise.nlprise.identifiers.parser.ItalianDateParser;

public class DateIdentifier {

	@SuppressWarnings("rawtypes")
	public static Response isADate(String toRecognize) {
		ItalianDateParser parser = Parboiled
				.createParser(ItalianDateParser.class);
		ParsingResult<?> result = new RecoveringParseRunner(
				parser.DateTimeParser()).run(toRecognize);
		Response retval = new Response();
		HashMap<DateParser, Boolean> srv = new HashMap<>();
		if (result.hasErrors()) {
			srv.put(DateParser.INTERNAL, false);

		} else {
			srv.put(DateParser.INTERNAL, true);
			retval.setResult(true);
		}
		toRecognize = toRecognize.replaceAll(",", " ");
		toRecognize = toRecognize.replaceAll(";", " ");
		toRecognize = toRecognize.replaceAll("  ", " ");
		
		
		try
		{
			DateTime.parse(toRecognize);
			srv.put(DateParser.POJAVA, true);
			retval.setResult(true);
		}catch(IllegalArgumentException e)
		{
			srv.put(DateParser.POJAVA, false);
		}
		if (Chronic.parse(toRecognize) == null) {
			srv.put(DateParser.JCHRONIC, false);
		} else {
			srv.put(DateParser.JCHRONIC, true);
			retval.setResult(true);
		}
		
		retval.setSingleParserReturn(srv);
		return retval;
	}
}
