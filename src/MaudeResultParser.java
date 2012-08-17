import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class MaudeResultParser {

	public MaudeResultParser(String filename) throws IOException {

		FileReader reader = new FileReader(filename);
		BufferedReader br = new BufferedReader(reader);
		int currentnode = 0;
		double delay1 = 0.0;
		double delay2 = 0.0;
		double jitter1 = -0.1;
		double jitter2 = -0.1;
		List<Double> DelayList = new ArrayList<Double>();
		List<Double> JitterList = new ArrayList<Double>();
		for (;;)
			try {
				String line = br.readLine();
				if (line==null) {
					break;
				}
				if (line.contains("=======")) {

					System.out.print(currentnode + " ");
					for (Iterator<Double> it = DelayList.iterator(); it
							.hasNext();) {
						System.out.print(it.next() + " ");
					}
					// // for (Iterator<Double> it = JitterList.iterator(); it
					// // .hasNext();) {
					// // System.out.print(it.next() + " ");
					// // }
					
					System.out.println(" ");
					currentnode = 0;
					DelayList.clear();
					JitterList.clear();
					delay1 = 0.0;
					delay2 = 0.0;
					jitter1 = -0.1;
					jitter2 = -0.1;
					continue;
				}
				if (line.contains("rewrite in Network")) {
					String[] rwinnw = line.split(" ");
					currentnode = Integer.parseInt((String) rwinnw[4]
							.subSequence(3, rwinnw[4].length()));

				}

				String[] stringArray = line.split(" ");

				for (int i = 0; i < stringArray.length - 2; i++) {
					String[] delay = null;
					String[] jitter = null;
					if (stringArray[i].contains("delay")) {

						delay = stringArray[i + 2].split(",");
						if (delay1 == 0.0)
							delay1 = Double.parseDouble(delay[0]);
						else if (delay2 == 0.0) {
							delay2 = Double.parseDouble(delay[0]);
							DelayList.add(delay2);
							// System.out.print(delay1+" "+delay2+" || ");
							delay1 = 0.0;
							delay2 = 0.0;
						}

					}
					if (stringArray[i].contains("jitter")) {

						jitter = stringArray[i + 2].split(",");
						if (jitter1 == -0.01)
							jitter1 = Double.parseDouble(jitter[0]);
						else if (jitter2 == -0.01) {
							jitter2 = Double.parseDouble(jitter[0]);
							JitterList.add(jitter2);
							jitter1 = -0.01;
							jitter2 = -0.01;
						}

					}
				}
				

			} catch (IOException e) {
				System.out.println("error==");
				break;
			}

		reader.close();
		br.close();

	}

	public static void main(String args[]) throws IOException {
		MaudeResultParser MRP = new MaudeResultParser("MaudeFW\\result.txt");
	}
}
