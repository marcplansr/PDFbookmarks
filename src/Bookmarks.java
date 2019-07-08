import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Scanner;

public class Bookmarks {

	public static void main(String[] args) throws IOException {

		File file = new File("data/index.txt");
		Scanner input = new Scanner(file);
		String string = "";
		ArrayList<Data> textArray = new ArrayList<Data>();
		int sons;
		int increment;
		String output = "";

		while (input.hasNextLine()) {
			string = input.nextLine();
			Data currentLineData = new Data(string);
			textArray.add(currentLineData);
		}
		input.close();

		for (int i = 0; i < textArray.size() - 1; i++) {
			sons = 0;
			increment = textArray.get(i + 1).getDepth() - textArray.get(i).getDepth();

			innerloop: for (int j = i + 1; j < textArray.size(); j++) {

				if ((textArray.get(j).getDepth() - textArray.get(i).getDepth()) <= 0) {
					break innerloop;

				} else if ((textArray.get(j).getDepth() - textArray.get(i).getDepth()) == increment) {
					sons++;
				}
			}

			if (sons == 0) {
				output += "[/Page " + textArray.get(i).getPage() + " /Title <" + textArray.get(i).getHexadecimal()
						+ "> /OUT pdfmark\n";
			} else {
				output += "[/Count " + sons + " /Page " + textArray.get(i).getPage() + " /Title <"
						+ textArray.get(i).getHexadecimal() + "> /OUT pdfmark\n";
			}
		}
		output += "[/Page " + textArray.get(textArray.size() - 1).getPage() + " /Title <"
				+ textArray.get(textArray.size() - 1).getHexadecimal() + "> /OUT pdfmark\n";

		Writer writer = null;
		writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("data/index.info"), "utf-8"));
		writer.write(output);
		writer.close();
	}
}

class Data {
	private int depth;
	private int page;
	private String lineString;
	private String hexadecimal;

	public Data(String textFileLine) throws UnsupportedEncodingException {
		lineString = textFileLine;
		depth = extractDepth(textFileLine);
		page = extractPgNum(textFileLine).getFirst();
		lineString = textFileLine.substring(depth, extractPgNum(textFileLine).getSecond() + 1);
		hexadecimal = toHex(getLineString());
	}

	// Returns text line depth, number of left indentations
	private int extractDepth(String lineString) {
		int index = 0;
		char character = lineString.charAt(index);
		while ((character == ' ') || (character == '\t')) {
			index++;
			character = lineString.charAt(index);
		}
		return index;
	}

	// Returns int value of end of text line
	private Pair extractPgNum(String lineString) {
		int index = lineString.length() - 1;
		char character = lineString.charAt(index);
		String stringEnd = "";
		while ((index >= 0) && (character != ' ') && (character != '\t')) {
			stringEnd = character + stringEnd;
			index--;
			character = lineString.charAt(index);
		}
		while ((index >= 0) && ((character == ' ') || (character == '\t'))) {
			index--;
			character = lineString.charAt(index);
		}
		return new Pair(Integer.parseInt(stringEnd), index);
	}

	private String toHex(String textString) {
		String hexString = "";
		char textChar = ' ';
		for (int i = 0; i < textString.length(); i++) {
			textChar = textString.charAt(i);
			if (textChar == '’') {
				textChar = '\'';
			}
			String hexChar = String.format("%04x", (int) textChar);
			hexString += capZeros(hexChar);
		}
		return hexString;
	}

	private String capZeros(String capString) {
		char capChar = ' ';
		int capPosition = 0;
		for (int i = 0; i < capString.length(); i++) {
			capChar = capString.charAt(i);
			if (capChar == '0') {
				capPosition++;
			} else {
				break;
			}
		}
		return capString.substring(capPosition, capString.length());
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public String getLineString() {
		return lineString;
	}

	public void setLineString(String lineString) {
		this.lineString = lineString;
	}

	public String getHexadecimal() {
		return hexadecimal;
	}

	public void setHexadecimal(String hexadecimal) {
		this.hexadecimal = hexadecimal;
	}
}

class Pair {
	private int first;
	private int second;

	public Pair(int first, int second) {
		this.first = first;
		this.second = second;
	}

	public int getFirst() {
		return first;
	}

	public void setFirst(int first) {
		this.first = first;
	}

	public int getSecond() {
		return second;
	}

	public void setSecond(int second) {
		this.second = second;
	}
}
