package helper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.*;

import data.DocumentRevision;

public class HtmlParser {

	public static DocumentRevision createRevisionFromHtml(String html) {

		DocumentRevision rev = new DocumentRevision();

		rev.setHtml(html);
		rev.setContent(parseContent(html));
		rev.setSubject(parseSubject(html));
		rev.setDocumentDate(parseDocumentDate(html));
		rev.setUpdateDate(parseUpdateDate(html));
		rev.setFoundDate(new Date());

		return rev;
	}

	private static Date parseGermanDateString(String dateString) {
		try {
			DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
			Date date = (Date)df.parse(dateString);
			return date;
		} catch (NullPointerException e) {
			return null;
		} catch (ParseException e) {
			return null;
		}
	}

	private static Date parseDocumentDate(String html) {
		// EXAMPLE:
		// ...<STRONG>Datum:  </StrONG></td>');
		// document.writeln('<td>25.05.2011');
		String pattern = 
			"<\\s*STRONG\\s*>\\s*" +
			"Datum:\\s*" +
			"<\\s*/\\s*S[Tt][Rr]ONG\\s*>\\s*" +
			"<\\s*/\\s*td\\s*>\\s*'\\s*\\)\\s*;\\s*" +
			"document\\.writeln\\s*\\(\\s*'\\s*<\\s*td\\s*>\\s*" +
			"([0-9]{2}.[0-9]{2}.[0-9]{4})" +
			"\\s*'\\s*\\)\\s*;";
		return extractDateWithRegEx(pattern, html);
	}

	private static Date parseUpdateDate(String html) {
		// EXAMPLE: 
		// ...letzte Aktualisierung des Sachstandes: 22.06.2011)');
		String pattern = 
			"letzte\\s*Aktualisierung\\s*des\\s*Sachstandes\\s*:\\s*" +
			"([0-9]{2}.[0-9]{2}.[0-9]{4})" +
			"\\s*\\)\\s*'\\s*\\)\\s*;";
		return extractDateWithRegEx(pattern, html);
	}

	private static String parseSubject(String html) {
		// EXAMPLE:
		// ...var titel = 'Einspurige Verkehrsführung';
		String pattern = 
			"var\\s*titel\\s*=\\s*'" +
			"(.*)" +
			"'\\s*;";
		return extractWithRegEx(pattern, html).trim();
	}

	private static String parseContent(String html) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private static Date extractDateWithRegEx(String regextype, String input) {
		String dateString = extractWithRegEx(regextype, input).trim();
		return parseGermanDateString(dateString);
	}

	private static String extractWithRegEx(String regextype, String input) {
		String matchedString = null;

		if (regextype != null && input != null) {
			Matcher matcher = Pattern.compile(regextype).matcher(input);
			if (matcher.find()) {
				matchedString = matcher.group(0);
				if (matcher.groupCount() > 0) {
					matchedString = matcher.group(1);
				}
			}
		}
		return matchedString;
	}

}
