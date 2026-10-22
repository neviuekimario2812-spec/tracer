package com.example.tracer;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SmsView extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_sms_view);

            LinearLayout container = findViewById(R.id.smsContainer);

            SupabaseHelper.fetchSms(new SupabaseHelper.SmsFetchCallback() {
                @Override
                public void onSuccess(JSONArray results) {
                    runOnUiThread(() -> {
                        for (int i = 0; i < results.length(); i++) {
                            try {
                                JSONObject row = results.getJSONObject(i);
                                String sender = row.optString("sender", "(unknown sender)");
                                String body = row.optString("body", "(no content)");
                                addSmsEntry(container, sender, body);
                            } catch (JSONException e) {
                                Log.e("SmsView", "Row parse error: " + e.getMessage());
                            }
                        }
                    });
                }

                @Override
                public void onFailure(String error) {
                    Log.e("SmsView", "Fetch failed: " + error);
                }
            });
        }

        private void addSmsEntry(LinearLayout container, String sender, String body) {
            LinearLayout card = new LinearLayout(this);
            card.setOrientation(LinearLayout.VERTICAL);
            card.setBackgroundResource(R.drawable.sections);
            card.setPadding(30, 20, 30, 20);

            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            cardParams.topMargin = 15;
            cardParams.leftMargin = 5;
            cardParams.rightMargin = 5;
            card.setLayoutParams(cardParams);

            TextView senderView = new TextView(this);
            senderView.setText(sender);
            senderView.setTextSize(16);
            senderView.setTypeface(null, Typeface.BOLD);
            senderView.setTextColor(ContextCompat.getColor(this, R.color.card_background));

            TextView bodyView = new TextView(this);
            bodyView.setText(body);
            bodyView.setTextSize(14);
            bodyView.setTextColor(ContextCompat.getColor(this, R.color.card_background));
            bodyView.setPadding(0, 6, 0, 0);

            card.addView(senderView);
            card.addView(bodyView);
            container.addView(card);
        }
    }