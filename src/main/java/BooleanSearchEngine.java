import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BooleanSearchEngine implements SearchEngine {
    static List<String> wordList = new ArrayList();
    static List<PageEntry> wordInformation = new ArrayList();
    static List<PageEntry> selectedWord = new ArrayList();

    public BooleanSearchEngine(File pdfsDir) throws IOException {
        File dir = new File("pdfs");
        File[] arrFiles = dir.listFiles();
        List<String> pdfName = new ArrayList<>();
        for (File file : dir.listFiles()) {
            pdfName.add(file.getName());
        }
        for (int i = 0; i < arrFiles.length; i++) {
            PdfReader reader = new PdfReader(arrFiles[i]);
            var doc = new PdfDocument(reader);
            for (int j = 1; j <= doc.getNumberOfPages(); ++j) {
                var text = PdfTextExtractor.getTextFromPage(doc.getPage(j)).toLowerCase();
                var words = text.split("\\P{IsAlphabetic}+");
                Map<String, Integer> hm = new HashMap();
                for (String x : words) {
                    if (!hm.containsKey(x)) {
                        hm.put(x, 1);
                    } else {
                        hm.put(x, hm.get(x) + 1);
                    }
                }
                List<String> keys = new ArrayList<>(hm.keySet());
                List<Integer> values = new ArrayList<>(hm.values());
                for (int g = 0; g < keys.size(); g++) {
                    wordList.add(keys.get(g));
                    wordInformation.add(new PageEntry(pdfName.get(i), j, values.get(g)));
                }
            }
        }
    }

    @Override
    public List<PageEntry> search(String word) {
        for (int i = 0; i < wordList.size(); i++) {
            if (wordList.get(i).equals(word)) {
                selectedWord.add(wordInformation.get(i));
            }
        }
        Collections.sort(selectedWord, new Comparator<PageEntry>() {
            @Override
            public int compare(PageEntry o1, PageEntry o2) {
                return o2.getCount() - o1.getCount();
            }
        });
        return selectedWord;
    }
}
