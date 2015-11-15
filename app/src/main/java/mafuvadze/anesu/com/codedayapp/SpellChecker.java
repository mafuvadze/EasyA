package mafuvadze.anesu.com.codedayapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.service.textservice.SpellCheckerService;
import android.view.textservice.SuggestionsInfo;
import android.view.textservice.TextInfo;

public class SpellChecker extends SpellCheckerService {
    public SpellChecker() {
    }

    @Override
    public Session createSession() {
        return null;
    }

    private static class AndroidSpellCheckerSession extends Session
    {

        @Override
        public void onCreate() {

        }

        @Override
        public SuggestionsInfo onGetSuggestions(TextInfo textInfo, int suggestionsLimit) {
            return null;
        }
    }
}
