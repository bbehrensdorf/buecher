package spiegelbestseller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WriteBestsellerToFileDemo {

	public static void main(String[] args) throws IOException {
		URL url = new URL("http://www.spiegel.de/kultur/literatur/spiegel-bestseller-hardcover-a-1025428.html");
		StringBuilder sb = new StringBuilder();

		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
		String inputLine;
		while ((inputLine = in.readLine()) != null)
			sb.append(inputLine);
		in.close();
		String html = sb.toString();

		String pattIsbn = "<div class=\"content\" data-isbn=\"(.*?)\".*?>";
		String pattTitle = "<span class=\"title bestseller-popup-link\">(.*?)</span>";
		String pattAuthor = "<span class=\"autor bestseller-popup-link\">(.*?)</span>";
		String pattPrice = "<span class=\"preis\">(.*?)</span>";
		String pattKlappentext = "<span class=\"bestseller-popup-klappentext\">(.*?)</span>";

		String pattJunk = ".*?";

		String pattAll = pattIsbn + pattJunk + pattTitle + pattJunk + pattAuthor + pattJunk + pattPrice + pattJunk
				+ pattKlappentext;

		Pattern pattern = Pattern.compile(pattAll);
		Matcher matcher = pattern.matcher(html);
		String delimiter = "|";
		String fileName = "resource/Buecher_von_bb";
		try (BufferedWriter out = new BufferedWriter(new FileWriter(fileName))) {
			while (matcher.find()) {
				String isbn = matcher.group(1);
				String title = matcher.group(2).replaceAll("Anders und seine Freunde\\.\\.\\.\\.", "Anders und seine Freunde nebst dem einen oder anderen Feind");
				String author = matcher.group(3);
				String price = matcher.group(4).replaceAll(",", ".").replaceAll(" Euro", "");
				@SuppressWarnings("unused")
				String klappentext = matcher.group(5);

				// Mit Klappentext:
				// String outFormat="%s" + delimiter+"%s"+ delimiter+"%s"+
				// delimiter+"%s"+ delimiter+"%s%n";

				// out.write(String.format(outFormat, isbn, title, author, price, klappentext));

				// ohne Klappentext:
				String outFormat = "%s" + delimiter + "%s" + delimiter + "%s" + delimiter + "%s%n";
				out.write(String.format(outFormat, isbn, title, author, price));

			}
		}
		;

	}

}
