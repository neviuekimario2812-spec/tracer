package com.example.tracer;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.util.Log;
import androidx.core.content.ContextCompat;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

public class NotificationView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_view);

        LinearLayout container = findViewById(R.id.notificationContainer);

        SupabaseHelper.fetchNotifications(new SupabaseHelper.NotificationFetchCallback() {
            @Override
            public void onSuccess(JSONArray results) {
                runOnUiThread(() -> {
                    for (int i = 0; i < results.length(); i++) {
                        try {
                            JSONObject row = results.getJSONObject(i);
                            String title = row.optString("title", "(no title)");
                            String text = row.optString("text", "(no content)");
                            addNotificationEntry(container, title, text);
                        } catch (JSONException e) {
                            Log.e("NotificationView", "Row parse error: " + e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onFailure(String error) {
                Log.e("NotificationView", "Fetch failed: " + error);
            }
        });
    }

    private void addNotificationEntry(LinearLayout container, String title, String text) {
        // The "card" wrapper — this holds the background, padding and margin
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

        // Title — bold, its own line
        TextView titleView = new TextView(this);
        titleView.setText(title);
        titleView.setTextSize(16);
        titleView.setTypeface(null, Typeface.BOLD);
        titleView.setTextColor(ContextCompat.getColor(this, R.color.card_background));

        // Body — normal weight, below the title
        TextView bodyView = new TextView(this);
        bodyView.setText(text);
        bodyView.setTextSize(14);
        bodyView.setTextColor(ContextCompat.getColor(this, R.color.card_background));
        bodyView.setPadding(0, 6, 0, 0); // small gap below the title

        card.addView(titleView);
        card.addView(bodyView);
        container.addView(card);
    }
}