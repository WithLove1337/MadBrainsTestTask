package test;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Main {
    //method that allowed parsing the text into words and count them by providing link to text and area to output the result
    private static void parse(URL link, JTextArea visualOutput) throws IOException {
        int count = 0;
        String pattern = "[\\d\\s\\p{Punct}"+"—]";

        //downloading and openning the file
        InputStream inputStream = link.openStream();
        Files.copy(inputStream, new File("C:\\Users\\Ivan\\file.txt").toPath());

        //reading and deleting the file
        List<String> lines  = Files.readAllLines(Paths.get("C:\\Users\\Ivan\\file.txt"), Charset.defaultCharset());
        String text = "";
        for (String x:lines) {
            text += x + " ";
        }
        Files.delete(Paths.get("C:\\Users\\Ivan\\file.txt"));

        //counting words using HashMap, resolving case conflict by forcing text into lowercase
        text=text.toLowerCase();
        String[] words = text.split("\\.?,?\\s");
        HashMap<String, Integer> countWords = new HashMap<String, Integer>();
        for (int  i = 0;  i < words.length;  i++) {
            if ((words[i].length() > 0) && !(words[i].matches(pattern))) {
                count++;
                Object counter = countWords.get(words[i]);
                countWords.put(words[i], counter == null ? 1 : (int) counter + 1);
            }
        }

        //sorting HashMap by value using LinkedHashMap
        LinkedHashMap<String, Integer> sortedCountWords = new LinkedHashMap<>();
        ArrayList<Integer> list = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : countWords.entrySet()) {
            list.add(entry.getValue());
        }
        Collections.sort(list, Collections.reverseOrder());
        for (int num : list) {
            for (Entry<String, Integer> entry : countWords.entrySet()) {
                if (entry.getValue().equals(num)) {
                    sortedCountWords.put(entry.getKey(), num);
                }
            }
        }

        //output the words
        visualOutput.setText("Количество слов = " + count + "\n\n");
        sortedCountWords.entrySet().forEach( entry -> {
            visualOutput.append( entry.getKey().toString() + " = " + entry.getValue().toString() + "\n");
        });
    }

    public static void main(String[] args) throws IOException {
        //creating frame
        JFrame frame = new JFrame("Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(350,350);
        frame.setLayout(null);
        frame.setVisible(true);

        //creating GUI objects
        JTextField urlField = new JTextField(80);
        urlField.setText("http://madbrains.github.io/java_course_test");
        JButton startButton = new JButton("Start");
        JTextArea outputArea = new JTextArea(10, 10);
        JScrollPane output = new JScrollPane(outputArea);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);

        //building the frame
        frame.getContentPane().add(urlField);
        frame.getContentPane().add(startButton);
        frame.getContentPane().add(output);
        urlField.setBounds(10, 10, 240, 20);
        startButton.setBounds(250, 10, 80, 20);
        output.setBounds(10, 30, 320, 280);

        //bounding button with ActionListener
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    URL link  = new URL(urlField.getText());
                    parse(link, outputArea);
                } catch (IOException malformedURLException) {
                    malformedURLException.printStackTrace();
                }
            }
        });
    }
}