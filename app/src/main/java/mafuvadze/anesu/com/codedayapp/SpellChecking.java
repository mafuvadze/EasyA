package mafuvadze.anesu.com.codedayapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.view.textservice.SentenceSuggestionsInfo;
import android.view.textservice.SpellCheckerSession;
import android.view.textservice.SuggestionsInfo;
import android.view.textservice.TextServicesManager;

import java.util.Locale;

/**
 * Created by PLTW on 11/17/2015.
 */
public class SpellChecking extends Service implements SpellCheckerSession.SpellCheckerSessionListener {

    @Override
    public void onGetSuggestions(SuggestionsInfo[] results) {

    }

    @Override
    public void onGetSentenceSuggestions(SentenceSuggestionsInfo[] results) {

    }

    private void fetchSuggestionsFor(String input){
        TextServicesManager tsm =
                (TextServicesManager) getSystemService(TEXT_SERVICES_MANAGER_SERVICE);

        SpellCheckerSession session =
                tsm.newSpellCheckerSession(null, Locale.ENGLISH, this, true);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
