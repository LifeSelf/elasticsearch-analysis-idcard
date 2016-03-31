package org.lavenderx.analysis;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.AttributeFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Baymax
 */
public final class IdCardTokenizer extends Tokenizer {

    /**
     * Creates a new {@link IdCardTokenizer} instance
     */
    public IdCardTokenizer() {
    }

    /**
     * Creates a new {@link IdCardTokenizer} instance
     *
     * @param factory the attribute factory to use for this {@link Tokenizer}
     */
    public IdCardTokenizer(AttributeFactory factory) {
        super(factory);
    }

    // The raw input
    private String stringToTokenize = null;

    // Position in the idCardTokens array. We build all the idCardTokens and return them one at a time as incrementToken gets called.
    private int position = 0;

    /**
     * The idCardTokens are determined on the first iteration and then returned one at a time
     */
    private List<String> idCardTokens = null;

    // The base class grabs the termAtt each time incrementToken returns.
    private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);

    private String readStringFromInput(Reader in) {
        try (BufferedReader br = new BufferedReader(in)) {
            String s;
            if ((s = br.readLine()) != null) {
                return s;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    @Override
    public final boolean incrementToken() throws IOException {
        clearAttributes();
        termAtt.setEmpty();

        if (idCardTokens == null) {
            idCardTokens = new ArrayList<>();
            stringToTokenize = readStringFromInput(input);

            // It's the 1st iteration, chop it up into idCardTokens.
            generateTokens();
        }

        // Return those idCardTokens
        return returnTokensOneAtATime();
    }

    private boolean returnTokensOneAtATime() {
        if (idCardTokens != null) {
            if (position == idCardTokens.size()) {
                // No more idCardTokens
                return false;
            }

            // Return each token
            termAtt.append(idCardTokens.get(position));
            position += 1;

            return true;
        }

        return false;
    }

    private void generateTokens() {
        int tokenizeLength = stringToTokenize.length();
        switch (tokenizeLength) {
            case 4:
                idCardTokens.add(stringToTokenize);
                break;
            case 6:
                idCardTokens.add(stringToTokenize);
                break;
            case 10:
                idCardTokens.add(stringToTokenize.substring(0, 6));
                idCardTokens.add(stringToTokenize);
                break;
            case 14:
                idCardTokens.add(stringToTokenize.substring(0, 6));
                idCardTokens.add(stringToTokenize.substring(0, 10));
                idCardTokens.add(stringToTokenize);
                break;
            case 18:
                if (stringToTokenize.contains("*")) {
                    int firstIndex = stringToTokenize.indexOf("*"), lastIndex = stringToTokenize.lastIndexOf("*");
                    if (firstIndex != -1 && firstIndex == 0) {
                        idCardTokens.add(stringToTokenize.replaceAll("\\*", ""));
                    } else if (lastIndex != -1 && lastIndex == 17) {
                        idCardTokens.add(stringToTokenize.substring(0, 6));
                        idCardTokens.add(stringToTokenize.substring(0, 10));
                        idCardTokens.add(stringToTokenize.substring(0, 14));
                    } else {
                        idCardTokens.add(stringToTokenize.substring(0, 6));
                        idCardTokens.add(stringToTokenize.substring(0, 10));
                        idCardTokens.add(stringToTokenize.substring(14, 18));
                    }
                } else {
                    idCardTokens.add(stringToTokenize);
                    idCardTokens.add(stringToTokenize.substring(0, 6));
                    idCardTokens.add(stringToTokenize.substring(0, 10));
                    idCardTokens.add(stringToTokenize.substring(0, 14));
                    idCardTokens.add(stringToTokenize.substring(14, 18));
                }
                break;
            default:
                if (tokenizeLength > 4) idCardTokens.add(stringToTokenize.replaceAll("\\*", ""));
                break;
        }
    }

    @Override
    public final void reset() throws IOException {
        super.reset();
        this.position = 0;
        this.idCardTokens = null;
        this.stringToTokenize = null;
    }
}
