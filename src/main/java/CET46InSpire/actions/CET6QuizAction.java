package CET46InSpire.actions;

import CET46InSpire.helpers.CET46Settings;

public class CET6QuizAction extends QuizAction {
    private static final String LEXICON;
    private static final String VOCABULARY_ID;
    private static final int VOCABULARY_SIZE;

    public CET6QuizAction() {
        super(LEXICON, VOCABULARY_ID, VOCABULARY_SIZE);
    }

    static {
        LEXICON = "CET6";
        VOCABULARY_ID = "CET46:CET6_";
        VOCABULARY_SIZE = CET46Settings.VOCABULARY_CET6;
    }
}
