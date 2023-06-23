import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BooleanSearchEngine implements SearchEngine {
    ArrayList<String> keys111 = new ArrayList();
    ArrayList<PageEntry> values222 = new ArrayList();

    public BooleanSearchEngine(File pdfsDir) throws IOException {
        File dir = new File("pdfs");
        File[] arrFiles = dir.listFiles();
        ArrayList<String> pdfName = new ArrayList<>();
        for (File file : dir.listFiles()) {
            pdfName.add(file.getName());
        }

        Map<String, PageEntry> freqs1 = new HashMap<>();
        for (int i = 0; i < arrFiles.length; i++) {
            PdfReader reader = new PdfReader(arrFiles[i]);   //???
            var doc = new PdfDocument(reader);
            for (int j = 1; j <= doc.getNumberOfPages(); ++j) {
                var text = PdfTextExtractor.getTextFromPage(doc.getPage(j));
                text.toLowerCase();
                var words = text.split("\\P{IsAlphabetic}+");
                Map<String, Integer> hm = new HashMap();
                for (String x : words) {
                    if (!hm.containsKey(x)) {
                        hm.put(x, 1);
                    } else {
                        hm.put(x, hm.get(x) + 1);
                    }
                }
                ArrayList<String> keys = new ArrayList<>(hm.keySet());
                ArrayList<Integer> values = new ArrayList<>(hm.values());
                for (int g = 0; g < keys.size(); g++) {
                    freqs1.put(keys.get(g), new PageEntry(pdfName.get(i), j, values.get(g)));
                }
            }
        }
        keys111 = new ArrayList<>(freqs1.keySet());
        values222 = new ArrayList<>(freqs1.values());
    }

    @Override
    public List<PageEntry> search(String word) {
        List<PageEntry> selectedWord = new ArrayList();
        for (int i = 0; i < keys111.size(); i++) {
            if (keys111.get(i).equals(word)) {
                selectedWord.add(values222.get(i));
            }
        }
        Collections.sort(selectedWord);
        return selectedWord;
    }
}
