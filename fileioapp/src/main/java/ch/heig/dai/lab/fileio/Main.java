package ch.heig.dai.lab.fileio;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

// *** TODO: Change this to import your own package ***
import ch.heig.dai.lab.fileio.karilla.*;

public class Main {
    // *** TODO: Change this to your own name ***
    private static final String newName = "Benoit";

    /**
     * Main method to transform files in a folder.
     * Create the necessary objects (FileExplorer, EncodingSelector, FileReaderWriter, Transformer).
     * In an infinite loop, get a new file from the FileExplorer, determine its encoding with the EncodingSelector,
     * read the file with the FileReaderWriter, transform the content with the Transformer, write the result with the
     * FileReaderWriter.
     * 
     * Result files are written in the same folder as the input files, and encoded with UTF8.
     *
     * File name of the result file:
     * an input file "myfile.utf16le" will be written as "myfile.utf16le.processed",
     * i.e., with a suffixe ".processed".
     */
    public static void main(String[] args) {
        // Read command line arguments
        if (args.length != 2 || !new File(args[0]).isDirectory()) {
            System.out.println("You need to provide two command line arguments: an existing folder and the number of words per line.");
            System.exit(1);
        }
        String folder = args[0];
        int wordsPerLine = Integer.parseInt(args[1]);
        System.out.println("Application started, reading folder " + folder + "...");
            try {
                FileExplorer fileExplorer = new FileExplorer(folder);
                EncodingSelector encodingSelector = new EncodingSelector();
                FileReaderWriter fileReaderWriter = new FileReaderWriter();
                Transformer transformer = new Transformer(newName,wordsPerLine);
                Charset encoding;
                File file;
                String content;
                while(true){
                    file = fileExplorer.getNewFile();
                    if (file == null) {
                        break;
                    }
                    if (file.getName().contains(".processed")) {
                        continue;
                    };
                     encoding =encodingSelector.getEncoding(file);
                    if (encoding == null) {
                        throw new Exception("");
                    }
                    content = fileReaderWriter.readFile(file, encoding);
                    if (content == null) {
                        throw new Exception("");
                    }
                    content = transformer.replaceChuck(content);
                    content = transformer.capitalizeWords(content);
                    content = transformer.wrapAndNumberLines(content);

                    String outputPath = file.getPath() + ".processed";
                    File outputFile = new File(outputPath);
                    if(!fileReaderWriter.writeFile(outputFile, content, StandardCharsets.UTF_8)) {
                        throw new RuntimeException(file.getName() + "can not be written in " + outputFile.getName());
                    }
                }
            } catch (Exception e) {
                System.out.println("Exception: " + e);
            }
        }
}
